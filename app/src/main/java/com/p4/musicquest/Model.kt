package com.p4.musicquest

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import java.nio.ByteBuffer
import android.opengl.GLES30 as gl
import java.nio.FloatBuffer
import java.nio.IntBuffer

class Model(private val context: Context, objPath: String, texPath: String? = null, scale: Float = 1f, offX: Float = 0f, offY: Float = 0f, offZ: Float = 0f) {
    private val vao: Int
    private var indices: Array<Int>
	private var tex: Int? = null
    lateinit var collider: Collider

    init {
        // read OBJ source

        val src = context.assets.open(objPath).reader().use { it.readLines() }

        var vertices = arrayOf<Float>()
        var tempTexCoords = arrayOf<Float>()
        indices = arrayOf<Int>()

        var minX = 999999f
        var maxX = -999999f

        var minY = 999999f
        var maxY = -999999f

        var minZ = 999999f
        var maxZ = -999999f

        for (line in src) {
            val bits = line.split(" ")

            when (bits[0]) {
                "v" -> {
                    val x = offX + scale * bits[1].toFloat()
                    val y = offY + scale * bits[2].toFloat()
                    val z = offZ + scale * -bits[3].toFloat()

                    minX = minOf(x, minX)
                    maxX = maxOf(x, maxX)

                    minY = minOf(y, minY)
                    maxY = maxOf(y, maxY)

                    minZ = minOf(z, minZ)
                    maxZ = maxOf(z, maxZ)

                    vertices += x

                    // flip Y/Z axes

                    vertices += z
                    vertices += y

                    // leave space for texture coordinates

                    vertices += 0f
                    vertices += 0f
                }

                "vt" -> {
                    tempTexCoords += bits[1].toFloat()
                    tempTexCoords += bits[2].toFloat()
                }

                "f" -> {
                    for (i in 1..3) {
                        if (!bits[i].contains("/")) {
                            indices += bits[i].toInt() - 1 // XXX don't know why I've got to do - 1
                            continue
                        }

                        val (vertexIndexStr, texCoordIndexStr, normalIndex) = bits[i].split("/")

                        val vertexIndex = vertexIndexStr.toInt() - 1
                        val texCoordIndex = texCoordIndexStr.toInt() - 1

                        vertices[vertexIndex * 5 + 3] = tempTexCoords[texCoordIndex * 2 + 0]
                        vertices[vertexIndex * 5 + 4] = tempTexCoords[texCoordIndex * 2 + 1]

                        indices += vertexIndex
                    }
                }
            }
        }

        val verticesBuf = FloatBuffer.wrap(vertices.toFloatArray())
        verticesBuf.rewind()

        val indicesBuf = IntBuffer.wrap(indices.toIntArray())
        indicesBuf.rewind()

        // create collider

        collider = Collider(minX, minY, minZ, maxX, maxY, maxZ)

        // create VAO

        val vaoBuf = IntBuffer.allocate(4)
        gl.glGenVertexArrays(1, vaoBuf)
        vao = vaoBuf[0]
        gl.glBindVertexArray(vao)

        // create VBO

        val vboBuf = IntBuffer.allocate(1)
        gl.glGenBuffers(1, vboBuf)
        val vbo = vboBuf[0]
        gl.glBindBuffer(gl.GL_ARRAY_BUFFER, vbo)

        gl.glBufferData(gl.GL_ARRAY_BUFFER, vertices.size * 4, verticesBuf, gl.GL_STATIC_DRAW)

        gl.glVertexAttribPointer(0, 3, gl.GL_FLOAT, false, 4 * 5, 4 * 0)
        gl.glEnableVertexAttribArray(0)

        gl.glVertexAttribPointer(1, 2, gl.GL_FLOAT, false, 4 * 5, 4 * 3)
        gl.glEnableVertexAttribArray(1)

        // create IBO

        val iboBuf = IntBuffer.allocate(1)
        gl.glGenBuffers(1, iboBuf)
        val ibo = iboBuf[0]
        gl.glBindBuffer(gl.GL_ELEMENT_ARRAY_BUFFER, ibo)

        gl.glBufferData(gl.GL_ELEMENT_ARRAY_BUFFER, indices.size * 4, indicesBuf, gl.GL_STATIC_DRAW)

        // load texture

        if (texPath != null) {
            val texBuf = IntBuffer.allocate(1)
            gl.glGenTextures(1, texBuf)
            tex = texBuf[0]
            gl.glBindTexture(gl.GL_TEXTURE_2D, tex!!)

            val bitmap = context.assets.open(texPath).use { BitmapFactory.decodeStream(it) }
            val buf = ByteBuffer.allocate(bitmap.byteCount)
	        bitmap.copyPixelsToBuffer(buf)
	        buf.rewind()

            gl.glTexImage2D(gl.GL_TEXTURE_2D, 0, gl.GL_RGBA, bitmap.width, bitmap.height, 0, gl.GL_RGBA, gl.GL_UNSIGNED_BYTE, buf)

            gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MIN_FILTER, gl.GL_NEAREST_MIPMAP_LINEAR)
            gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MAG_FILTER, gl.GL_NEAREST)

            gl.glGenerateMipmap(gl.GL_TEXTURE_2D)
        }
    }

    fun draw(shader: Shader) {
		if (tex != null) {
			gl.glActiveTexture(gl.GL_TEXTURE0)
			shader.setSampler(0)
			gl.glBindTexture(gl.GL_TEXTURE_2D, tex!!)
		}

        gl.glBindVertexArray(vao)
        gl.glDrawElements(gl.GL_TRIANGLES, indices.size, gl.GL_UNSIGNED_INT, 0)
    }
}

package com.p4.musicquest

import android.content.Context
import android.opengl.GLES30 as gl
import java.nio.FloatBuffer
import java.nio.IntBuffer

class Model(private val context: Context, objPath: String, texPath: String? = null) {
    private val vao: Int
    private var indices: Array<Int>

    init {
        // read OBJ source

        val src = context.assets.open(objPath).reader().use { it.readLines() }

        var vertexPositions = arrayOf<Float>()
        indices = arrayOf<Int>()

        for (line in src) {
            val bits = line.split(" ")

            when (bits[0]) {
                "v" -> {
                    vertexPositions += bits[1].toFloat()
                    vertexPositions += bits[2].toFloat()
                    vertexPositions += bits[3].toFloat()
                }

                "f" -> {
                    // XXX don't know why I've got to do - 1

                    indices += bits[1].toInt() - 1
                    indices += bits[2].toInt() - 1
                    indices += bits[3].toInt() - 1
                }
            }
        }

        val vertexPositionsBuf = FloatBuffer.wrap(vertexPositions.toFloatArray())
        vertexPositionsBuf.rewind()

        val indicesBuf = IntBuffer.wrap(indices.toIntArray())
        indicesBuf.rewind()

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

        gl.glBufferData(gl.GL_ARRAY_BUFFER, vertexPositions.size * 4, vertexPositionsBuf, gl.GL_STATIC_DRAW)

        gl.glVertexAttribPointer(0, 3, gl.GL_FLOAT, false, 0, 0)
        gl.glEnableVertexAttribArray(0)

        // create IBO

        val iboBuf = IntBuffer.allocate(1)
        gl.glGenBuffers(1, iboBuf)
        val ibo = iboBuf[0]
        gl.glBindBuffer(gl.GL_ELEMENT_ARRAY_BUFFER, ibo)

        gl.glBufferData(gl.GL_ELEMENT_ARRAY_BUFFER, indices.size * 4, indicesBuf, gl.GL_STATIC_DRAW)

        // TODO textures
    }

    fun draw() {
        gl.glBindVertexArray(vao)
        gl.glDrawElements(gl.GL_TRIANGLES, indices.size, gl.GL_UNSIGNED_INT, 0)
    }
}
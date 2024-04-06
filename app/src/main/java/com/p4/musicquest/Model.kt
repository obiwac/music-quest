package com.p4.musicquest

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.channels.Channel
import java.nio.ByteBuffer
import java.nio.ByteOrder
import android.opengl.GLES30 as gl
import java.nio.FloatBuffer
import java.nio.IntBuffer
import java.nio.channels.Channels

class Model(private val context: Context, ivxPath: String, texPath: String? = null) {
    companion object {
        const val IVX_HEADER_SIZE = 1080
    }

    private val vao: Int
	private var tex: Texture? = null
    private var indexCount: Int

    init {
        val stream = context.assets.open(ivxPath)
        val channel = Channels.newChannel(stream)

        val header = ByteBuffer.allocate(IVX_HEADER_SIZE).order(ByteOrder.LITTLE_ENDIAN)
        channel.read(header)
        header.flip()
        println("IVX version ${header.long}.${header.long}")
        header.position(1040) // skip version + name

        indexCount = header.long.toInt()
        val indexOffset = header.long

        val vertexCount = header.long.toInt()
        val components = header.long.toInt()
        val offset = header.long

        val vertexBuf = ByteBuffer.allocate(4 * components * vertexCount).order(ByteOrder.LITTLE_ENDIAN)
        val indexBuf = ByteBuffer.allocate(4 * indexCount).order(ByteOrder.LITTLE_ENDIAN)

        stream.reset()
        stream.skip(offset)
        channel.read(vertexBuf)
        stream.reset()
        stream.skip(indexOffset)
        channel.read(indexBuf)

        vertexBuf.rewind()
        indexBuf.rewind()

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

        gl.glBufferData(gl.GL_ARRAY_BUFFER, vertexCount * 4 * components, vertexBuf, gl.GL_STATIC_DRAW)

        gl.glVertexAttribPointer(0, 3, gl.GL_FLOAT, false, 4 * components, 4 * 0)
        gl.glEnableVertexAttribArray(0)

        gl.glVertexAttribPointer(1, 2, gl.GL_FLOAT, false, 4 * components, 4 * 3)
        gl.glEnableVertexAttribArray(1)

        // create IBO

        val iboBuf = IntBuffer.allocate(1)
        gl.glGenBuffers(1, iboBuf)
        val ibo = iboBuf[0]
        gl.glBindBuffer(gl.GL_ELEMENT_ARRAY_BUFFER, ibo)

        gl.glBufferData(gl.GL_ELEMENT_ARRAY_BUFFER, indexCount * 4, indexBuf, gl.GL_STATIC_DRAW)

        // load texture

        if (texPath != null) {
            tex = Texture(context, texPath)
        }
    }

    fun draw(shader: Shader) {
		if (tex != null) {
			gl.glActiveTexture(gl.GL_TEXTURE0)
			shader.setSampler(0)
			gl.glBindTexture(gl.GL_TEXTURE_2D, tex!!.tex)
		}

        gl.glBindVertexArray(vao)
        gl.glDrawElements(gl.GL_TRIANGLES, indexCount, gl.GL_UNSIGNED_INT, 0)
    }
}

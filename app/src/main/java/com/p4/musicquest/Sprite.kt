package com.p4.musicquest

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES30 as gl
import java.nio.ByteBuffer
import java.nio.FloatBuffer
import java.nio.IntBuffer

class Sprite(private val context: Context, texPath: String?) {
	private val vao: Int
	//private var indices: Array<Int>
	private var tex: Int? = null

	init {

		// coord of each vertex

		var vertices = floatArrayOf(
			-0.5f,  0.5f, 1.0f,
			-0.5f, -0.5f, 1.0f,
			0.5f, -0.5f, 1.0f,
			0.5f,  0.5f, 1.0f
		)

		// triangle

		var indices = intArrayOf(
			0, 1, 2,
			0, 2, 3
		)


		val verticesBuf = FloatBuffer.wrap(vertices)
		verticesBuf.rewind()

		val indicesBuf = IntBuffer.wrap(indices)
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


}
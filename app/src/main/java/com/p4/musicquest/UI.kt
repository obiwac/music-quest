package com.p4.musicquest

import android.content.Context
import android.opengl.GLES30 as gl
import com.p4.musicquest.ui.Heart
import java.nio.ByteBuffer
import java.nio.IntBuffer
import java.nio.FloatBuffer

enum class UIRefCorner {
	TOP_LEFT, TOP_RIGHT,
	BOTTOM_LEFT, BOTTOM_RIGHT,
}

class UI(val context: Context) {
	private val vao: Int
	var xRes = 1
	var yRes = 1

	// various elements

	val heart = Heart(this) // TODO pass UI so it can draw

	init {
		val vertices = FloatBuffer.wrap(floatArrayOf(
			-1f, -1f, .5f, 1f, 0f,
			-1f, +1f, .5f, 0f, 0f,
			+1f, +1f, .5f, 0f, 1f,
			+1f, -1f, .5f, 1f, 1f,
		))

		val indices = ByteBuffer.wrap(byteArrayOf(
			0, 1, 2,
			0, 2, 3,
		))

		vertices.rewind()
		indices.rewind()

		// create vao

		val vaoBuf = IntBuffer.allocate(4)
		gl.glGenVertexArrays(1, vaoBuf)
		vao = vaoBuf[0]
		gl.glBindVertexArray(vao)

		// create VBO

		val vboBuf = IntBuffer.allocate(1)
		gl.glGenBuffers(1, vboBuf)
		val vbo = vboBuf[0]
		gl.glBindBuffer(gl.GL_ARRAY_BUFFER, vbo)

		gl.glBufferData(gl.GL_ARRAY_BUFFER, 4 * 4 * 5, vertices, gl.GL_STATIC_DRAW)

		gl.glVertexAttribPointer(0, 3, gl.GL_FLOAT, false, 4 * 5, 4 * 0)
		gl.glEnableVertexAttribArray(0)

		gl.glVertexAttribPointer(1, 2, gl.GL_FLOAT, false, 4 * 5, 4 * 3)
		gl.glEnableVertexAttribArray(1)

		// create IBO

		val iboBuf = IntBuffer.allocate(1)
		gl.glGenBuffers(1, iboBuf)
		val ibo = iboBuf[0]
		gl.glBindBuffer(gl.GL_ELEMENT_ARRAY_BUFFER, ibo)

		gl.glBufferData(gl.GL_ELEMENT_ARRAY_BUFFER, 6, indices, gl.GL_STATIC_DRAW)
	}

	fun updateResolution(xRes: Int, yRes: Int) {
		this.xRes = xRes
		this.yRes = yRes
	}

	fun draw(shader: Shader, dt: Float) {
		gl.glDisable(gl.GL_DEPTH_TEST)
		gl.glClear(gl.GL_DEPTH_BUFFER_BIT)
		gl.glBindVertexArray(vao)
		heart.draw(shader, dt)
	}
}
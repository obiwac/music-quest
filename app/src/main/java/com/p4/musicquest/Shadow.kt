package com.p4.musicquest

import android.content.Context
import android.opengl.GLES30 as gl
import java.nio.FloatBuffer
import java.nio.IntBuffer

class Shadow(context: Context, size: Float) {
	companion object {
		private var shadow: Shadow? = null

		fun getShadow(context: Context, size: Float): Shadow {
			if (shadow == null) {
				shadow = Shadow(context, size)
			}

			return shadow!!
		}
	}

	private val vao: Int
	private var indices: IntArray
	private var vertices: FloatArray
	private var tex: Texture? = null

	init {
		vertices = floatArrayOf(
			-size, -size, 0f, 0f, 0f, // bottom left
			-size, size, 0f, 0f, 1f, // top left
			size, size, 0f, 1f, 1f, // top right
			size, -size, 0f, 1f, 0f, // bottom right
		)

		// triangle

		indices = intArrayOf(    //xxx
			0, 1, 2,             //x o
			0, 2, 3              //ooo
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

		tex = Texture(context, "textures/shadow.png")
	}

	fun draw(shader: Shader, camera: Camera, x: Float, y: Float, z: Float) {
		gl.glActiveTexture(gl.GL_TEXTURE0)
		shader.setSampler(0)
		gl.glBindTexture(gl.GL_TEXTURE_2D, tex!!.tex)
		gl.glBindVertexArray(vao)
		shader.setMvp(camera.mvp(x, y + .01f, z - .05f, tilt = true))
		gl.glDrawElements(gl.GL_TRIANGLES, indices.size, gl.GL_UNSIGNED_INT, 0)
	}
}
package com.p4.musicquest

import android.content.Context
import java.nio.FloatBuffer
import java.nio.IntBuffer
import android.opengl.GLES30 as gl


class Sprite(private val context: Context, texPath: String?, dimension: FloatArray, size: FloatArray, multiplicator: Float = 1f) {
	private val vao: Int
	private var indices: IntArray
	private var vertices: FloatArray
	private var tex: Texture? = null

	init {

		// coord of each vertex

		val position = outline(dimension[0], dimension[1], dimension[2], dimension[3], size)

		vertices = floatArrayOf(
			// position    // texture
			-0.40f * multiplicator, -0f,                   0f, 1f - position[3], position[0], // bottom left
			-0.40f * multiplicator, 0.80f * multiplicator, 0f, 1f - position[2], position[0], // top left
			0.40f * multiplicator,  0.80f * multiplicator, 0f, 1f - position[2], position[1], // top right
			0.40f * multiplicator,  -0f,                   0f, 1f - position[3], position[1], // bottom right
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

		// premier argument : layout (0 : position, 1 : texture)
		// sert à indiquer à la memoire
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
			tex = Texture(context, texPath)
		}
	}

	fun draw(shader: Shader, camera: Camera, x: Float, y: Float, z: Float) {
		gl.glActiveTexture(gl.GL_TEXTURE0)
		shader.setSampler(0)
		gl.glBindTexture(gl.GL_TEXTURE_2D, tex!!.tex)
		gl.glBindVertexArray(vao)
		shader.setMvp(camera.mvp(x, y, z - .3f, tilt = false))
		gl.glDrawElements(gl.GL_TRIANGLES, indices.size, gl.GL_UNSIGNED_INT, 0)
	}

	fun outline(x: Float, y: Float, width: Float, height: Float, size: FloatArray): FloatArray {
		/*
		Prend les coordonnées du point top left du rectangle du sprite et
		retourne les dimensions à mettre dans les vertices
		Size : dimension de l'image
		 */

		val left = x / size[0]
		val right = (width + x) / size[0]
		val top = 1f - (y / size[1])
		val bottom = top - (height / size[1])

		return floatArrayOf(left, right, top, bottom)
	}
}
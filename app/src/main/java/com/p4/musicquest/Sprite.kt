package com.p4.musicquest

import android.content.Context
import android.graphics.BitmapFactory
import java.nio.ByteBuffer
import java.nio.FloatBuffer
import java.nio.IntBuffer
import javax.microedition.khronos.opengles.GL10.GL_BLEND
import javax.microedition.khronos.opengles.GL10.GL_ONE_MINUS_SRC_ALPHA
import javax.microedition.khronos.opengles.GL10.GL_SRC_ALPHA
import android.opengl.GLES30 as gl


class Sprite(private val context: Context, texPath: String?) {
	private val vao: Int
	private var indices: IntArray
	private var vertices: FloatArray
	private var tex: Int? = null

	init {

		// coord of each vertex

		vertices = floatArrayOf(
			// position    // texture (0.03 width, 0.15 high)
			-0.5f, -0.5f, 0.5f,  0.00f, 0.75f, // bottom left
			-0.5f, 0.5f, 0.5f,  0.00f, 1f, // top left
			0.5f, 0.5f, 0.5f,  0.03f, 1f, // top right
			0.5f, -0.5f, 0.5f,  0.03f, 0.75f, // bottom right
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
			val texBuf = IntBuffer.allocate(1)
			gl.glGenTextures(1, texBuf)
			tex = texBuf[0]
			gl.glBindTexture(gl.GL_TEXTURE_2D, tex!!)

			val bitmap = context.assets.open(texPath).use { BitmapFactory.decodeStream(it) }
			val buf = ByteBuffer.allocate(bitmap.byteCount)
			bitmap.copyPixelsToBuffer(buf)
			buf.rewind()

			// transparent background
			gl.glEnable(GL_BLEND)
			gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

			gl.glTexImage2D(gl.GL_TEXTURE_2D, 0, gl.GL_RGBA, bitmap.width, bitmap.height, 0, gl.GL_RGBA, gl.GL_UNSIGNED_BYTE, buf)
			gl.glTexSubImage2D(gl.GL_TEXTURE_2D, 0, 0, 0, bitmap.width, bitmap.height / 2, gl.GL_RGBA, gl.GL_UNSIGNED_BYTE, buf)

			gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MIN_FILTER, gl.GL_NEAREST_MIPMAP_LINEAR)
			gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MAG_FILTER, gl.GL_NEAREST)

			gl.glGenerateMipmap(gl.GL_TEXTURE_2D)
		}
	}

	fun draw(shader: Shader) {
		gl.glActiveTexture(gl.GL_TEXTURE0)
		shader.setSampler(0)
		gl.glBindTexture(gl.GL_TEXTURE_2D, tex!!)
		gl.glBindVertexArray(vao)
		gl.glDrawElements(gl.GL_TRIANGLES, indices.size, gl.GL_UNSIGNED_INT, 0)
	}

	fun movement(x: Float, y: Float) {
		vertices[0] += x
		vertices[1] += y
		vertices[5] += x
		vertices[6] += y
		vertices[10] += x
		vertices[11] += y
		vertices[15] += x
		vertices[16] += y
	}


}
package com.p4.musicquest

import android.content.Context
import android.view.MotionEvent
import com.p4.musicquest.entities.Player
import com.p4.musicquest.ui.Button
import android.opengl.GLES30 as gl
import com.p4.musicquest.ui.Heart
import com.p4.musicquest.ui.Joystick
import java.nio.ByteBuffer
import java.nio.IntBuffer
import java.nio.FloatBuffer

enum class UIRefCorner {
	TOP_LEFT, TOP_RIGHT,
	BOTTOM_LEFT, BOTTOM_RIGHT,
}

class UI(val context: Context, player: Player) {
	private val vao: Int
	var xRes = 1
	var yRes = 1
	var aspect = 1f

	// various elements

	private val heart = Heart(this, player)
	private val joystick = Joystick(this, player)

	// TODO texture here is temporary
	private val sword = Button(this, "ui/joystick-thumb.png", UIRefCorner.BOTTOM_RIGHT, .05f, .1f, 0.2f) {
		player.attackWithSword()
	}

	init {
		val vertices = FloatBuffer.wrap(floatArrayOf(
			-.5f, -.5f, .5f, 1f, 0f,
			-.5f, +.5f, .5f, 0f, 0f,
			+.5f, +.5f, .5f, 0f, 1f,
			+.5f, -.5f, .5f, 1f, 1f,
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

		aspect = xRes.toFloat() / yRes
	}

	fun onTouchEvent(event: MotionEvent) {
		val x =   event.x / xRes * 2 - 1f
		val y = -(event.y / yRes * 2 - 1f)

		joystick.onTouchEvent(event, x, y)
		sword.onTouchEvent(event, x, y)
	}

	fun draw(shader: Shader, dt: Float) {
		gl.glDisable(gl.GL_DEPTH_TEST)

		gl.glEnable(gl.GL_BLEND)
		gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA)

		gl.glBindVertexArray(vao)

		heart.draw(shader, dt)
		joystick.draw(shader, dt)
		sword.draw(shader, dt)
	}
}
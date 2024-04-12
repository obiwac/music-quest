package com.p4.musicquest

import android.content.Context
import android.opengl.GLES30
import android.view.MotionEvent
import com.p4.musicquest.entities.Player
import com.p4.musicquest.ui.Button
import com.p4.musicquest.ui.Heart
import com.p4.musicquest.ui.Joystick

class UIPlaying(context: Context, player: Player): UI(context) {

	// various elements

	private val heart = Heart(this, player)
	private val joystick = Joystick(this, player)

	// TODO texture here is temporary
	private val sword = Button(this, "ui/joystick-thumb.png", UIRefCorner.BOTTOM_RIGHT, .05f, .1f, 0.2f) {
		player.attackWithSword()
	}

	override fun updateResolution(xRes: Int, yRes: Int) {
		this.xRes = xRes
		this.yRes = yRes

		aspect = xRes.toFloat() / yRes
	}

	override fun onTouchEvent(event: MotionEvent) {
		val x =   event.x / xRes * 2 - 1f
		val y = -(event.y / yRes * 2 - 1f)

		joystick.onTouchEvent(event, xRes.toFloat(), yRes.toFloat())
		sword.onTouchEvent(event, xRes.toFloat(), yRes.toFloat())
	}

	override fun draw(shader: Shader, dt: Float) {
		GLES30.glDisable(GLES30.GL_DEPTH_TEST)

		GLES30.glEnable(GLES30.GL_BLEND)
		GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE_MINUS_SRC_ALPHA)

		GLES30.glBindVertexArray(vao)

		heart.draw(shader, dt)
		joystick.draw(shader, dt)
		sword.draw(shader, dt)
	}

}
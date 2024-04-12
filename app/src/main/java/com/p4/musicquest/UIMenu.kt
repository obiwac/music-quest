package com.p4.musicquest

import android.content.Context
import android.opengl.GLES30
import android.view.MotionEvent
import com.p4.musicquest.entities.Player
import com.p4.musicquest.ui.Button

class UIMenu(context: Context, val game: Game) : UI(context) {

	private val buttonTest = Button(this, "ui/joystick-thumb.png", UIRefCorner.BOTTOM_RIGHT, .05f, .1f, 0.8f) {
		println("button menu")
	}

	override fun updateResolution(xRes: Int, yRes: Int) {
		this.xRes = xRes
		this.yRes = yRes

		aspect = xRes.toFloat() / yRes
	}

	override fun onTouchEvent(event: MotionEvent) {
		val x = event.x / xRes * 2 - 1f
		val y = -(event.y / yRes * 2 - 1f)

	}

	override fun draw(shader: Shader, dt: Float) {
		GLES30.glDisable(GLES30.GL_DEPTH_TEST)

		GLES30.glEnable(GLES30.GL_BLEND)
		GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE_MINUS_SRC_ALPHA)

		GLES30.glBindVertexArray(vao)

		buttonTest.draw(shader, dt)

	}

	fun onTouchEvents(event: MotionEvent) {
		if (event.action == MotionEvent.ACTION_DOWN) {
			println("here")
			game.currentGameState = Game.GameState.PLAYING
			game.changeRenderer()
		}
	}
}
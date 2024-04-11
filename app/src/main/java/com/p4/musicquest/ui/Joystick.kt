package com.p4.musicquest.ui

import android.view.MotionEvent
import com.p4.musicquest.Shader
import com.p4.musicquest.UI
import com.p4.musicquest.UIRefCorner
import com.p4.musicquest.entities.Player
import kotlin.math.sqrt

class Joystick(private val ui: UI, private val player: Player) {
	companion object {
		const val SIZE = .4f
		const val THUMB_RATIO = 2
		const val OFF_X = .05f
		const val OFF_Y = .1f
		const val THUMB_SIZE = SIZE / THUMB_RATIO
		const val THUMB_INIT_X = OFF_X + SIZE / 2 - THUMB_SIZE / 2
		const val THUMB_INIT_Y = OFF_Y + SIZE / 2 - THUMB_SIZE / 2
		const val THUMB_DIST = SIZE / 2
	}

	private val base = Element(ui, "ui/joystick-base.png", UIRefCorner.BOTTOM_LEFT, OFF_X, OFF_Y, SIZE, SIZE)
	private val thumb = Element(ui, "ui/joystick-thumb.png", UIRefCorner.BOTTOM_LEFT, THUMB_INIT_X, THUMB_INIT_Y, THUMB_SIZE, THUMB_SIZE)

	private var pressing = false
	var joystickPointerId = -1

	fun draw(shader: Shader, dt: Float) {
		base.draw(shader, dt)
		thumb.draw(shader, dt)
	}

	fun onTouchEvent(event: MotionEvent, xRes: Float, yRes: Float) {
		val action = event.actionMasked
		val actionIndex = event.actionIndex
		val pointerId = event.getPointerId(actionIndex)

		var x =    event.getX(event.findPointerIndex(pointerId)) / xRes * 2 - 1f
		var y = - (event.getY(event.findPointerIndex(pointerId)) / yRes * 2 - 1f)

		when (action) {
			MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
				if (base.containsPoint(x, y) && joystickPointerId == -1) {
					joystickPointerId = pointerId
					pressing = true
				}
			}

			MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
				if (pointerId == joystickPointerId) {
					pressing = false
					joystickPointerId = -1

					// reset joystick position
					thumb.targetX = THUMB_INIT_X
					thumb.targetY = THUMB_INIT_Y
				}

			}
		}

		if (!pressing) {
			player.input[0] = 0f
			player.input[1] = 0f

			return
		}

		x =    event.getX(event.findPointerIndex(joystickPointerId)) / xRes * 2 - 1f
		y = - (event.getY(event.findPointerIndex(joystickPointerId)) / yRes * 2 - 1f)

		val thumbInitX = -1f + (OFF_X + SIZE / 2) * 2
		val thumbInitY = -1f + (OFF_Y + SIZE / 2) * 2 * ui.aspect

		var cx = (x - thumbInitX) / 2
		var cy = (y - thumbInitY) / 2 / ui.aspect

		val mag = sqrt(cx * cx + cy * cy)

		if (mag > THUMB_DIST) {
			cx /= mag / THUMB_DIST
			cy /= mag / THUMB_DIST
		}

		player.input[0] = cx / THUMB_DIST
		player.input[1] = cy / THUMB_DIST

		thumb.targetX = THUMB_INIT_X + cx
		thumb.targetY = THUMB_INIT_Y + cy
	}
}
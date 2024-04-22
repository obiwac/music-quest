package com.p4.musicquest.ui

import android.view.MotionEvent
import com.p4.musicquest.Shader
import com.p4.musicquest.Texture
import com.p4.musicquest.UI
import com.p4.musicquest.UIRefCorner

class ButtonAnimated (val ui: UI, val texPath: ArrayList<String>, refCorner: UIRefCorner, x: Float, y: Float, width: Float, height: Float, var onClick: () -> Unit) : Element(ui, null, refCorner, x, y, width, height){
	private var pressing = false
	var buttonPointerId = -1
	var textureIdx = 0

	fun onTouchEvent(event: MotionEvent, xRes: Float, yRes: Float) {
		val action = event.actionMasked
		val actionIndex = event.actionIndex
		val pointerId = event.getPointerId(actionIndex)

		val x =    event.getX(event.findPointerIndex(pointerId)) / xRes * 2 - 1f
		val y = - (event.getY(event.findPointerIndex(pointerId)) / yRes * 2 - 1f)

		when (action) {
			MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
				if (containsPoint(x, y) && buttonPointerId == -1) {
					pressing = true
					buttonPointerId = pointerId
					textureIdx = 1

				}
			}

			MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP-> {
				if (containsPoint(x, y) && pointerId == buttonPointerId) {
					pressing = false
					buttonPointerId = -1
					textureIdx = 0
					onClick()
				}
			}
		}

		if (!pressing) {
			return
		}

		//onClick()
	}

	override fun draw(shader: Shader, dt: Float) {
		setTex(shader, Texture(ui.context, texPath[textureIdx]))
		super.draw(shader, dt)
	}


}
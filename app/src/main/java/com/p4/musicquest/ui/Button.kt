package com.p4.musicquest.ui

import android.view.MotionEvent
import com.p4.musicquest.UI
import com.p4.musicquest.UIRefCorner

class Button(ui: UI, texPath: String, refCorner: UIRefCorner, x: Float, y: Float, size: Float, private val onClick: () -> Unit) : Element(ui, texPath, refCorner, x, y, size, size) {
	private var pressing = false
	var buttonPointerId = -1

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
				}
			}

			MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP-> {
				if (containsPoint(x, y) && pointerId == buttonPointerId) {
					pressing = false
					buttonPointerId = -1
					onClick()
				}
			}
		}

		if (!pressing) {
			return
		}

		onClick()
	}

}
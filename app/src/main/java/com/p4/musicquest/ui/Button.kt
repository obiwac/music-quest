package com.p4.musicquest.ui

import android.view.MotionEvent
import com.p4.musicquest.UI
import com.p4.musicquest.UIRefCorner

class Button(ui: UI, texPath: String, refCorner: UIRefCorner, x: Float, y: Float, size: Float, private val onClick: () -> Unit) : Element(ui, texPath, refCorner, x, y, size, size) {
	private var pressing = false

	fun onTouchEvent(event: MotionEvent, x: Float, y: Float) {
		when (event.action) {
			MotionEvent.ACTION_DOWN -> {
				if (containsPoint(x, y)) {
					pressing = true
				}
			}

			MotionEvent.ACTION_UP -> {
				pressing = false

				if (containsPoint(x, y)) {
					onClick()
				}
			}
		}
	}
}
package com.p4.musicquest.ui

import android.content.Context
import android.view.MotionEvent
import com.p4.musicquest.Shader
import com.p4.musicquest.UI
import com.p4.musicquest.UIRefCorner

class Dialog(val context: Context, val ui: UI, private val onClick: () -> Unit) {

	var textInDialog: Text? = null
	var textToShow: String? = null
	var sizeFont: Float? = null

	// For touch event

	var pressing = false
	var dialogPointerId = -1


	fun initDialog(text: String, size: Float) {
		textToShow = text
		sizeFont = size
	}

	fun draw(shader: Shader, dt: Float) {
		if (textToShow != null && sizeFont != null) {
			val font = Font(ui, context, sizeFont!!)

			textInDialog = Text(ui, font, textToShow!!, UIRefCorner.TOP_LEFT, .1f, .25f, .8f)
			textInDialog!!.draw(shader, dt)
		}

	}

	fun onTouchEvent(event: MotionEvent, xRes: Float, yRes: Float) {
		val action = event.actionMasked
		val actionIndex = event.actionIndex
		val pointerId = event.getPointerId(actionIndex)

		when (action) {
			MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
				if (dialogPointerId == -1) {
					pressing = true
					dialogPointerId = pointerId
				}
			}

			MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP-> {
				if (pointerId == dialogPointerId) {
					pressing = false
					dialogPointerId = -1
					onClick()
				}
			}
		}
		if (!pressing) {
			return
		}
	}

}
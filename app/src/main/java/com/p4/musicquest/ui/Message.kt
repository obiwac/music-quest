package com.p4.musicquest.ui

import android.content.Context
import com.p4.musicquest.Shader
import com.p4.musicquest.UI
import com.p4.musicquest.UIRefCorner

class Message(context: Context, val ui: UI) {

	val font = Font(ui, context, 10f)
	var uiText = Text(ui, font, "Lorem Ipsum dolor\nsit amet. This line\nhere is a little\nlonger than the\nprevious. This\none is shorter.", UIRefCorner.TOP_LEFT, .1f, .5f, .8f)

	fun draw(shader: Shader, dt: Float) {

		val messageX = 0.1f
		var messageY = 0.5f

		for (i in 0..<ui.messageList.size) {
			if (i < ui.messageList.size) {

				// Show a message

				uiText = Text(ui, font, ui.messageList[i], UIRefCorner.TOP_LEFT, messageX, messageY, .8f)

				uiText.draw(shader, dt)

				// Multiple messages

				val counter = ui.messageCounter[i] + 1
				ui.messageCounter[i] = counter
				messageY -= 0.1f

				// Delete them after x time

				if (ui.messageCounter[i] > 90) {
					ui.messageList.removeAt(i)
					ui.messageCounter.removeAt(i)
				}
			}
		}
	}
}
package com.p4.musicquest.ui

import android.content.Context
import com.p4.musicquest.Shader
import com.p4.musicquest.UI
import com.p4.musicquest.UIRefCorner

class Message(context: Context, val ui: UI) {

	val font = Font(ui, context, 10f)

	fun draw(shader: Shader, dt: Float) {

		val messageX = 0.1f
		var messageY = 0.5f


		for (i in 0..<ui.messageTextList.size) {
			if (ui.messageTextList[i].text == null) {
				ui.messageTextList[i].text = Text(ui, font, ui.messageTextList[i].contents, UIRefCorner.TOP_LEFT, .1f, .5f, .8f)
			}

			ui.messageTextList[i].text?.x = messageX
			ui.messageTextList[i].text?.y = messageY

			ui.messageTextList[i].text?.draw(shader, dt)

			// Multiple messages

			ui.messageTextList[i].counter += dt
			messageY -= 0.1f

			// Delete them after x time

			if (ui.messageTextList[i].counter > 2f) {
				ui.messageTextList.removeAt(i)
			}
		}
		/*

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

		 */
	}
}
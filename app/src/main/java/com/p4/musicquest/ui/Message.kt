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
			if (i < ui.messageTextList.size) {
				if (ui.messageTextList[i].text == null) {
					ui.messageTextList[i].text = Text(ui, font, ui.messageTextList[i].contents, UIRefCorner.TOP_LEFT, .1f, messageY, .8f)
				}

				ui.messageTextList[i].text?.targetX = messageX
				ui.messageTextList[i].text?.targetY = messageY

				ui.messageTextList[i].text?.draw(shader, dt)

				// Multiple messages

				ui.messageTextList[i].counter += dt
				messageY -= 0.1f

				// Delete them after x time

				if (ui.messageTextList[i].counter > 2f) {
					ui.messageTextList.removeAt(i)
				}
			}

		}
	}
}
package com.p4.musicquest.ui

import com.p4.musicquest.Shader
import com.p4.musicquest.Texture
import com.p4.musicquest.UI
import com.p4.musicquest.UIRefCorner

class Heart(ui: UI) : Element(ui, null, UIRefCorner.TOP_LEFT, 0f, 0f, .2f, .2f) {
	private val hearts = arrayOf(
		Texture(ui.context, "ui/heart/coeur_30.png"),
		Texture(ui.context, "ui/heart/coeur_50.png"),
		Texture(ui.context, "ui/heart/coeur_60.png"),
		Texture(ui.context, "ui/heart/coeur_20.png"),
		Texture(ui.context, "ui/heart/coeur_100.png"),
	)

	override fun draw(shader: Shader, dt: Float) {
		setTex(shader, hearts[4])
		super.draw(shader, dt)
	}
}
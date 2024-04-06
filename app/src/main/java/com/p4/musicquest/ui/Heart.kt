package com.p4.musicquest.ui

import com.p4.musicquest.Shader
import com.p4.musicquest.Texture
import com.p4.musicquest.UI
import com.p4.musicquest.UIRefCorner
import com.p4.musicquest.entities.Player

class Heart(ui: UI, private val player: Player) : Element(ui, null, UIRefCorner.TOP_LEFT, -.1f, -.1f, .5f, .5f) {
	private val hearts = arrayOf(
		Texture(ui.context, "ui/heart/coeur_20.png"),
		Texture(ui.context, "ui/heart/coeur_30.png"),
		Texture(ui.context, "ui/heart/coeur_50.png"),
		Texture(ui.context, "ui/heart/coeur_60.png"),
		Texture(ui.context, "ui/heart/coeur_100.png"),
	)

	override fun draw(shader: Shader, dt: Float) {
		var i = 0

		if (player.health > 16) i = 4
		else if (player.health > 12) i = 3
		else if (player.health > 8) i = 2
		else if (player.health > 4) i = 1

		setTex(shader, hearts[i])
		super.draw(shader, dt)
	}
}
package com.p4.musicquest.ui

import com.p4.musicquest.Shader
import com.p4.musicquest.UI
import com.p4.musicquest.UIRefCorner

class Text(ui: UI, font: Font, text: String, refCorner: UIRefCorner, x: Float, y: Float, width: Float) : Element(ui, null, refCorner, x, y, width, width) {
	private val tex = font.render(text)

	override fun draw(shader: Shader, dt: Float) {
		val ratio = tex.xRes.toFloat() / tex.yRes
		height = width / ratio

		setTex(shader, tex)
		super.draw(shader, dt)
	}
}
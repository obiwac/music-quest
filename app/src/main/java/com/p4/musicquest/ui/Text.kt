package com.p4.musicquest.ui

import com.p4.musicquest.Shader
import com.p4.musicquest.UI
import com.p4.musicquest.UIRefCorner

class Text(ui: UI, font: Font, var text: String, refCorner: UIRefCorner, var x: Float, var y: Float, width: Float) : Element(ui, null, refCorner, x, y, width, width) {
	var tex = font.render(text)

	override fun draw(shader: Shader, dt: Float) {
		val ratio = tex.xRes.toFloat() / tex.yRes
		height = width / ratio

		setTex(shader, tex)
		super.draw(shader, dt)
	}
}
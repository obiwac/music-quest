package com.p4.musicquest

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.graphics.get

class Mask(context: Context) {
	private val bitmap: Bitmap
	val tex: Texture

	init {
		bitmap = context.assets.open("textures/mask.png").use { BitmapFactory.decodeStream(it) }
		tex = Texture(bitmap)
	}

	/**
	 * @param x - From 0 to 1, same coordinate space as GLSL.
	 * @param y - From 0 to 1, same coordinate space as GLSL.
	 */
	fun query(x: Float, y: Float): Int {
		return bitmap[(x * bitmap.width).toInt(), (y * bitmap.height).toInt()]
	}
}
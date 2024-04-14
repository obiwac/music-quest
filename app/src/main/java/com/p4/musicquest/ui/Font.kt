package com.p4.musicquest.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.p4.musicquest.Texture
import com.p4.musicquest.UI
import kotlin.math.ceil
import kotlin.math.max

// inspired by https://github.com/inobulles/aqua-android/blob/master/app/src/main/java/com/inobulles/obiwac/aqua/Font.java
// I do recall there were premultiplication issues with this though - do look into that

class Font(private val ui: UI, context: Context, size: Float) {
	private val face = Typeface.createFromAsset(context.assets, "fonts/visitor1.ttf")
	private val paint = TextPaint()

	init {
		paint.isAntiAlias = true
		paint.textSize = size
		paint.color = Color.WHITE
		paint.style = Paint.Style.FILL
		paint.typeface = face
		paint.hinting = Paint.HINTING_ON
	}

	fun render(text: String): Texture {
		val layout = StaticLayout.Builder
			.obtain(text, 0, text.length, paint, 1000) // make this big cuz we don't want wrapping if we don't need it
			.setAlignment(Layout.Alignment.ALIGN_NORMAL)
			.build()

		// layout.width is just the width we passed to it when building (i.e. 1000)

		var xRes = 0

		for (i in 0..<layout.lineCount) {
			xRes = max(xRes, ceil(layout.getLineWidth(i)).toInt())
		}

		val yRes = layout.height

		val bitmap = Bitmap.createBitmap(xRes, yRes, Bitmap.Config.ARGB_8888)
		val canvas = Canvas(bitmap)

		canvas.save()
		bitmap.eraseColor(Color.TRANSPARENT)

		canvas.translate(0f, 0f)
		layout.draw(canvas)
		canvas.restore()

		val texture = Texture(bitmap)
		bitmap.recycle()

		return texture
	}
}
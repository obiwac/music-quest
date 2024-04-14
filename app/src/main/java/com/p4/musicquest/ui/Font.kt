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
	private val fillPaint = TextPaint()
	private val strokePaint = TextPaint()
	private val strokeSize = size / 5

	companion object {
		private const val MAX_WIDTH = 1000 // make this big cuz we don't want wrapping if we don't need it
	}

	init {
		fillPaint.isAntiAlias = true
		fillPaint.textSize = size
		fillPaint.color = Color.WHITE
		fillPaint.style = Paint.Style.FILL
		fillPaint.typeface = face
		fillPaint.hinting = Paint.HINTING_ON

		strokePaint.isAntiAlias = true
		strokePaint.textSize = size
		strokePaint.color = Color.BLACK
		strokePaint.style = Paint.Style.STROKE
		strokePaint.strokeWidth = strokeSize
		strokePaint.typeface = face
		strokePaint.hinting = Paint.HINTING_ON
	}

	fun render(text: String): Texture {
		val layout = StaticLayout.Builder
			.obtain(text, 0, text.length, fillPaint, MAX_WIDTH)
			.setAlignment(Layout.Alignment.ALIGN_NORMAL)
			.build()

		// XXX not sure if there's a more optimal way, but I can't find an easy way to draw a layout to a canvas with different paint to the one that it was created with

		val strokeLayout = StaticLayout.Builder
			.obtain(text, 0, text.length, strokePaint, MAX_WIDTH) // make this big cuz we don't want wrapping if we don't need it
			.setAlignment(Layout.Alignment.ALIGN_NORMAL)
			.build()

		// for the resolution, base ourselves off of the stroke layout as it is a bit wider
		// layout.width is just the width we passed to it when building (i.e. MAX_WIDTH)

		var xRes = 0

		for (i in 0..<layout.lineCount) {
			xRes = max(xRes, ceil(layout.getLineWidth(i)).toInt())
		}

		xRes += ceil(strokeSize).toInt()
		val yRes = layout.height + ceil(strokeSize).toInt()

		val bitmap = Bitmap.createBitmap(xRes, yRes, Bitmap.Config.ARGB_8888)
		val canvas = Canvas(bitmap)

		canvas.save()
		bitmap.eraseColor(Color.TRANSPARENT)

		canvas.translate(strokeSize / 2, strokeSize / 2)
		strokeLayout.draw(canvas)
		layout.draw(canvas)
		canvas.restore()

		val texture = Texture(bitmap)
		bitmap.recycle()

		return texture
	}
}
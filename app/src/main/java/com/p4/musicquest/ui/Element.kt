package com.p4.musicquest.ui

import com.p4.musicquest.Matrix
import com.p4.musicquest.Shader
import com.p4.musicquest.Texture
import com.p4.musicquest.UI
import com.p4.musicquest.UIRefCorner
import android.opengl.GLES30 as gl

open class Element(private val ui: UI, texPath: String? = null, private val refCorner: UIRefCorner, private var x: Float, private var y: Float, private val width: Float, private val height: Float) {
	var targetX = x
	var targetY = y

	private var tex: Texture? = null

	init {
		if (texPath != null) {
			tex = Texture(ui.context, texPath)
		}
	}

	fun setTex(shader: Shader, tex: Texture) {
		gl.glActiveTexture(gl.GL_TEXTURE0)
		shader.setSampler(0)
		gl.glBindTexture(gl.GL_TEXTURE_2D, tex.tex)
	}

	var realWidth = 0f
	var realHeight = 0f

	var realX = 0f
	var realY = 0f

	var offX = 0f
	var offY = 0f

	var centreX = 0f
	var centreY = 0f

	fun updateBounds() {
		realWidth = width * 2
		realHeight = height * 2 * ui.aspect

		realX = x * 2
		realY = y * 2 * ui.aspect

		offX = 1f - (realWidth / 2 + realX)
		offY = 1f - (realHeight / 2 + realY)

		when (refCorner) {
			UIRefCorner.TOP_LEFT -> { centreX = -offX; centreY = offY }
			UIRefCorner.BOTTOM_LEFT -> { centreX = -offX; centreY = -offY }
			UIRefCorner.TOP_RIGHT -> { centreX = offX; centreY = offY }
			UIRefCorner.BOTTOM_RIGHT -> { centreX = offX; centreY = -offY }
		}
	}

	fun containsPoint(x: Float, y: Float): Boolean {
		updateBounds()

		val xOk = x > centreX - realWidth / 2 && x < centreX + realWidth / 2
		val yOk = y > centreY - realHeight / 2 && y < centreY + realHeight / 2

		return xOk && yOk
	}

	open fun draw(shader: Shader, dt: Float) {
		x += (targetX - x) * dt * 10
		y += (targetY - y) * dt * 10

		val mvp = Matrix().identity()
		updateBounds()

		mvp.mul(Matrix().translate(centreX, centreY, 0f))
		mvp.mul(Matrix().scale(realWidth, realHeight, 1f))
		shader.setMvp(mvp)

		if (tex != null) {
			setTex(shader, tex!!)
		}

		gl.glDrawElements(gl.GL_TRIANGLES, 6, gl.GL_UNSIGNED_BYTE, 0)
	}
}
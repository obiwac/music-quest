package com.p4.musicquest.ui

import com.p4.musicquest.Matrix
import com.p4.musicquest.Shader
import com.p4.musicquest.Texture
import com.p4.musicquest.UI
import com.p4.musicquest.UIRefCorner
import android.opengl.GLES30 as gl

open class Element(private val ui: UI, private val tex: Texture? = null, private val refCorner: UIRefCorner, private var x: Float, private var y: Float, private val width: Float, private val height: Float) {
	private var targetX = x
	private var targetY = y

	fun setTex(shader: Shader, tex: Texture) {
		gl.glActiveTexture(gl.GL_TEXTURE0)
		shader.setSampler(0)
		gl.glBindTexture(gl.GL_TEXTURE_2D, tex.tex)
	}

	open fun draw(shader: Shader, dt: Float) {
		x += (targetX - x) * dt * 10
		y += (targetY - y) * dt * 10

		val aspect = ui.xRes.toFloat() / ui.yRes
		val mvp = Matrix().identity()

		val realWidth = width * 2
		val realHeight = height * 2

		val realX = x * 2
		var realY = y * 2

		val offX = 1f - (realWidth / 2 + realX)
		val offY = 1f - (realHeight / 2 + realY) * aspect

		if (refCorner == UIRefCorner.TOP_LEFT) {
			mvp.mul(Matrix().translate(-offX, offY, 0f))
		}

		else if (refCorner == UIRefCorner.BOTTOM_LEFT) {
			mvp.mul(Matrix().translate(-offX, -offY, 0f))
		}

		else if (refCorner == UIRefCorner.TOP_RIGHT) {
			mvp.mul(Matrix().translate(offX, offY, 0f))
		}

		else if (refCorner == UIRefCorner.BOTTOM_RIGHT) {
			mvp.mul(Matrix().translate(offX, -offY, 0f))
		}

		mvp.mul(Matrix().scale(realWidth, realHeight * aspect, 1f))
		shader.setMvp(mvp)

		if (tex != null) {
			setTex(shader, tex)
		}

		gl.glDrawElements(gl.GL_TRIANGLES, 6, gl.GL_UNSIGNED_BYTE, 0)
	}
}
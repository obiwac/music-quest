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

		val mvp = Matrix().identity()
		mvp.mul(Matrix().scale(width / 2, height / 2, 1f))
		// mvp.mul(Matrix().translate(x, y, 0f))

		shader.setMvp(mvp)

		if (tex != null) {
			setTex(shader, tex)
		}

		gl.glDrawElements(gl.GL_TRIANGLES, 6, gl.GL_UNSIGNED_BYTE, 0)
	}
}
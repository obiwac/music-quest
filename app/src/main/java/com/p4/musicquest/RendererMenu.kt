package com.p4.musicquest

import android.content.Context
import android.opengl.GLES30
import com.p4.musicquest.entities.Monster
import com.p4.musicquest.entities.Player
import com.p4.musicquest.entities.Shoot
import com.p4.musicquest.entities.Villager
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.abs
import kotlin.math.sqrt

class RendererMenu(private val context: Context): Renderer(context) {
	private lateinit var game: Game
	private lateinit var shader: Shader

	private var prevTime: Long = 0

	private var rightMul: Float = 0f
	private var leftMul: Float = 0f
	private var topMul: Float = 0f
	private var bottomMul: Float = 0f

	private var targetRightMul: Float = 1f
	private var targetLeftMul: Float = 1f
	private var targetTopMul: Float = 1f
	private var targetBottomMul: Float = 0f

	override var dt = 0f

	override fun onSurfaceCreated(unused: GL10, config: EGLConfig?) {
		prevTime = System.currentTimeMillis()

		game = Game(context)
		shader = Shader(context, "shaders/vert.glsl", "shaders/frag.glsl")

		camera = Camera()
		uiMenu = UIMenu(context, game)
	}

	override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
		GLES30.glViewport(0, 0, width, height)

		camera.updateResolution(width, height)
		uiMenu.updateResolution(width, height)
	}

	override fun onDrawFrame(unused: GL10) {
		val curTime = System.currentTimeMillis()
		dt = (curTime - prevTime).toFloat() / 1000
		prevTime = curTime

		// world section animation

		rightMul += (targetRightMul - rightMul) * dt * 3
		leftMul += (targetLeftMul - leftMul) * dt * 3
		topMul += (targetTopMul - topMul) * dt * 3
		bottomMul += (targetBottomMul - bottomMul) * dt * 3

		// check if player should die

		/*
		if (dist > 1.5f) {
			if (x - abs(z) > 0 && rightMul > .9f) {
				player?.position = arrayOf(0f, 0f, -1f)
			}
			if (-x - abs(z) > 0 && leftMul > .9f) {
				player?.position = arrayOf(0f, 0f, -1f)
			}
			if (z - abs(x) > 0 && topMul > .9f) {
				player?.position = arrayOf(0f, 0f, -1f)
			}
			if (-z - abs(x) > 0 && bottomMul > .9f) {
				player?.position = arrayOf(0f, 0f, -1f)
			}
		}
		*/

		// rendering

		GLES30.glEnable(GLES30.GL_DEPTH_TEST)
		GLES30.glDisable(GLES30.GL_BLEND)

		shader.use()
		shader.setMvp(camera.mvp(0f, 0f, 0f))
		shader.setMultipliers(rightMul, leftMul, topMul, bottomMul)

		GLES30.glClearColor(0f, 1f, .5f, 1f)
		GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)

		// render UI

		uiMenu.draw(shader, dt)
	}

}
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

class RendererPlaying(private val context: Context): Renderer(context) {
	private lateinit var game: Game
	private lateinit var world: World
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
		world = World(context)
		shader = Shader(context, "shaders/vert.glsl", "shaders/frag.glsl")

		player = Player(context, world, arrayOf(0f, 0f, -1f))
		monster1 = Monster(context, world, arrayOf(1.9f, 0f, 1.2f), player)
		monster1?.let { world.listeMonstres.add(it) }
		villager1 = Villager(context, player, world, arrayOf(-2f, 0f, 4f))

		listShoot = ArrayList<Shoot>()
		for (i in 1..3) {
			listShoot.add(Shoot(context, player!!, world, arrayOf(999f, 0f, 999f))) // pour qu'on voit pas la fleche
		}

		camera = Camera()
		uiPlaying = UIPlaying(context, player!!)
	}

	override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
		GLES30.glViewport(0, 0, width, height)

		camera.updateResolution(width, height)
		uiPlaying.updateResolution(width, height)
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

		val (x, y, z) = player!!.position
		val dist = sqrt(x * x + z * z)

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

		// camera stuff

		player?.update(dt)
		monster1?.update(dt)
		villager1?.update(dt)
		camera.followPlayer(player!!, dt)

		for (shoot in listShoot) {
			shoot.update(dt)
		}

		// rendering

		GLES30.glEnable(GLES30.GL_DEPTH_TEST)
		GLES30.glDisable(GLES30.GL_BLEND)

		shader.use()
		shader.setMvp(camera.mvp(0f, 0f, 0f))
		shader.setMultipliers(rightMul, leftMul, topMul, bottomMul)

		GLES30.glClearColor(0f, 1f, .5f, 1f)
		GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)

		world.draw(shader)
		player?.draw(shader, camera)
		monster1?.draw(shader, camera)
		villager1?.draw(shader, camera)

		if(villager1 != null) {
			villager1!!.drawEntity(shader, camera)
		}

		for (shoot in listShoot) {
			shoot.draw(shader, camera)
		}

		// render UI

		uiPlaying.draw(shader, dt)
	}

}

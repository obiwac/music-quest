package com.p4.musicquest

import android.content.Context
import android.opengl.GLES30 as gl
import android.opengl.GLSurfaceView
import android.util.Log
import com.p4.musicquest.entities.Monster
import com.p4.musicquest.entities.Player
import com.p4.musicquest.entities.Shoot
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.abs
import kotlin.math.sqrt

class Renderer(private val context: Context) : GLSurfaceView.Renderer {
    private lateinit var world: World
    private lateinit var shader: Shader

    var player: Player? = null
    var monster1: Monster? = null
    lateinit var listShoot: ArrayList<Shoot>
    var numberShoot = 0

    lateinit var camera: Camera

    private var prevTime: Long = 0

    private var rightMul: Float = 0f
    private var leftMul: Float = 0f
    private var topMul: Float = 0f
    private var bottomMul: Float = 0f

    private var targetRightMul: Float = 1f
    private var targetLeftMul: Float = 1f
    private var targetTopMul: Float = 1f
    private var targetBottomMul: Float = 0f

    fun setRight() {
        targetRightMul = 0f
        targetLeftMul = 1f
        targetTopMul = 1f
        targetBottomMul = 1f
    }

    fun setLeft() {
        targetRightMul = 1f
        targetLeftMul = 0f
        targetTopMul = 1f
        targetBottomMul = 1f
    }

    fun setTop() {
        targetRightMul = 1f
        targetLeftMul = 1f
        targetTopMul = 0f
        targetBottomMul = 1f
    }

    fun setBottom() {
        targetRightMul = 1f
        targetLeftMul = 1f
        targetTopMul = 1f
        targetBottomMul = 0f
    }

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig?) {
        prevTime = System.currentTimeMillis()

        world = World(context)
        shader = Shader(context, "shaders/vert.glsl", "shaders/frag.glsl")

        player = Player(context, world, arrayOf(0f, 0f, -1f))
        monster1 = Monster(context, world, arrayOf(1.9f, 0f, 1.2f), player)
        monster1?.let { world.listeMonstres.add(it) }

        listShoot = ArrayList<Shoot>()
        for (i in 1..3) {
            listShoot.add(Shoot(context, player!!, world, arrayOf(999f, 0f, 999f))) // pour qu'on voit pas la fleche
        }

        camera = Camera()

        gl.glEnable(gl.GL_DEPTH_TEST)
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        gl.glViewport(0, 0, width, height)
        camera.updateResolution(width, height)
    }

    override fun onDrawFrame(unused: GL10) {
        val curTime = System.currentTimeMillis()
        val dt = (curTime - prevTime).toFloat() / 1000
        prevTime = curTime

        // world section animation

        rightMul += (targetRightMul - rightMul) * dt * 3
        leftMul += (targetLeftMul - leftMul) * dt * 3
        topMul += (targetTopMul - topMul) * dt * 3
        bottomMul += (targetBottomMul - bottomMul) * dt * 3

        // check if player should die

        val (x, y, z) = player!!.position
        val dist = sqrt(x * x + z * z)

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

        // camera stuff

        player?.update(dt)
        monster1?.update(dt)
        camera.followPlayer(player!!, dt)

        for (shoot in listShoot) {
            shoot.update(dt)
        }

        // rendering

        shader.use()
        shader.setMvp(camera.mvp(0f, 0f, 0f))
        shader.setMultipliers(rightMul, leftMul, topMul, bottomMul)

        gl.glClearColor(0f, 0f, 0f, 1f)
        gl.glClear(gl.GL_COLOR_BUFFER_BIT or gl.GL_DEPTH_BUFFER_BIT)

        world.draw(shader)

        player?.draw(shader, camera)

        monster1?.draw(shader, camera)

        for (shoot in listShoot) {
            shoot.draw(shader, camera)
        }
    }

    fun shoot() {
        if (player != null) {
            println("direction 1 :" + player!!.direction[0])
            println("direction 2 :" + player!!.direction[2])
            if (abs(player!!.velocity[0]) >= 0.005f && abs(player!!.velocity[2]) >= 0.005f) {
                // player moves
                listShoot[numberShoot].directionPlayer[0] = player!!.direction[0]
                listShoot[numberShoot].directionPlayer[2] = player!!.direction[2]
            }

            listShoot[numberShoot].position[0] = player!!.position[0]
            listShoot[numberShoot].position[2] = player!!.position[2]

        }
        numberShoot = (numberShoot + 1) % 3
    }
}
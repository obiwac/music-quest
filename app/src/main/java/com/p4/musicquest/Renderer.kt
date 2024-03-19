package com.p4.musicquest

import android.content.Context
import android.opengl.GLES30 as gl
import android.opengl.GLSurfaceView
import android.util.Log
import com.p4.musicquest.entities.Player
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class Renderer(private val context: Context) : GLSurfaceView.Renderer {
    private lateinit var world: World
    private lateinit var shader: Shader

    var player: Player? = null
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

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig?) {
        prevTime = System.currentTimeMillis()

        world = World(context)
        shader = Shader(context, "shaders/vert.glsl", "shaders/frag.glsl")

        player = Player(context, world, arrayOf(0f, 0f, -1f))
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

        rightMul += (targetRightMul - rightMul) * dt * 1
        leftMul += (targetLeftMul - leftMul) * dt * 1
        topMul += (targetTopMul - topMul) * dt * 1
        bottomMul += (targetBottomMul - bottomMul) * dt * 1

        // camera stuff

        player?.update(dt)
        camera.followPlayer(player!!, dt)

        // rendering

        shader.use()
        shader.setMvp(camera.mvp(0f, 0f, 0f))
        shader.setMultipliers(rightMul, leftMul, topMul, bottomMul)

        gl.glClearColor(0f, 0f, 0f, 1f)
        gl.glClear(gl.GL_COLOR_BUFFER_BIT or gl.GL_DEPTH_BUFFER_BIT)

        world.draw(shader)

        player?.draw(shader, camera)
    }
}
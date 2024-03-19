package com.p4.musicquest

import android.content.Context
import android.opengl.GLES30 as gl
import android.opengl.GLSurfaceView
import android.util.Log
import com.p4.musicquest.entities.Monster
import com.p4.musicquest.entities.Player
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class Renderer(private val context: Context) : GLSurfaceView.Renderer {
    private lateinit var world: World
    private lateinit var shader: Shader

    var player: Player? = null
    var monster1: Monster? = null
    lateinit var camera: Camera

    private var prevTime: Long = 0

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig?) {
        prevTime = System.currentTimeMillis()

        world = World(context)
        shader = Shader(context, "shaders/vert.glsl", "shaders/frag.glsl")

        player = Player(context, world, arrayOf(0f, 0f, 0f))
        monster1 = Monster(context, world, arrayOf(1.9f, 0f, 1.2f))
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

        player?.update(dt)
        monster1?.update(dt)
        camera.followPlayer(player!!, dt)

        shader.use()
        shader.setMvp(camera.mvp(0f, 0f, 0f))

        gl.glClearColor(0f, 0f, 0f, 1f)
        gl.glClear(gl.GL_COLOR_BUFFER_BIT or gl.GL_DEPTH_BUFFER_BIT)

        world.draw(shader)

        player?.draw(shader, camera)

        monster1?.draw(shader, camera)
    }
}
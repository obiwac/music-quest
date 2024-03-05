package com.p4.musicquest

import android.content.Context
import android.opengl.GLES30 as gl
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class Renderer(private val context: Context) : GLSurfaceView.Renderer {
    private lateinit var world: World
    private lateinit var shader: Shader
    private lateinit var teapot: Model
    val camera = Camera()

    private var prevTime: Long = 0

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig?) {
        prevTime = System.currentTimeMillis()

        world = World(context)
        shader = Shader(context, "shaders/vert.glsl", "shaders/frag.glsl")
        teapot = Model(context, "teapot.obj")

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

        camera.update(dt)

        shader.use()
        shader.setMvp(camera.mvp())

        gl.glClearColor(0f, 0f, 0f, 1f)
        gl.glClear(gl.GL_COLOR_BUFFER_BIT or gl.GL_DEPTH_BUFFER_BIT)

        world.draw()
    }
}
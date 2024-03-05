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
    lateinit var camera: Camera

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig?) {
        world = World()
        shader = Shader(context, "shaders/vert.glsl", "shaders/frag.glsl")
        camera = Camera()
        teapot = Model(context, "teapot.obj")

        gl.glEnable(gl.GL_DEPTH_TEST)
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        gl.glViewport(0, 0, width, height)
        camera.updateResolution(width, height)
    }

    override fun onDrawFrame(unused: GL10) {
        shader.use()
        shader.setMvp(camera.mvp())

        gl.glClearColor(0f, 0f, 0f, 1f)
        gl.glClear(gl.GL_COLOR_BUFFER_BIT or gl.GL_DEPTH_BUFFER_BIT)

        teapot.draw()
    }
}
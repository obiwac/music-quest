package com.p4.musicquest

import android.content.Context
import java.nio.FloatBuffer
import android.opengl.GLES30 as gl
import java.nio.IntBuffer

class World(context: Context) {
    private lateinit var model: Model

    init {
        model = Model(context, "world.obj", "textures/texture.png")
    }

    fun draw() {
        model.draw()
    }
}
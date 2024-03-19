package com.p4.musicquest

import android.content.Context
import java.nio.FloatBuffer
import android.opengl.GLES30 as gl
import java.nio.IntBuffer

class World(context: Context) {
    private var model: Model
    private var jukebox: Model
    private var house: Model

    var colliders: Array<Collider>

    init {
        model = Model(context, "world.obj", "textures/texture.png")
        jukebox = Model(context, "jukebox.obj", "textures/jukebox.png", scale = .4f)
        house = Model(context, "house.obj", "textures/house.png", scale = 8f, offX = 3f, offY = 0f, offZ = 0f)

        colliders = arrayOf(jukebox.collider, house.collider)
    }

    fun draw(shader: Shader) {
        model.draw(shader)
        jukebox.draw(shader)
        house.draw(shader)
    }
}
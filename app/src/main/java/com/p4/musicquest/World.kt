package com.p4.musicquest

import android.content.Context
import com.p4.musicquest.entities.Monster
import com.p4.musicquest.entities.Shoot
import java.nio.FloatBuffer
import android.opengl.GLES30 as gl
import java.nio.IntBuffer

class World(context: Context) {
    private var model: Model

    public val listeMonstres = mutableListOf<Monster>()
    val listShoot = ArrayList<Shoot>()

    var colliders: Array<Collider>

    init {
        model = Model(context, "world.obj", "textures/texture.png", scale = .2f)
        colliders = arrayOf()
    }

    fun draw(shader: Shader) {
        model.draw(shader)
    }
}
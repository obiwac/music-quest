package com.p4.musicquest

import com.p4.musicquest.entities.Player
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class Camera(private var width: Int = 1, private var height: Int = 1) {
    private val mv = Matrix() // model view
    private val p = Matrix() // perspective

    private var time = 0f
    val position = arrayOf(0f, 0f) // camera coordinates

    fun updateResolution(width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    fun followPlayer(player: Player, dt: Float) {
        position[0] += (player.position[0] - position[0]) * dt * 15
        position[1] += (player.position[2] - position[1]) * dt * 15
    }

    fun mvp(x: Float, y: Float, z: Float, tilt: Boolean = true): Matrix {
        // perspective matrix

        p.perspective((PI / 2).toFloat(), width.toFloat() / height.toFloat(), .1f, 500f)

        // model-view matrix

        mv.identity()
        mv.mul(Matrix().translate(0f, 0f, -2f))

        time += 0.016f
        // val tiltAngle = sin(time) * sin(time) * PI.toFloat() / 6
        val tiltAngle = PI.toFloat() / 6

        mv.mul(Matrix().rotate2d(0f, tiltAngle))
        mv.mul(Matrix().translate(-position[0] + x, -position[1] + z, y))

        if (!tilt) {
            mv.mul(Matrix().rotate2d(0f, -tiltAngle))
        }

        // model-view-projection matrix

        return Matrix(p).mul(mv)
    }

}
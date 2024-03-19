package com.p4.musicquest

import kotlin.math.PI

class Camera(private var width: Int = 1, private var height: Int = 1) {
    private val mv = Matrix() // model view
    private val p = Matrix() // perspective

    val position = floatArrayOf(0f, 0f) // camera coordinates
    val strafe = floatArrayOf(0f, 0f)

    fun updateResolution(width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    fun update(dt: Float) {
        position[0] += strafe[0] * dt
        position[1] += strafe[1] * dt
    }

    fun mvp(pos1: Float, pos2: Float): Matrix {
        // perspective matrix

        p.perspective((PI / 2).toFloat(), width.toFloat() / height.toFloat(), .1f, 500f)

        // model-view matrix

        mv.identity()

        mv.mul(Matrix().translate(0f, 0f, -1f))
        mv.mul(Matrix().rotate2d(0f, PI.toFloat() / 6))
        mv.mul(Matrix().translate(-position[0] + pos1, -position[1] + pos2, 0f))

        // model-view-projection matrix

        return Matrix(p).mul(mv)
    }

}
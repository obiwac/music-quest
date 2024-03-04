package com.p4.musicquest

import kotlin.math.PI

class Camera(private var width: Int = 1, private var height: Int = 1) {
    val mv = Matrix() // model view
    val p = Matrix() // perspective

    val position = floatArrayOf(0f, 0f) // coordonnee de la camera
    //var x = 0f

    fun updateResolution(width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    fun mvp(): Matrix {
        // perspective matrix

        p.perspective((PI / 2).toFloat(), width.toFloat() / height.toFloat(), .1f, 500f)

        // model-view matrix

        mv.identity()

        mv.mul(Matrix().translate(position[0], position[1], -1f))
        //mv.mul(Matrix().rotate2d(x + 6.28f / 4, sin(x / 3 * 2) / 2))
        //x += .01f

        // model-view-projection matrix

        return Matrix(p).mul(mv)
    }

    fun moveLeftCamera() {
        position[0] += 0.1f
    }

    fun moveRightCamera() {
        position[0] -= 0.1f
    }

    fun moveUpCamera() {
        position[1] -= 0.1f
    }

    fun moveDownCamera() {
        position[1] += 0.1f
    }

}
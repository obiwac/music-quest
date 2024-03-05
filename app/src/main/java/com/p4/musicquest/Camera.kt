package com.p4.musicquest

import kotlin.math.PI
import kotlin.math.sin

class Camera(private var width: Int = 1, private var height: Int = 1) {
    val mv = Matrix()
    val p = Matrix()

    val position = floatArrayOf(0f, 0f, 0f)
    var x = 0f

    fun updateResolution(width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    fun mvp(): Matrix {
        // perspective matrix

        p.perspective((PI / 2).toFloat(), width.toFloat() / height.toFloat(), .1f, 500f)

        // model-view matrix

        mv.identity()

        mv.mul(Matrix().translate(0f, -1f, -7f))
        mv.mul(Matrix().rotate2d(x + 6.28f / 4, sin(x / 3 * 2) / 2))
        x += .01f

        // model-view-projection matrix

        return Matrix(p).mul(mv)
    }
}
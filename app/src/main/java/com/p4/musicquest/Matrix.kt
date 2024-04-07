package com.p4.musicquest

import java.nio.FloatBuffer
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

class Matrix(mat: Matrix? = null) {
    private val data: FloatBuffer = FloatBuffer.allocate(4 * 4)

    private fun index(i: Int, j: Int): Int {
        return i * 4 + j
    }

    fun getBuf(): FloatBuffer {
        data.rewind()
        return data
    }

    fun identity(): Matrix {
        for (i in 0..3) {
            for (j in 0..3) {
                data.put(index(i, j), if (i == j) 1f else 0f)
            }
        }

        data.rewind()
        return this
    }

    init {
        if (mat == null) {
            identity()
        }

        else {
            data.put(mat.data)
            data.rewind()
        }
    }

    fun mul(y: Matrix): Matrix {
        val x = Matrix(this)

        for (i in 0..3) {
            for (j in 0..3) {
                data.put(index(i, j),
                    x.data.get(index(0, j)) * y.data.get(index(i, 0)) +
                    x.data.get(index(1, j)) * y.data.get(index(i, 1)) +
                    x.data.get(index(2, j)) * y.data.get(index(i, 2)) +
                    x.data.get(index(3, j)) * y.data.get(index(i, 3))
                )
            }
        }

        data.rewind()
        return this
    }

    fun translate(x: Float, y: Float, z: Float): Matrix {
        identity()

        for (i in 0..3) {
            var acc = 0f

            acc += x * data.get(index(0, i))
            acc += y * data.get(index(1, i))
            acc += z * data.get(index(2, i))

            data.put(index(3, i), data.get(index(3, i)) + acc)
        }

        data.rewind()
        return this
    }

    fun scale(x: Float, y: Float, z: Float): Matrix {
        identity()

        data.put(index(0, 0), x)
        data.put(index(1, 1), y)
        data.put(index(2, 2), z)

        data.rewind()
        return this
    }

    fun rotate(angle: Float, x_: Float, y_: Float, z_: Float): Matrix {
        identity()

        // XXX Kotlin workaround

        var x = x_
        var y = y_
        var z = z_

        val mag = sqrt(x * x + y * y + z * z)

        x /= -mag
        y /= -mag
        z /= -mag

        val sinAngle = sin(angle)
        val cosAngle = cos(angle)
        val oneMinusCos = 1f - cosAngle

        val xx = x * x
        val yy = y * y
        val zz = z * z

        val xy = x * y
        val yz = y * z
        val zx = z * x

        val xs = x * sinAngle
        val ys = y * sinAngle
        val zs = z * sinAngle

        identity()

        data.put(index(0, 0), (oneMinusCos * xx) + cosAngle)
        data.put(index(0, 1), (oneMinusCos * xy) - zs)
        data.put(index(0, 2), (oneMinusCos * zx) + ys)

        data.put(index(1, 0), (oneMinusCos * xy) + zs)
        data.put(index(1, 1), (oneMinusCos * yy) + cosAngle)
        data.put(index(1, 2), (oneMinusCos * yz) - xs)

        data.put(index(2, 0), (oneMinusCos * zx) - ys)
        data.put(index(2, 1), (oneMinusCos * yz) + xs)
        data.put(index(2, 2), (oneMinusCos * zz) + cosAngle)

        data.put(index(3, 3), 1f)

        data.rewind()
        return this
    }

    fun rotate2d(x: Float, y: Float): Matrix {
        rotate(x, 0f, 1f, 0f) // already calls loadIdentity
        mul(Matrix().rotate(-y, cos(x), 0f, sin(x)))

        return this
    }

    private fun frustum(left: Float, right: Float, bottom: Float, top: Float, near: Float, far: Float): Matrix {
        val deltaX = right - left
        val deltaY = top - bottom
        val deltaZ = far - near

        identity()

        data.put(index(0, 0), 2 * near / deltaX)
        data.put(index(1, 1), 2 * near / deltaY)

        data.put(index(2, 0), (right + left) / deltaX)
        data.put(index(2, 1), (top + bottom) / deltaY)
        data.put(index(2, 2), -(near + far)  / deltaZ)

        data.put(index(2, 3), -1f)
        data.put(index(3, 2), -2 * near * far / deltaZ)

        data.rewind()
        return this
    }

    fun perspective(fovY: Float, aspect: Float, near: Float, far: Float): Matrix {
        val frustumY = tan(fovY / 2)
        val frustumX = frustumY * aspect

        frustum(-frustumX * near, frustumX * near, -frustumY * near, frustumY * near, near, far) // already calls loadIdentity

        return this
    }
}

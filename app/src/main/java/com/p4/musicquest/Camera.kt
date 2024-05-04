package com.p4.musicquest

import com.p4.musicquest.entities.Player
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class Camera(private val ui: UI, private var width: Int = 1, private var height: Int = 1) {
    private val mv = Matrix() // model view
    private val p = Matrix() // perspective

    private var time = 0f
    val position = arrayOf(0f, 0f, 0f)
    val targetPosition = arrayOf(0f, 0f, 0f)
    var tiltAngle = 0f
    var targetTiltAngle = PI.toFloat() / 6

    fun updateResolution(width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    fun update(world: World, dt: Float) {
        val player = world.player!!

        // Set targets.

        when (ui.uiState) {
            UI.UIState.PLAYING -> {
                targetTiltAngle = PI.toFloat() / 6
                targetPosition[0] = player.position[0]
                targetPosition[1] = player.position[2]
                targetPosition[2] = -2f
            }

            UI.UIState.DIALOG, UI.UIState.SHOP, UI.UIState.INVENTORY -> {
                targetTiltAngle = PI.toFloat() / 4
                targetPosition[0] = player.position[0]
                targetPosition[1] = player.position[2]
                targetPosition[2] = -1f
            }

            UI.UIState.GUIDE -> {
                targetTiltAngle = 0f
                targetPosition[0] = player.position[0]
                targetPosition[1] = player.position[2]
                targetPosition[2] = -5f
            }

            UI.UIState.ENDING -> {
                targetTiltAngle = 0f
                targetPosition[0] = 1f
                targetPosition[1] = 0f
                targetPosition[2] = -80f
            }

            else -> {
                targetTiltAngle = 0f
                targetPosition[0] = 0f
                targetPosition[1] = 0f
                targetPosition[2] = -50f
            }
        }

        // Animate.

        var positionSpeed = 15f
        var heightSpeed = 4f
        var tiltSpeed = 3f

        if (ui.uiState == UI.UIState.ENDING) {
            positionSpeed = .6f
            heightSpeed = .2f
            tiltSpeed = .3f

            if (position[2] < -40f) {
                world.state = World.WorldState.WORLD_UNGREYED
            }
        }

        position[0] += (targetPosition[0] - position[0]) * dt * positionSpeed
        position[1] += (targetPosition[1] - position[1]) * dt * positionSpeed

        position[2] += (targetPosition[2] - position[2]) * dt * heightSpeed
        tiltAngle += (targetTiltAngle - tiltAngle) * dt * tiltSpeed
    }

    fun mvp(x: Float, y: Float, z: Float, tilt: Boolean = true): Matrix {
        // perspective matrix

        p.perspective((PI / 2).toFloat(), width.toFloat() / height.toFloat(), .1f, 500f)

        // model-view matrix

        mv.identity()
        mv.mul(Matrix().translate(0f, 0f, position[2]))

        time += 0.016f

        mv.mul(Matrix().rotate2d(0f, tiltAngle))
        mv.mul(Matrix().translate(-position[0] + x, -position[1] + z, y))

        if (!tilt) {
            mv.mul(Matrix().rotate2d(0f, -tiltAngle))
        }

        // model-view-projection matrix

        return Matrix(p).mul(mv)
    }

}
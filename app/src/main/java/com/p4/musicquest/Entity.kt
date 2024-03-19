package com.p4.musicquest

import android.util.Log
import kotlin.math.abs

open class Entity(private val sprite: Sprite, var position: Array<Float>, private var width: Float, private var height: Float) {
	companion object {
		private val GRAVITY_ACCEL = arrayOf(0f, -32f, 0f)
		private val FRICTION = arrayOf(20f, 20f, 20f)
		private val DRAG_JUMP = arrayOf(1.8f, 0f, 1.8f)
		private val DRAG_FALL = arrayOf(1.8f, .4f, 1.8f)
	}

	private val collider = Collider()
	private var grounded = false
	private var velocity = arrayOf(0f, 0f, 0f)
	protected var accel = arrayOf(0f, 0f, 0f)

	private fun updateCollider() {
		val (x, y, z) = position

		collider.x1 = x - width / 2
		collider.x2 = x + width / 2

		collider.y1 = y
		collider.y2 = y + height

		collider.z1 = z - width / 2
		collider.z2 = z + width / 2
	}

	private fun computeFriction(): Array<Float> {
		if (grounded) {
			return FRICTION;
		}

		else if (velocity[1] > 0f) {
			return DRAG_JUMP;
		}

		return DRAG_FALL;
	}

	private fun absMin(x: Float, y: Float): Float {
		if (abs(x) < abs(y)) {
			return x;
		}

		return y;
	}

	open fun update(dt: Float) {
		val (fx, fy, fz) = computeFriction()

		// input acceleration + friction compensation

		velocity[0] += accel[0] * fx * dt
		velocity[1] += accel[1] * fy * dt
		velocity[2] += accel[2] * fz * dt

		// reset acceleration for next frame

		accel[0] = 0f
		accel[1] = 0f
		accel[2] = 0f

		// collisions

		updateCollider()
		grounded = false

		// collide with colliders

		repeat(3) {
			val vx = velocity[0] * dt
			val vy = velocity[1] * dt
			val vz = velocity[2] * dt

			// TODO actually collide with colliders
		}

		// update position

		position[0] += velocity[0] * dt
		position[1] += velocity[1] * dt
		position[2] += velocity[2] * dt

		// collide with ground

		if (position[1] < 0f) {
			grounded = true
			position[1] = 0f
			velocity[1] = 0f
		}

		// gravity

		velocity[0] += GRAVITY_ACCEL[0] * dt
		velocity[1] += GRAVITY_ACCEL[1] * dt
		velocity[2] += GRAVITY_ACCEL[2] * dt

		// friction

		velocity[0] -= absMin(velocity[0] * fx * dt, velocity[0])
		velocity[1] -= absMin(velocity[1] * fy * dt, velocity[1])
		velocity[2] -= absMin(velocity[2] * fz * dt, velocity[2])

		updateCollider()
	}

	fun draw(shader: Shader, camera: Camera) {
		sprite.draw(shader, camera, position[0], position[1], position[2])
	}
}

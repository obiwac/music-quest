package com.p4.musicquest

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

open class Entity(protected val world: World, private val animator: Animator, var position: Array<Float>, private var width: Float, private var height: Float) {
	companion object {
		private val GRAVITY_ACCEL = arrayOf(0f, -32f, 0f)
		private val FRICTION = arrayOf(20f, 20f, 20f)
		private val DRAG_JUMP = arrayOf(1.8f, 0f, 1.8f)
		private val DRAG_FALL = arrayOf(1.8f, .4f, 1.8f)
	}

	val shadow = Shadow.getShadow(world.context, .2f)
	val collider = Collider()
	val hurtBox = Collider()
	private var grounded = false
	var velocity = arrayOf(0f, 0f, 0f)
	protected var accel = arrayOf(0f, 0f, 0f)
	var direction = arrayOf(0f, 0f, -1f)
	var directionToPlayer = arrayOf(0f, 0f, 1f)

	var entityState = EntityState(this)

	var vulnerable = true

	// for the animator

	var entityLife = true

	enum class TYPE_ENTITY {
		BASE_SPRITE,
		ITEM,
		SLIME_BOSS
	}

	var typeEntity = TYPE_ENTITY.BASE_SPRITE

	// statistic of entity
	var health = 20f
	var damage = 5
	var knockback = 0f

	var isHit = false
	var isAttack = false

	protected fun updateCollider() {
		val (x, y, z) = position

		collider.x1 = x - width / 2
		collider.x2 = x + width / 2

		collider.y1 = y
		collider.y2 = y + height

		collider.z1 = z - width / 2
		collider.z2 = z + width / 2

		// hurt box

		val hx = position[0] + direction[0] * 0.5f
		val hy = position[1] + direction[1] * 0.5f
		val hz = position[2] + direction[2] * 0.5f

		hurtBox.x1 = hx - width / 1.1f
		hurtBox.x2 = hx + width / 1.1f

		hurtBox.y1 = hy
		hurtBox.y2 = hy + height

		hurtBox.z1 = hz - width / 1.1f
		hurtBox.z2 = hz + width / 1.1f

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

	private fun distanceEuclidienne(pos1: Array<Float>, pos2: Array<Float>): Float {
		return sqrt((pos1[0] - pos2[0]).pow(2) + (pos1[1] - pos2[1]).pow(2))
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

		for (repeat in 0..<3) {
			// adjusted velocity

			val vx = velocity[0] * dt
			val vy = velocity[1] * dt
			val vz = velocity[2] * dt

			val candidates = ArrayList<Collider.Collision>()
			val candidateTimes = ArrayList<Float>()

			for (otherCollider in world.colliders) {
				val collision = collider.collide(otherCollider, vx, vy, vz)

				candidates.add(collision)
				candidateTimes.add(collision.entryTime)
			}

			// get first collision

			var earliestCollision: Collider.Collision? = null
			var earliestTime = 1f

			for (i in 0..<candidates.size) {
				val time = candidateTimes[i]

				if (time > earliestTime) {
					continue
				}

				earliestTime = time
				earliestCollision = candidates[i]
			}

			if (earliestCollision == null) {
				break;
			}

			earliestTime -= .001f

			if ((earliestCollision.normal?.get(0) ?: 0f) != 0f) {
				velocity[0] = 0f
				position[0] += vx * earliestTime
			}

			if ((earliestCollision.normal?.get(1) ?: 0f) != 0f) {
				velocity[1] = 0f
				position[1] += vy * earliestTime
			}

			if ((earliestCollision.normal?.get(2) ?: 0f) != 0f) {
				velocity[2] = 0f
				position[2] += vz * earliestTime
			}
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

		// Update direction
		if (velocity[0] != 0f || velocity[2] != 0f) {
			// Normalize velocity to get direction (unit vector of velocity)
			val distance = distanceEuclidienne(arrayOf(0f,0f), arrayOf(velocity[0], velocity[2]))
			direction[0] = velocity[0] / distance
			direction[2] = velocity[2] / distance
		}

		updateCollider()

		entityState.update()
	}

	fun draw(shader: Shader, camera: Camera) {
		shadow.draw(shader, camera, position[0], position[1], position[2])
		animator.draw(shader, camera, position[0], position[1], position[2], this)
	}

	fun receiveKnockback(directionDamage: Array<Float>, knockbackModifier: Float) {
		velocity[0] = directionDamage[0] * knockbackModifier
		velocity[2] = directionDamage[2] * knockbackModifier

	}
}

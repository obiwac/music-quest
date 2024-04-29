package com.p4.musicquest.entities

import android.content.Context
import com.p4.musicquest.Animator
import com.p4.musicquest.Entity
import com.p4.musicquest.Renderer
import com.p4.musicquest.SpriteSheet
import com.p4.musicquest.World
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random.Default.nextFloat

class CandyBoss(context: Context, world: World, pos: Array<Float>, var player: Player?, val renderer: Renderer) : Entity(
	world, Animator(SpriteSheet(context).getSpriteList("textures/Undead.png")), pos, .2f, .5f
) {

	init {
		health = 100f
		isHit = false
		damage = 2
		knockback = 15f
		entityLife = true
	}

	enum class PHASE {
		SURROUND,
		ATTACK
	}

	var stateCandyBoss = PHASE.SURROUND
	val speed = 25f

	override fun update(dt: Float) {
		// Update velocity of the enemy so that the velocity is in the direction of the player
		if (player != null) {

			// AI's monster to follow the player
			val distanceToPlayerX = player!!.position[0] - this.position[0]
			val distanceToPlayerY = player!!.position[2] - this.position[2]

			// calculate distance between enemy to player
			val distanceToPlayer = sqrt((distanceToPlayerX).pow(2) + (distanceToPlayerY).pow(2))

			// calculate direction from enemy to player
			if (distanceToPlayer > 0.1 && distanceToPlayer < 2.5) {
				val directionX: Float = distanceToPlayerX / distanceToPlayer
				val directionY: Float = distanceToPlayerY / distanceToPlayer

				directionToPlayer[0] = directionX
				directionToPlayer[2] = directionY

				// Advanced AI

				when(stateCandyBoss) {
					PHASE.ATTACK -> {
						val desiredVelocity = arrayOf(directionX * speed, 0f, directionY * speed)
						val steering = arrayOf(0f, 0f, 0f)
						steering[0] = ((desiredVelocity[0] - velocity[0]) * dt * 2.5).toFloat()
						steering[2] = ((desiredVelocity[2] - velocity[2]) * dt * 2.5).toFloat()

						accel[0] = steering[0]
						accel[2] = steering[2]
					}

					PHASE.SURROUND -> {
						// Circle position
						val radius = 40f // distance from center to circumference of circle

						var angle = nextFloat() * PI * 2

						position[0] = (player!!.position[0] + cos(angle) * radius).toFloat()
						position[2] = (player!!.position[2] + cos(angle) * radius).toFloat()
					}
				}

			} else {
				accel[0] = 0f
				accel[2] = 0f
			}

			// PVE
			var hit = collider.intersection(player!!.collider)
			//var hit = false

			if (hit) {
				player!!.getHit(this)
			}

			// drop item when he is dead

			val chanceDrop = (0..0).random()

			if (health <= 0 && chanceDrop == 0) {
				world.dropCoin(position.clone())
			}
		}

		super.update(dt)
	}

	fun getHit() {

		// Monster is hit by player

		isHit = true

		// Pansement

		if (health <= 0) {
			return
		}

		health -=  player!!.damage

		receiveKnockback(player!!.direction, knockback)

	}
}
package com.p4.musicquest.entities

import android.content.Context
import com.p4.musicquest.Animator
import com.p4.musicquest.Entity
import com.p4.musicquest.Renderer
import com.p4.musicquest.SpriteSheet
import com.p4.musicquest.World
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random.Default.nextFloat

class CandyBoss(context: Context, world: World, pos: Array<Float>, var player: Player?, val renderer: Renderer) : Entity(
	world, Animator(SpriteSheet(context).getSpriteList("textures/AntMan.png")), pos, .2f, .5f
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

	var stateCandyBoss = PHASE.ATTACK
	var angleSpeed = 0f
	var lastState = System.currentTimeMillis()
	var lastShoot = System.currentTimeMillis()

	override fun update(dt: Float) {
		// Update velocity of the enemy so that the velocity is in the direction of the player
		if (player != null) {

			// AI's monster to follow the player
			val distanceToPlayerX = player!!.position[0] - this.position[0]
			val distanceToPlayerY = player!!.position[2] - this.position[2]

			// calculate distance between enemy to player
			val distanceToPlayer = sqrt((distanceToPlayerX).pow(2) + (distanceToPlayerY).pow(2))

			// calculate direction from enemy to player
			val directionX: Float = distanceToPlayerX / distanceToPlayer
			val directionY: Float = distanceToPlayerY / distanceToPlayer

			directionToPlayer[0] = directionX
			directionToPlayer[2] = directionY

			// Advanced AI

			if (distanceToPlayer > 0.1 && distanceToPlayer <= 4.5f) {
				if (System.currentTimeMillis() - lastState >= 2000) {
					lastState = System.currentTimeMillis()

					if (stateCandyBoss == PHASE.ATTACK) {
						stateCandyBoss = PHASE.SURROUND
						lastShoot = System.currentTimeMillis()
					} else {
						stateCandyBoss = PHASE.ATTACK
					}
				}

				when(stateCandyBoss) {
					PHASE.ATTACK -> {
						accel[0] = directionX * 1.5f
						accel[2] = directionY * 1.5f
					}

					PHASE.SURROUND -> {

						if (System.currentTimeMillis() - lastShoot >= 700) {
							lastShoot = System.currentTimeMillis()
							world.shoot(this)
						}

						// Get circle position

						val radius = 1.7f // distance from center to circumference of circle

						angleSpeed += 0.006f
						var angle = angleSpeed * PI * 2

						position[0] = (player!!.position[0] + cos(angle) * radius).toFloat()
						position[2] = (player!!.position[2] + sin(angle) * radius).toFloat()
						accel[0] = directionX
						accel[2] = directionY
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

			//TODO("final disk")
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
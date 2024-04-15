package com.p4.musicquest.entities

import android.content.Context
import com.p4.musicquest.Animator
import com.p4.musicquest.Entity
import com.p4.musicquest.Sprite
import com.p4.musicquest.SpriteSheet
import com.p4.musicquest.World
import kotlin.math.pow
import kotlin.math.sqrt

class Monster (context: Context, world: World, pos: Array<Float>, var player: Player?) : Entity(
	world, Animator(SpriteSheet(context).getSpriteList("textures/Undead.png")), pos, .2f, .5f
) {

	init {
		health = 20
		isHit = false
		damage = 2
		knockback = 15f
		entityLife = true

	}

	val x_initial = position[0]
	val y_initial = position[1]
	val z_initial = position[2]
	val startHealth = health
	var directionToPlayer = arrayOf(0f, 0f, 1f)

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

				accel[0] = directionX
				accel[2] = directionY
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
		}

		super.update(dt)
	}

	fun getHit() {

		// Monster is hit by player

		isHit = true
		println("health : $health")
		if (isDead(this, player!!.damage)) {
			position[0] = x_initial
			position[1] = y_initial
			position[2] = z_initial

			health = 0
		}

		receiveKnockback(player!!.direction, knockback)
	}

}
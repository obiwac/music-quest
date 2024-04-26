package com.p4.musicquest.entities

import android.content.Context
import com.p4.musicquest.Animator
import com.p4.musicquest.Entity
import com.p4.musicquest.EntityState
import com.p4.musicquest.SpriteSheet
import com.p4.musicquest.World
import kotlin.math.pow
import kotlin.math.sqrt

class SlimeBoss (val context: Context, world: World, val pos: Array<Float>, var player: Player?) : Entity(
	world, Animator(SpriteSheet(context).getSlimeBoss("textures/slime_run_spritesheeet.png")), pos, .2f, .5f){

	init {
		health = 100
		isHit = false
		damage = 5
		knockback = 15f
		entityLife = false
		typeEntity = TYPE_ENTITY.SLIME_BOSS
	}

	var stateBoss = arrayOf(EntityState.StateSlimeBoss.NOT_MOVING, EntityState.StateSlimeBoss.CHARGE, EntityState.StateSlimeBoss.DASH)
	var indexState = 0

	var timeNextState = System.currentTimeMillis()

    var timeout = 1000


	var directionX = 0f
	var directionY = 0f
	override fun update (dt: Float) {
		// Update velocity of the enemy so that the velocity is in the direction of the player
		if (player != null) {

			if (System.currentTimeMillis() - timeNextState >= timeout) {
				indexState = (indexState + 1) % 3
				timeNextState = System.currentTimeMillis()
				directionToPlayer[0] = directionX
				directionToPlayer[2] = directionY
			}

			when(stateBoss[indexState]) {
				EntityState.StateSlimeBoss.NOT_MOVING -> {
					vulnerable = true
					accel[0] = 0f
					accel[2] = 0f
					timeout = 2000
				}

				EntityState.StateSlimeBoss.CHARGE -> {
					vulnerable = false
					accel[0] = 0f
					accel[2] = 0f

					// AI's monster to follow the player
					val distanceToPlayerX = player!!.position[0] - this.position[0]
					val distanceToPlayerY = player!!.position[2] - this.position[2]

					// calculate distance between enemy to player
					val distanceToPlayer = sqrt((distanceToPlayerX).pow(2) + (distanceToPlayerY).pow(2))

					directionX = distanceToPlayerX / distanceToPlayer
					directionY = distanceToPlayerY / distanceToPlayer

					directionToPlayer[0] = directionX
					directionToPlayer[2] = directionY

					timeout = 2000
				}

				EntityState.StateSlimeBoss.DASH -> {
					vulnerable = false
					accel[0] = directionToPlayer[0] * 5f
					accel[2] = directionToPlayer[2] * 5f

					timeout = 500
				}
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

		// SlimeBoss is hit by player

		isHit = true

		if (isDead(this, player!!.damage) && vulnerable) {
			health = 0
		}

		if (vulnerable) {
			receiveKnockback(player!!.direction, knockback)
		}

	}
}
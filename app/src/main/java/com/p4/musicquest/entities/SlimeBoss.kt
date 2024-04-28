package com.p4.musicquest.entities

import android.content.Context
import com.p4.musicquest.Animator
import com.p4.musicquest.Entity
import com.p4.musicquest.EntityState
import com.p4.musicquest.MusicManager
import com.p4.musicquest.R
import com.p4.musicquest.Renderer
import com.p4.musicquest.SpriteSheet
import com.p4.musicquest.World
import kotlin.math.pow
import kotlin.math.sqrt

class SlimeBoss (val context: Context, world: World, val pos: Array<Float>, var player: Player?, val renderer: Renderer) : Entity(
	world, Animator(SpriteSheet(context).getSlimeBoss("textures/slime_run_spritesheeet.png")), pos, .2f, .5f){

	init {
		health = 10f
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

			var distanceToPlayerX = player!!.position[0] - this.position[0]
			var distanceToPlayerY = player!!.position[2] - this.position[2]

			// calculate distance between enemy to player
			var distanceToPlayer = sqrt((distanceToPlayerX).pow(2) + (distanceToPlayerY).pow(2))

			if (System.currentTimeMillis() - timeNextState >= timeout && distanceToPlayer <= 4.5f) {
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
					distanceToPlayerX = player!!.position[0] - this.position[0]
					distanceToPlayerY = player!!.position[2] - this.position[2]

					// calculate distance between enemy to player
					distanceToPlayer = sqrt((distanceToPlayerX).pow(2) + (distanceToPlayerY).pow(2))

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

			if (health <= 0){
				val discBeach = Item(context, "Disque de la plage","textures/disc2.png", floatArrayOf(0f, 0f, 12f, 12f), floatArrayOf(12f, 12f), 0.5f, position, player, world, renderer,
					onClickInventory = {
						val disttozero = sqrt(player!!.position[0] * player!!.position[0] + player!!.position[2] * player!!.position[2])
						if (disttozero <= 1.3f) {
							world.state = World.WorldState.MAGMA_UNGREYED
							MusicManager.playMusic(R.raw.piano_music_quest)
							renderer.ui.addMessage("Disque de la plage utilisé")
							renderer.ui.guide.defineText(8)
						}else {
							renderer.ui.addMessage("Rapprochez vous du jukebox")
						}
					}, onClickScenario = {
						renderer.ui.guide.defineText(7)
					})
				world.listItem.add(discBeach)
			}

		}

		super.update(dt)
	}

	fun getHit() {

		// SlimeBoss is hit by player

		isHit = true

		if (isDead(this, player!!.damage) && vulnerable) {
			health = 0f
		}

		if (vulnerable) {
			receiveKnockback(player!!.direction, knockback)
		}

	}
}
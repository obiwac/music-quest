package com.p4.musicquest.entities

import android.content.Context
import com.p4.musicquest.Animator
import com.p4.musicquest.Entity
import com.p4.musicquest.MusicManager
import com.p4.musicquest.R
import com.p4.musicquest.Renderer
import com.p4.musicquest.SpriteSheet
import com.p4.musicquest.World
import java.util.Random
import kotlin.math.pow
import kotlin.math.sqrt

class IceBoss(val context: Context, world: World, val pos: Array<Float>, var player: Player?, val renderer: Renderer) : Entity(
	world, Animator(SpriteSheet(context).getSpriteList("textures/Ghoul.png")), pos, .2f, .5f
) {

	init {
		health = 1
		isHit = false
		damage = 3
		knockback = 15f
		entityLife = true

	}

	val x_initial = position[0]
	val y_initial = position[1]
	val z_initial = position[2]

	val pos_initial = pos.clone()

	var lastShoot = System.currentTimeMillis()
	var lastDirection = System.currentTimeMillis()

	var random = Random()

	var dirX = arrayOf(1f, 0f, -1f, 0f)
	var dirY = arrayOf(0f, -1f, 0f, 1f)
	var dirFace = 1

	var canSpawn = false

	override fun update(dt: Float) {
		// Update velocity of the enemy so that the velocity is in the direction of the player
		if (player != null ) {

			// AI's monster to follow the player
			val distanceToPlayerX = player!!.position[0] - this.position[0]
			val distanceToPlayerY = player!!.position[2] - this.position[2]

			// calculate distance between enemy to player
			val distanceToPlayer = sqrt((distanceToPlayerX).pow(2) + (distanceToPlayerY).pow(2))

			val directionX: Float = distanceToPlayerX / distanceToPlayer
			val directionY: Float = distanceToPlayerY / distanceToPlayer

			directionToPlayer[0] = directionX
			directionToPlayer[2] = directionY

			if (System.currentTimeMillis() - lastDirection >= 3000) {
				lastDirection = System.currentTimeMillis()
				dirFace = random.nextInt(4)
			}

			// Direction of the boss

			if (distanceToPlayer <= 4.5f) {
				accel[0] = dirX[dirFace] * 0.5f
				accel[2] = dirY[dirFace] * 0.5f
			} else {
				accel[0] = 0f
				accel[2] = 0f
			}

			// Shoot player

			if (System.currentTimeMillis() - lastShoot >= 4000 && distanceToPlayer <= 4.5f) {
				lastShoot = System.currentTimeMillis()

				world.shoot(this)
			}

			// Spawn mob

			if (health % 25 == 0 && health != 0 && canSpawn) {

				val listCoordsMonster = arrayOf(arrayOf(position[0] + 2.2f, 0f, position[2]),
												arrayOf(position[0] - 2.2f, 0f, position[2]),
												arrayOf(position[0], 0f, position[2] + 2.2f),
												arrayOf(position[0], 0f, position[2] - 2.2f))

				for (i in listCoordsMonster.indices) {
					world.listMonster.add(Monster(context, world, listCoordsMonster[i], player,"textures/ice_undead.png"))
				}
				canSpawn = false

			}

			// The boss not escape from the boss room

			// calculate distance between spawn and boss
			val distanceToSpawnX = pos_initial[0] - this.position[0]
			val distanceToSpawnY = pos_initial[2] - this.position[2]
			val distanceToSpawn = sqrt((distanceToSpawnX).pow(2) + (distanceToSpawnY).pow(2))

			if (distanceToSpawn > 2.5f) {
				this.position[0] = pos_initial[0]
				this.position[2] = pos_initial[2]
			}

			if (health <= 0){
				val iceDisk = Item(context, "iceDisc","textures/disc3.png", floatArrayOf(0f, 0f, 12f, 12f), floatArrayOf(12f, 12f), 0.5f, position, player, world, renderer,
					onClickInventory = {
						val disttozero = sqrt(player!!.position[0] * player!!.position[0] + player!!.position[2] * player!!.position[2])
						if (disttozero <= 1.3f) {
							world.state = World.WorldState.BEACH_UNGREYED
							MusicManager.playMusic(R.raw.trompette_music_quest)
							renderer.ui.addMessage("Disque de la glace utilisé")
							renderer.ui.guide.defineText(6)
						}else {
							renderer.ui.addMessage("Rapprochez vous du jukebox")
						}
					}, onClickScenario = {
						renderer.ui.guide.defineText(5)
					})

				world.listItem.add(iceDisk)
			}
		}

		super.update(dt)

	}

	fun getHit() {

		// IceBoss is hit by player

		canSpawn = true

		isHit = true

		if (isDead(this, player!!.damage)) {
			health = 0
		}

		receiveKnockback(player!!.direction, knockback)

	}
}
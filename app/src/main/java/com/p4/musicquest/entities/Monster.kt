package com.p4.musicquest.entities

import android.content.Context
import com.p4.musicquest.Animator
import com.p4.musicquest.Entity
import com.p4.musicquest.Sprite
import com.p4.musicquest.SpriteSheet
import com.p4.musicquest.World
import kotlin.math.pow
import kotlin.math.sqrt

class Monster(context: Context, world: World, pos: Array<Float>, var player: Player?, texPath: String = "textures/Undead.png")
	: Entity(world, Animator(getSpritesheet(context, texPath)), pos, .2f, .5f) {
	companion object {
		private var spritesheets: MutableMap<String, ArrayList<Sprite>> = mutableMapOf()

		fun getSpritesheet(context: Context, texPath: String): ArrayList<Sprite> {
			if (spritesheets[texPath] == null) {
				spritesheets[texPath] = SpriteSheet(context).getSpriteList(texPath)
			}

			return spritesheets[texPath]!!
		}
	}

	init {
		health = 10f
		isHit = false
		damage = 2
		knockback = 15f
		entityLife = true
	}

	val x_initial = position[0]
	val y_initial = position[1]
	val z_initial = position[2]
	val startHealth = health

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
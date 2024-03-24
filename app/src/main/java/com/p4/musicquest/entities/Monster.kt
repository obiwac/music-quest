package com.p4.musicquest.entities

import android.content.Context
import com.p4.musicquest.Entity
import com.p4.musicquest.Sprite
import com.p4.musicquest.World
import kotlin.math.pow
import kotlin.math.sqrt

class Monster (context: Context, world: World, pos: Array<Float>, private val player: Player?) : Entity(
	world, Sprite(context, "textures/Undead.png", floatArrayOf(7f, 4f)), pos,
	.6f, 1f
) {
	val input = arrayOf(0f, 0f)

	override fun update(dt: Float) {
		// Update velocity of the enemy so that the velocity is in the direction of the player
		if (player != null) {

			val distanceToPlayerX = player.position[0] - this.position[0]
			val distanceToPlayerY = player.position[2] - this.position[2]

			// calculate distance between enemy to player
			val distanceToPlayer = sqrt((distanceToPlayerX).pow(2) + (distanceToPlayerY).pow(2))

			// calculate direction from enemy to player
			if (distanceToPlayer > 0.1) {
				val directionX: Float = distanceToPlayerX / distanceToPlayer
				val directionY: Float = distanceToPlayerY / distanceToPlayer

				accel[0] = directionX
				accel[2] = directionY
			} else {
				accel[0] = 0f
				accel[2] = 0f
			}
		}

		super.update(dt)
	}
}
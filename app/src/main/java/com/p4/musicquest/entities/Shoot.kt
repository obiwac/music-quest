package com.p4.musicquest.entities

import android.content.Context
import com.p4.musicquest.Entity
import com.p4.musicquest.Sprite
import com.p4.musicquest.World

class Shoot (context: Context, private val shooter: Player?, world: World, pos: Array<Float>) : Entity(
	world, Sprite(context, "textures/Human.png", floatArrayOf(7f, 4f)), pos,
	.6f, 1f) {

	var directionPlayer = arrayOf(0f, 0f, -1f)

	override fun update(dt: Float) {
		if (shooter != null) {
			position[0] = position[0] + (directionPlayer[0] * 0.1f)
			position[2] = position[2] + (directionPlayer[2] * 0.1f)

			for (monster in world.listeMonstres) {
				val dead = collider.intersection(monster.collider)

				if (dead) {
					monster.position[0] = monster.x_initial
					monster.position[1] = monster.y_initial
					monster.position[2] = monster.z_initial
				}
			}


		}

 		super.update(dt)
	}
}
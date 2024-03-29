package com.p4.musicquest.entities

import android.content.Context
import android.util.Log
import com.p4.musicquest.Collider
import com.p4.musicquest.Entity
import com.p4.musicquest.Sprite
import com.p4.musicquest.World
import android.app.Activity
import kotlin.math.abs


class Player(private val context: Context, world: World, pos: Array<Float>) : Entity(
	world, Sprite(context, "textures/Human.png", floatArrayOf(7f, 4f)), pos,
	.6f, 1f
) {
	val input = arrayOf(0f, 0f)
	var listShoot2 = ArrayList<Shoot>()

	override fun update(dt: Float) {
		accel[0] += input[0]
		accel[2] += input[1]

		for (monster in world.listeMonstres) {
			val dead = collider.intersection(monster.collider)
			if (dead) {
				position[0] = 0f
				position[1] = 0f
				position[2] = 0f
				for (monster in world.listeMonstres){
					monster.position[0] = monster.x_initial
					monster.position[1] = monster.y_initial
					monster.position[2] = monster.z_initial
				}
				break
			}
		}
		super.update(dt)
	}

	fun shoot() {
		listShoot2.add(Shoot(context, this, world, position))
	}
}
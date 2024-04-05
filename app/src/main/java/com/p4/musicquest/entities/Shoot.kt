package com.p4.musicquest.entities

import android.content.Context
import com.p4.musicquest.Animator
import com.p4.musicquest.Entity
import com.p4.musicquest.Sprite
import com.p4.musicquest.SpriteSheet
import com.p4.musicquest.World

class Shoot (context: Context, private val shooter: Player?, world: World, pos: Array<Float>) : Entity(
	world, Animator(SpriteSheet(context).getSpriteListProjectile("textures/note.png")), pos, .6f, 1f) {
	init {
		entityLife = false
	}
	var directionPlayer = arrayOf(0f, 0f, -1f)
	private var hit = false

	override fun update(dt: Float) {
		if (shooter != null) {

			position[0] = position[0] + (directionPlayer[0] * 0.1f)
			position[2] = position[2] + (directionPlayer[2] * 0.1f)

			for (monster in world.listeMonstres) {

				hit = collider.intersection(monster.collider)

				if (hit) {
					monster.isHit = true
					if (isDead(monster, shooter.damage)) {
						monster.position[0] = monster.x_initial
						monster.position[1] = monster.y_initial
						monster.position[2] = monster.z_initial

						monster.health = monster.startHealth

					}

					monster.receiveKnockback(directionPlayer, monster.knockback)

					// "delete" shoot
					velocity[0] = 0f
					velocity[2] = 0f
					position[0] = 999f
					position[2] = 999f

				}
			}


		}

 		super.update(dt)
	}
}
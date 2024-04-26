package com.p4.musicquest.entities

import android.content.Context
import com.p4.musicquest.Animator
import com.p4.musicquest.Entity
import com.p4.musicquest.SpriteSheet
import com.p4.musicquest.World

class Shoot (context: Context, private val shooter: Entity?, world: World, pos: Array<Float>) : Entity(
	world, Animator(SpriteSheet(context).getSpriteListProjectile("textures/note.png")), pos, .6f, 1f) {
	init {
		entityLife = false
		damage = 3
		knockback = 15f
	}

	var directionEntity = arrayOf(0f, 0f, -1f)
	private var hit = false

	override fun update(dt: Float) {
		if (shooter != null) {
			if(shooter is Player) {
				position[0] = position[0] + (directionEntity[0] * 0.1f)
				position[2] = position[2] + (directionEntity[2] * 0.1f)

				for (monster in world.listMonster) {

					hit = collider.intersection(monster.collider)

					if (hit) {
						monster.isHit = true
						if (isDead(monster, shooter.damage)) {
							monster.position[0] = monster.x_initial
							monster.position[1] = monster.y_initial
							monster.position[2] = monster.z_initial

							monster.health = monster.startHealth

						}

						monster.receiveKnockback(directionEntity, monster.knockback)

						// "delete" shoot
						velocity[0] = 0f
						velocity[2] = 0f
						position[0] = 999f
						position[2] = 999f

					}
				}
			} else if (shooter is IceBoss && world.player != null) {
				position[0] = position[0] + (directionEntity[0] * 0.03f)
				position[2] = position[2] + (directionEntity[2] * 0.03f)

				val hit = collider.intersection(world.player!!.collider)

				if (hit) {

					directionToPlayer = directionEntity
					world.player!!.getHit(this)

					// "delete" shoot
					velocity[0] = 0f
					velocity[2] = 0f
					position[0] = 999f
					position[2] = 999f
				}

				for (collider in world.colliders) {
					if (collider.x1 < position[0] && position[0] < collider.x2 && collider.z1 < position[2] && position[2] < collider.z2) {
						// "delete" shoot
						velocity[0] = 0f
						velocity[2] = 0f
						position[0] = 999f
						position[2] = 999f
					}
				}
			}


		}

 		super.update(dt)
	}
}
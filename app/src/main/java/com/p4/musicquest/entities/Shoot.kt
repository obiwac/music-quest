package com.p4.musicquest.entities

import android.content.Context
import com.p4.musicquest.Animator
import com.p4.musicquest.Entity
import com.p4.musicquest.Sprite
import com.p4.musicquest.SpriteSheet
import com.p4.musicquest.World

class Shoot (context: Context, private val shooter: Entity?, world: World, pos: Array<Float>, texPath: String, dimension: FloatArray, size: FloatArray, multiplicator: Float)
	: Entity(world, Animator(getSpritesheet(context, texPath, dimension, size, multiplicator)), pos, .6f, 1f) {
	companion object {
		private var spritesheets: MutableMap<String, ArrayList<Sprite>> = mutableMapOf()

		fun getSpritesheet(context: Context, texPath: String, dimension: FloatArray, size: FloatArray, multiplier: Float): ArrayList<Sprite> {
			if (spritesheets[texPath] == null) {
				spritesheets[texPath] = SpriteSheet(context).getItem(texPath, dimension, size, multiplier)
			}

			return spritesheets[texPath]!!
		}
	}

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
						monster.health -= shooter.damage

						monster.receiveKnockback(directionEntity, monster.knockback)

						// "delete" shoot
						velocity[0] = 0f
						velocity[2] = 0f
						position[0] = 999f
						position[2] = 999f

					}
				}
			} else if ((shooter is IceBoss || shooter is VolcanoBoss || shooter is CandyBoss) && world.player != null) {

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
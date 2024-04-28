package com.p4.musicquest.entities

import android.content.Context
import com.p4.musicquest.Entity
import com.p4.musicquest.World
import com.p4.musicquest.Animator
import com.p4.musicquest.SpriteSheet


class Player(private val context: Context, world: World, pos: Array<Float>) : Entity(
	world, Animator(SpriteSheet(context).getSpriteList("textures/Human.png")), pos, .2f, .5f
) {
	companion object {
		const val INITIAL_HEALTH = 20f
	}

	init {
		health = INITIAL_HEALTH
		isHit = false
		damage = 5
		knockback = 13f
		isAttack = false
		entityLife = true
	}

	val input = arrayOf(0f, 0f)

	fun attackWithSword() {
		isAttack = true
		for (monster in world.listMonster) {
			val inHurtbox = hurtBox.intersection(monster.collider)

			if (!inHurtbox) {
				continue
			}

			monster.getHit()
		}

		if (world.iceBoss != null) {
			val inHurtbox = hurtBox.intersection(world.iceBoss!!.collider)

			if (inHurtbox) {
				world.iceBoss!!.getHit()
			}
		}

		if (world.slimeBoss != null) {
			val inHurtbox = hurtBox.intersection(world.slimeBoss!!.collider)

			if (inHurtbox) {
				world.slimeBoss!!.getHit()
			}
		}
	}

	override fun update(dt: Float) {
		accel[0] += input[0] * 1.5f
		accel[2] += input[1] * 1.5f

		super.update(dt)
	}

	fun getHit(offender: Entity?) {

		// When the player stands on grey

		if (offender == null) {
			if (isDead(this, 1)) {
				health = 0f
			}
			return
		}

		isHit = true

		if (isDead(this, offender.damage)) {
			health = 0f
		}

		if (offender is SlimeBoss) {
			receiveKnockback(offender.directionToPlayer, 30f)
			return
		}

		receiveKnockback(offender.directionToPlayer, knockback)
	}

	fun resetPlayer() {
		position[0] = 0f
		position[1] = 0f
		position[2] = 0f
		health = INITIAL_HEALTH

		// Reset position of monsters
		for (monster in world.listMonster){
			monster.position[0] = monster.x_initial
			monster.position[1] = monster.y_initial
			monster.position[2] = monster.z_initial

			monster.health = monster.startHealth
		}
	}
}
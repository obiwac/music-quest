package com.p4.musicquest.entities

import android.content.Context
import android.util.Log
import com.p4.musicquest.Collider
import com.p4.musicquest.Entity
import com.p4.musicquest.Sprite
import com.p4.musicquest.World
import android.app.Activity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.p4.musicquest.Animator
import com.p4.musicquest.SpriteSheet
import kotlin.math.abs


class Player(private val context: Context, world: World, pos: Array<Float>) : Entity(
	world, Animator(SpriteSheet(context).getSpriteList("textures/Human.png")), pos, .6f, 1f
) {
	companion object {
		const val INITIAL_HEALTH = 20
	}

	init {
		health.intValue = INITIAL_HEALTH
		isHit = false
		damage = 10
		knockback = 13f
		isAttack = false
		entityLife = true
	}

	val input = arrayOf(0f, 0f)
	private var hit = false
	private var attackInHurtbox = false // collider when an enemy is inside take damage when player attacks

	override fun update(dt: Float) {
		accel[0] += input[0]
		accel[2] += input[1]
		if (health.intValue <=0) {health.intValue = INITIAL_HEALTH}

		outer@ for (monster in world.listeMonstres) {

			hit = collider.intersection(monster.collider)
			//hit = false

			if (hit) {

				isHit = true

				if (isDead(this, monster.damage)) {
					position[0] = 0f
					position[1] = 0f
					position[2] = 0f
					health.intValue = 0


					for (monster in world.listeMonstres){

						// remettre les monstres ou ils etaient
						monster.position[0] = monster.x_initial
						monster.position[1] = monster.y_initial
						monster.position[2] = monster.z_initial

					}
					break@outer
				}
				receiveKnockback(monster.directionToPlayer, knockback)
			}

			attackInHurtbox = hurtBox.intersection(monster.collider)

			if (attackInHurtbox && isAttack) {
				monster.isHit = true
				if (isDead(monster, damage)) {
					monster.position[0] = monster.x_initial
					monster.position[1] = monster.y_initial
					monster.position[2] = monster.z_initial

					monster.health.intValue = INITIAL_HEALTH
				}
				monster.receiveKnockback(direction, monster.knockback)
			}
			//isAttack = false

		}
		super.update(dt)
	}
}
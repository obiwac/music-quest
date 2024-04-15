package com.p4.musicquest.entities

import android.content.Context
import com.p4.musicquest.Entity
import com.p4.musicquest.World
import com.p4.musicquest.Animator
import com.p4.musicquest.SpriteSheet
import com.p4.musicquest.inventory.InventoryItem


class Player(private val context: Context, world: World, pos: Array<Float>) : Entity(
	world, Animator(SpriteSheet(context).getSpriteList("textures/Human.png")), pos, .2f, .5f
) {
	companion object {
		const val INITIAL_HEALTH = 20
	}

	init {
		health = INITIAL_HEALTH
		isHit = false
		damage = 10
		knockback = 13f
		isAttack = false
		entityLife = true
	}

	val input = arrayOf(0f, 0f)
	var itemListInventory = arrayListOf<InventoryItem>()

	fun attackWithSword() {
		isAttack = true
		for (monster in world.listMonster) {
			val inHurtbox = hurtBox.intersection(monster.collider)

			if (!inHurtbox) {
				continue
			}

			monster.getHit()
		}
	}

	override fun update(dt: Float) {
		accel[0] += input[0] * 1.5f
		accel[2] += input[1] * 1.5f

		super.update(dt)
	}

	fun getHit(monster: Monster) {
		isHit = true

		if (isDead(this, monster.damage)) {
			health = 0
		}

		receiveKnockback(monster.directionToPlayer, knockback)
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
		}
	}
}
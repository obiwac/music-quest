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
		health = INITIAL_HEALTH
		isHit = false
		damage = 10
		knockback = 13f
		isAttack = false
		entityLife = true
	}

	val input = arrayOf(0f, 0f)

	fun attackWithSword() {
		isAttack = true
		for (monster in world.listeMonstres) {
			val inHurtbox = hurtBox.intersection(monster.collider)

			// TODO ceci devrait être dans le monstre. C'est pas le job du joueur de s'occuper de l'état du monstre,
			//      ça devrait être le monstre qui s'en occupe. Genre une fonction monster.getHit()

			if (!inHurtbox) {
				continue
			}

			monster.isHit = true

			if (isDead(monster, damage)) {
				monster.position[0] = monster.x_initial
				monster.position[1] = monster.y_initial
				monster.position[2] = monster.z_initial

				monster.health = INITIAL_HEALTH
			}

			monster.receiveKnockback(direction, monster.knockback)
		}
	}

	override fun update(dt: Float) {
		accel[0] += input[0] * 1.5f
		accel[2] += input[1] * 1.5f
		if (health <=0) {health = INITIAL_HEALTH}

		// TODO ceci devrait vraiment être dans l'autre sens. C'est un monstre qui fait l'action d'attaquer un joueur,
		//      pas le joueur qui fait l'action de recevoir l'attaque. Dans la fonction update du monstre, on devrait check s'il attaque le joueur

		outer@ for (monster in world.listeMonstres) {
			//var hit = collider.intersection(monster.collider)
			var hit = false

			if (!hit) {
				continue
			}

			isHit = true

			if (isDead(this, monster.damage)) {
				position[0] = 0f
				position[1] = 0f
				position[2] = 0f
				health = 0


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

		super.update(dt)
	}
}
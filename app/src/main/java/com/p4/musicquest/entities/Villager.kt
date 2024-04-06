package com.p4.musicquest.entities

import android.content.Context
import com.p4.musicquest.Animator
import com.p4.musicquest.Camera
import com.p4.musicquest.Entity
import com.p4.musicquest.Shader
import com.p4.musicquest.Sprite
import com.p4.musicquest.SpriteSheet
import com.p4.musicquest.World

class Villager (private val context: Context, private val player: Player?, world: World, pos: Array<Float>) : Entity(
	world, Animator(SpriteSheet(context).getSpriteList("textures/Dwarf.png")), pos, .6f, 1f
) {

	var popup = Sprite(context, "textures/interraction.png", floatArrayOf(0f, 0f, 700f, 75f))
	var showSignal = true
	var isInteract = false
	var showMessage = false

	var textForDialog = "Bonjour mon brave"
	override fun update(dt: Float) {

		if (player != null) {
			isInteract = player.hurtBox.intersection(collider)

			if (isInteract && player.isAttack) {
				// start popup dialog when player interacts with villager
				showSignal = false
				showMessage = true
			}
		}
		super.update(dt)
	}

	fun drawEntity(shader: Shader, camera: Camera){
		if (showSignal) {
			popup.draw(shader, camera, position[0] + 0.2f , 0.2f, position[2] + 0.7f)
		}

	}

}
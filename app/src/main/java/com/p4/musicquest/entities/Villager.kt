package com.p4.musicquest.entities

import android.content.Context
import com.p4.musicquest.Animator
import com.p4.musicquest.Camera
import com.p4.musicquest.Entity
import com.p4.musicquest.Shader
import com.p4.musicquest.Sprite
import com.p4.musicquest.SpriteSheet
import com.p4.musicquest.World

class Villager (private val context: Context, world: World, pos: Array<Float>) : Entity(
	world, Animator(SpriteSheet(context).getSpriteList("textures/Human.png")), pos, .6f, 1f
) {

	var popup = Sprite(context, "textures/Human.png", floatArrayOf(0f, 0f, 700f, 75f))
	var interaction = true
	override fun update(dt: Float) {
		//popup.position[1] = 2f
		super.update(dt)
	}

	fun drawEntity(shader: Shader, camera: Camera){
		if (interaction) {
			popup.draw(shader, camera, position[0] + 0.2f , 0.2f, position[2] + 0.7f)
		}

	}

}
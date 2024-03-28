package com.p4.musicquest.entities

import android.content.Context
import com.p4.musicquest.Entity
import com.p4.musicquest.Sprite
import com.p4.musicquest.World

class Shoot (context: Context, private val shooter: Player, world: World, pos: Array<Float>) : Entity(
	world, Sprite(context, "textures/Human.png", floatArrayOf(7f, 4f)), shooter.direction,
	.6f, 1f) {

	override fun update(dt: Float) {
		accel[0] = shooter.direction[0]
		accel[2] = shooter.direction[2]
		super.update(dt)
	}
}
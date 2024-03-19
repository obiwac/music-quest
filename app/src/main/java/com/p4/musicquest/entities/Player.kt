package com.p4.musicquest.entities

import android.content.Context
import android.util.Log
import com.p4.musicquest.Entity
import com.p4.musicquest.Sprite

class Player(context: Context, pos: Array<Float>) : Entity(
	Sprite(context, "textures/Human.png"), pos,
	.6f, 1f
) {
	val input = arrayOf(0f, 0f)

	override fun update(dt: Float) {
		accel[0] += input[0]
		accel[2] += input[1]

		super.update(dt)
	}
}
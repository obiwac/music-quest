package com.p4.musicquest

import android.content.Context

class SpriteSheet(private val context: Context) {

	// Take all sprites for the animation

	fun getSpriteList(texPath: String): ArrayList<Sprite> {
		var spriteList = ArrayList<Sprite>()

		spriteList.add(Sprite(context, texPath, floatArrayOf(7f, 4f, 10f, 15f))) // not moving 1
		spriteList.add(Sprite(context, texPath, floatArrayOf(31f, 4f, 10f, 15f))) // not moving 2

		spriteList.add(Sprite(context, texPath, floatArrayOf(79f, 4f, 10f, 15f))) // moving down 1
		spriteList.add(Sprite(context, texPath, floatArrayOf(127f, 4f, 10f, 15f))) // moving down 2

		spriteList.add(Sprite(context, texPath, floatArrayOf(79f, 28f, 10f, 15f))) // moving left 1
		spriteList.add(Sprite(context, texPath, floatArrayOf(127f, 28f, 10f, 15f))) // moving left 2

		spriteList.add(Sprite(context, texPath, floatArrayOf(79f, 52f, 10f, 15f))) // moving right 1
		spriteList.add(Sprite(context, texPath, floatArrayOf(127f, 52f, 10f, 15f))) // moving right 2

		spriteList.add(Sprite(context, texPath, floatArrayOf(79f, 76f, 10f, 15f))) // moving up 1
		spriteList.add(Sprite(context, texPath, floatArrayOf(127f, 76f, 10f, 15f))) // moving up 2

		return spriteList
	}
}
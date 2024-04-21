package com.p4.musicquest

import android.content.Context

class SpriteSheet(private val context: Context) {

	// Take all sprites for the animation

	fun getSpriteList(texPath: String): ArrayList<Sprite> {
		var spriteList = ArrayList<Sprite>()

		spriteList.add(Sprite(context, texPath, floatArrayOf(0f, 0f, 24f, 24f), floatArrayOf(768f, 96f))) // not moving 1
		spriteList.add(Sprite(context, texPath, floatArrayOf(24f, 0f, 24f, 24f), floatArrayOf(768f, 96f))) // not moving 2

		spriteList.add(Sprite(context, texPath, floatArrayOf(72f, 0f, 24f, 24f), floatArrayOf(768f, 96f))) // moving down 1
		spriteList.add(Sprite(context, texPath, floatArrayOf(120f, 0f, 24f, 24f), floatArrayOf(768f, 96f))) // moving down 2

		spriteList.add(Sprite(context, texPath, floatArrayOf(72f, 24f, 24f, 24f), floatArrayOf(768f, 96f))) // moving left 1
		spriteList.add(Sprite(context, texPath, floatArrayOf(120f, 24f, 24f, 24f), floatArrayOf(768f, 96f))) // moving left 2

		spriteList.add(Sprite(context, texPath, floatArrayOf(72f, 48f, 24f, 24f), floatArrayOf(768f, 96f))) // moving right 1
		spriteList.add(Sprite(context, texPath, floatArrayOf(120f, 48f, 24f, 24f), floatArrayOf(768f, 96f))) // moving right 2

		spriteList.add(Sprite(context, texPath, floatArrayOf(72f, 72f, 24f, 24f), floatArrayOf(768f, 96f))) // moving up 1
		spriteList.add(Sprite(context, texPath, floatArrayOf(120f, 72f, 24f, 24f), floatArrayOf(768f, 96f))) // moving up 2

		spriteList.add(Sprite(context, texPath, floatArrayOf(168f, 0f, 24f, 24f), floatArrayOf(768f, 96f))) // damage down
		spriteList.add(Sprite(context, texPath, floatArrayOf(168f, 24f, 24f, 24f), floatArrayOf(768f, 96f))) // damage left
		spriteList.add(Sprite(context, texPath, floatArrayOf(168f, 48f, 24f, 24f), floatArrayOf(768f, 96f))) // damage right
		spriteList.add(Sprite(context, texPath, floatArrayOf(168f, 72f, 24f, 24f), floatArrayOf(768f, 96f))) // damage up

		spriteList.add(Sprite(context, texPath, floatArrayOf(408f, 0f, 24f, 24f), floatArrayOf(768f, 96f))) // sword down damage
		spriteList.add(Sprite(context, texPath, floatArrayOf(432f, 0f, 24f, 24f), floatArrayOf(768f, 96f))) // sword down

		spriteList.add(Sprite(context, texPath, floatArrayOf(408f, 24f, 22f, 21f), floatArrayOf(768f, 96f))) // sword left damage
		spriteList.add(Sprite(context, texPath, floatArrayOf(432f, 24f, 22f, 21f), floatArrayOf(768f, 96f))) // sword left

		spriteList.add(Sprite(context, texPath, floatArrayOf(408f, 48f, 24f, 24f), floatArrayOf(768f, 96f))) // sword right damage
		spriteList.add(Sprite(context, texPath, floatArrayOf(432f, 48f, 24f, 24f), floatArrayOf(768f, 96f))) // sword right

		spriteList.add(Sprite(context, texPath, floatArrayOf(408f, 72f, 24f, 24f), floatArrayOf(768f, 96f))) // sword up damage
		spriteList.add(Sprite(context, texPath, floatArrayOf(432f, 72f, 24f, 24f), floatArrayOf(768f, 96f))) // sword up

		return spriteList
	}

	fun getSpriteListProjectile(texPath: String) : ArrayList<Sprite> {
		var spriteList = ArrayList<Sprite>()

		spriteList.add(Sprite(context, texPath, floatArrayOf(0f, 0f, 348f, 99.5f), floatArrayOf(768f, 96f)))
		spriteList.add(Sprite(context, texPath, floatArrayOf(796f, 0f, 348f, 99.5f), floatArrayOf(768f, 96f)))

		return spriteList
	}

	fun getItem(texPath: String, dimension: FloatArray, size: FloatArray, multiplicator: Float) : ArrayList<Sprite> {
		var spriteList = ArrayList<Sprite>()

		spriteList.add(Sprite(context, texPath, dimension, size, multiplicator))
		spriteList.add(Sprite(context, texPath, dimension, size, multiplicator))

		return spriteList
	}

}
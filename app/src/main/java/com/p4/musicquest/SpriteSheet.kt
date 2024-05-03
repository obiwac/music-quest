package com.p4.musicquest

import android.content.Context

class SpriteSheet(private val context: Context) {

	// Take all sprites for the animation

	fun getSpriteList(texPath: String): ArrayList<Sprite> {
		val spriteList = ArrayList<Sprite>()
		val tex = Texture(context, texPath)

		spriteList.add(Sprite(tex, floatArrayOf(0f, 0f, 24f, 24f), floatArrayOf(768f, 96f))) // not moving 1
		spriteList.add(Sprite(tex, floatArrayOf(24f, 0f, 24f, 24f), floatArrayOf(768f, 96f))) // not moving 2

		spriteList.add(Sprite(tex, floatArrayOf(72f, 0f, 24f, 24f), floatArrayOf(768f, 96f))) // moving down 1
		spriteList.add(Sprite(tex, floatArrayOf(120f, 0f, 24f, 24f), floatArrayOf(768f, 96f))) // moving down 2

		spriteList.add(Sprite(tex, floatArrayOf(72f, 24f, 24f, 24f), floatArrayOf(768f, 96f))) // moving left 1
		spriteList.add(Sprite(tex, floatArrayOf(120f, 24f, 24f, 24f), floatArrayOf(768f, 96f))) // moving left 2

		spriteList.add(Sprite(tex, floatArrayOf(72f, 48f, 24f, 24f), floatArrayOf(768f, 96f))) // moving right 1
		spriteList.add(Sprite(tex, floatArrayOf(120f, 48f, 24f, 24f), floatArrayOf(768f, 96f))) // moving right 2

		spriteList.add(Sprite(tex, floatArrayOf(72f, 72f, 24f, 24f), floatArrayOf(768f, 96f))) // moving up 1
		spriteList.add(Sprite(tex, floatArrayOf(120f, 72f, 24f, 24f), floatArrayOf(768f, 96f))) // moving up 2

		spriteList.add(Sprite(tex, floatArrayOf(168f, 0f, 24f, 24f), floatArrayOf(768f, 96f))) // damage down
		spriteList.add(Sprite(tex, floatArrayOf(168f, 24f, 24f, 24f), floatArrayOf(768f, 96f))) // damage left
		spriteList.add(Sprite(tex, floatArrayOf(168f, 48f, 24f, 24f), floatArrayOf(768f, 96f))) // damage right
		spriteList.add(Sprite(tex, floatArrayOf(168f, 72f, 24f, 24f), floatArrayOf(768f, 96f))) // damage up

		spriteList.add(Sprite(tex, floatArrayOf(408f, 0f, 24f, 24f), floatArrayOf(768f, 96f))) // sword down damage
		spriteList.add(Sprite(tex, floatArrayOf(432f, 0f, 24f, 24f), floatArrayOf(768f, 96f))) // sword down

		spriteList.add(Sprite(tex, floatArrayOf(408f, 24f, 22f, 21f), floatArrayOf(768f, 96f))) // sword left damage
		spriteList.add(Sprite(tex, floatArrayOf(432f, 24f, 22f, 21f), floatArrayOf(768f, 96f))) // sword left

		spriteList.add(Sprite(tex, floatArrayOf(408f, 48f, 24f, 24f), floatArrayOf(768f, 96f))) // sword right damage
		spriteList.add(Sprite(tex, floatArrayOf(432f, 48f, 24f, 24f), floatArrayOf(768f, 96f))) // sword right

		spriteList.add(Sprite(tex, floatArrayOf(408f, 72f, 24f, 24f), floatArrayOf(768f, 96f))) // sword up damage
		spriteList.add(Sprite(tex, floatArrayOf(432f, 72f, 24f, 24f), floatArrayOf(768f, 96f))) // sword up

		return spriteList
	}

	fun getItem(texPath: String, dimension: FloatArray, size: FloatArray, multiplier: Float) : ArrayList<Sprite> {
		val spriteList = ArrayList<Sprite>()
		val tex = Texture(context, texPath)

		spriteList.add(Sprite(tex, dimension, size, multiplier))
		spriteList.add(Sprite(tex, dimension, size, multiplier))

		return spriteList
	}

	fun getSlimeBoss(texPath: String) : ArrayList<Sprite> {
		val spriteList = ArrayList<Sprite>()
		val tex = Texture(context, texPath)

		spriteList.add(Sprite(tex, floatArrayOf(0f, 0f, 16f, 16f), floatArrayOf(96f, 16f)))
		spriteList.add(Sprite(tex, floatArrayOf(32f, 0f, 16f, 16f), floatArrayOf(96f, 16f)))
		spriteList.add(Sprite(tex, floatArrayOf(64f, 0f, 16f, 16f), floatArrayOf(96f, 16f)))

		return spriteList
	}

}

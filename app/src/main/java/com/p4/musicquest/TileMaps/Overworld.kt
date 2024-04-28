package com.p4.musicquest.TileMaps

import android.content.Context
import com.p4.musicquest.TileMap

class Overworld(context: Context, texPath: String) : TileMap(context, texPath) {
	override val map = arrayOf(
		arrayOf(0, 0, 0, 0, 0, 0),
		arrayOf(0, 0, 0, 0, 0, 0),
		arrayOf(0, 0, 0, 0, 0, 0),
		arrayOf(0, 0, 0, 0, 0, 0),
		arrayOf(0, 0, 0, 0, 0, 0),
		arrayOf(0, 0, 0, 0, 0, 0),
	)

	override val overlay = arrayOf(
		arrayOf(0, 0, 0, 0, 0, 0),
		arrayOf(0, 0, 0, 0, 0, 0),
		arrayOf(0, 0, 0, 0, 0, 0),
		arrayOf(0, 0, 0, 0, 0, 0),
		arrayOf(0, 0, 0, 0, 0, 0),
		arrayOf(0, 0, 0, 0, 0, 0),
	)
}

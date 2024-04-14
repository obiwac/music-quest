package com.p4.musicquest.entities

import android.content.Context
import com.p4.musicquest.Animator
import com.p4.musicquest.Camera
import com.p4.musicquest.Entity
import com.p4.musicquest.Renderer
import com.p4.musicquest.Shader
import com.p4.musicquest.Sprite
import com.p4.musicquest.SpriteSheet
import com.p4.musicquest.UI
import com.p4.musicquest.World

class Item(private val context: Context, name: String, texPath: String?, dimension: FloatArray, size: FloatArray, multiplicator: Float, position: Array<Float>, val player: Player?, world: World, val renderer: Renderer) :
	Entity(world, Animator(SpriteSheet(context).getItem(texPath!!, dimension, size, multiplicator)), position, .2f, .5f){

	var textForDialog = "Vous avez récupéré :\n$name"
	var showMessage = true
	override fun update(dt: Float) {
		if (player != null) {
			var grab = player.collider.intersection(collider)

			if (grab) {
				// Delete from map

				position[0] = 999f
				position[2] = 999f

				player.itemListInventory.add(this)

				recupMessage()
			}
		}

		super.update(dt)

	}

	fun recupMessage() {

		if (!showMessage) {
			return
		}

		// Show a message when you grab an item
		renderer.ui.dialog.initDialog(textForDialog, 10f)
		renderer.ui.uiState = UI.UIState.DIALOG
	}

}
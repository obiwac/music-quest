package com.p4.musicquest.entities

import android.content.Context
import com.p4.musicquest.Animator
import com.p4.musicquest.Entity
import com.p4.musicquest.Renderer
import com.p4.musicquest.SpriteSheet
import com.p4.musicquest.UI
import com.p4.musicquest.World
import com.p4.musicquest.inventory.InventoryItem

class Item(private val context: Context, val name: String, val texPath: String?, val dimension: FloatArray, val size: FloatArray, val multiplicator: Float, position: Array<Float>,
           val player: Player?, world: World, val renderer: Renderer, val onClickInventory: () -> Unit, val onClickScenario: () -> Unit ) :
	Entity(world, Animator(SpriteSheet(context).getItem(texPath!!, dimension, size, multiplicator)), position, .2f, .5f){

	var textForDialog = "Vous avez récupéré :\n$name"
	var showMessage = true

	init {
		entityLife = false
	}

	override fun update(dt: Float) {
		if (player != null) {
			var grab = player.collider.intersection(collider)

			if (grab) {
				// Delete from map
				position[0] = 999f
				position[2] = 999f

				// Add to inventory

				val texPathList = ArrayList<String>()
				texPathList.add(texPath!!)
				val itemInventory = InventoryItem(name, texPath, floatArrayOf(.25f, 0.3f, 0.2f, 0.2f)) {
					onClickInventory()
				}
				renderer.ui.inventoryPlayer.insert(itemInventory)

				recupMessage()
				recupScenario()
			}
		}

		super.update(dt)

	}

	fun recupMessage() {

		// Show a message when the player takes the item

		if (!showMessage) {
			return
		}

		// Show a message when you grab an item
		renderer.ui.dialog.initDialog(textForDialog, 100f)
		renderer.ui.uiState = UI.UIState.DIALOG
	}

	fun recupScenario() {

		// Change something in the world when the player takes the item

		onClickScenario()
	}

}
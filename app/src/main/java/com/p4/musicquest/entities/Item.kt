package com.p4.musicquest.entities

import android.content.Context
import com.p4.musicquest.Animator
import com.p4.musicquest.Entity
import com.p4.musicquest.Renderer
import com.p4.musicquest.Sprite
import com.p4.musicquest.SpriteSheet
import com.p4.musicquest.Texture
import com.p4.musicquest.UI
import com.p4.musicquest.UIRefCorner
import com.p4.musicquest.World
import com.p4.musicquest.inventory.InventoryItem
import com.p4.musicquest.ui.Font
import com.p4.musicquest.ui.Text

class Item(private val context: Context, val name: String, val texPath: String, dimension: FloatArray, val size: FloatArray, multiplier: Float, position: Array<Float>,
           val player: Player?, world: World, val renderer: Renderer, val onClickInventory: () -> Unit, val onClickScenario: () -> Unit )
	: Entity(world, Animator(getSpritesheet(context, texPath, dimension, size, multiplier)), position, .2f, .5f) {
	companion object {
		private var spritesheets: MutableMap<String, ArrayList<Sprite>> = mutableMapOf()

		fun getSpritesheet(context: Context, texPath: String, dimension: FloatArray, size: FloatArray, multiplier: Float): ArrayList<Sprite> {
			if (spritesheets[texPath] == null) {
				spritesheets[texPath] = SpriteSheet(context).getItem(texPath, dimension, size, multiplier)
			}

			return spritesheets[texPath]!!
		}
	}

	var textForDialog = "Vous avez récupéré :\n$name"
	var textDialog: Text? = null
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
				val itemInventory = InventoryItem(name, Texture(context, texPath), floatArrayOf(.25f, 0.3f, 0.2f, 0.2f)) {
					onClickInventory()
				}
				renderer.ui.inventoryPlayer.insert(itemInventory)

				recupMessage()
				recupScenario()
			}
		}

		super.update(dt)

	}

	fun createText() {
		val font = Font(renderer.ui, context, 75f)
		textDialog = Text(renderer.ui, font, textForDialog, UIRefCorner.TOP_CENTER, .1f, 0.1f, .8f)
	}

	fun recupMessage() {

		// Show a message when the player takes the item

		if (!showMessage) {
			return
		}

		// Show a message when you grab an item

		if (name == "piece") {
			renderer.ui.addMessage("Pièce ramassée")
			return
		}

		if (textDialog == null) {
			createText()
		}

		textDialog?.let { renderer.ui.dialog.initDialog(it) }
		renderer.ui.uiState = UI.UIState.DIALOG
	}

	fun recupScenario() {

		// Change something in the world when the player takes the item

		onClickScenario()
	}

}
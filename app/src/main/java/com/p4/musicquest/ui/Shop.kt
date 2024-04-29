package com.p4.musicquest.ui

import android.content.Context
import android.view.MotionEvent
import com.p4.musicquest.Shader
import com.p4.musicquest.UI
import com.p4.musicquest.UIRefCorner
import com.p4.musicquest.entities.Villager
import com.p4.musicquest.inventory.InventoryItem
import com.p4.musicquest.inventory.Slot

class Shop(val context: Context, val ui: UI, val inputItem: InventoryItem?, val outputItem: InventoryItem) {

	var textInDialog: Text? = null
	var textToShow: String? = null
	var sizeFont = 75f

	val background = Element(ui, "ui/mainmenu_menubackground.png", UIRefCorner.CENTER, 0.5f, 0f, 1f, 2f)
	val shopText = Text(ui, ui.font, "Magasin", UIRefCorner.TOP_LEFT, .1f, .2f, .8f)
	val shopBackground = Element(ui, "ui/shop_background.png", UIRefCorner.TOP_CENTER, 0.5f, 0.5f, 0.8f, 0.5f)
	val coinsIndicator = Element(ui, "ui/coins_indicator.png", UIRefCorner.TOP_CENTER, 0.5f, 0.5f, 0.8f, 0.5f)

	// Item of the shop

	var input: InventoryItem? = null
	var output: InventoryItem? = null

	val slotInput = Slot(context, ui, null,  .24f, 0.65f, 0.2f)
	val slotOutput = Slot(context, ui, null,  0.57f, 0.7f, 0.2f)

	fun draw(shader: Shader, dt: Float) {

		input = InventoryItem("piece", "textures/coin.png", floatArrayOf(.25f, 0.3f, 0.2f, 0.2f)) {}

		output = InventoryItem(outputItem.name, outputItem.texture, outputItem.dimension, outputItem.onClick)

		slotInput.update(input, shader, dt)
		slotOutput.update(output, shader, dt)

	}

	fun onTouchEvent(event: MotionEvent, xRes: Float, yRes: Float) {
		exchange()
	}

	fun exchange() {
		val itemInventoryList = ui.inventoryPlayer.itemInventoryList

		if (itemInventoryList.size == 0 || !ui.inventoryPlayer.contain(input!!)) {
			ui.addMessage("Pas d'argent")
			return
		}

		ui.inventoryPlayer.reduce(input!!)

		outputItem.consumable = true // disappear when we click on it

		ui.inventoryPlayer.insert(outputItem)

		ui.addMessage("Achat effectué")

	}
}
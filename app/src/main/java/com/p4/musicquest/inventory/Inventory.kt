package com.p4.musicquest.inventory

import android.content.Context
import android.view.MotionEvent
import com.p4.musicquest.Shader
import com.p4.musicquest.UI
import com.p4.musicquest.UIRefCorner
import com.p4.musicquest.entities.Player
import com.p4.musicquest.ui.Element

class Inventory(context: Context, val ui: UI, player: Player?) {

	var background = Element(ui, "ui/mainmenu_menubackground.png", UIRefCorner.CENTER, 0.5f, 0f, 1f, 2f)

	var itemList = arrayListOf<InventoryItem>()
	var slotList: ArrayList<Slot> = arrayListOf()

	init {
		// size of inventory
		slotList.add(Slot(context, ui, null,  .25f, 0.3f, 0.2f))
		slotList.add(Slot(context, ui, null,  .25f, 0.6f, 0.2f))
		slotList.add(Slot(context, ui, null,  .25f, 0.9f, 0.2f))
		slotList.add(Slot(context, ui, null,  .25f, 0.12f, 0.2f))
		slotList.add(Slot(context, ui, null,  .25f, 0.15f, 0.2f))
		slotList.add(Slot(context, ui, null,  .25f, 0.18f, 0.2f))

		itemList = player!!.itemListInventory
	}

	fun draw(shader: Shader, dt: Float) {
		for (i in 0..<itemList.size) {
			if (i < slotList.size) {
				slotList[i].update(itemList[i], shader, dt)
			}

		}

	}

	fun onTouchEvent(event: MotionEvent, xRes: Float, yRes: Float) {
		for (i in 0..<itemList.size) {
			if (i < slotList.size) {
				slotList[i].onTouchEvent(event, xRes, yRes, itemList[i])
			}
		}
	}

}
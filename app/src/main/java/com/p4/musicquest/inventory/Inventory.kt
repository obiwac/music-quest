package com.p4.musicquest.inventory

import android.content.Context
import android.view.MotionEvent
import com.p4.musicquest.Shader
import com.p4.musicquest.UI
import com.p4.musicquest.UIRefCorner
import com.p4.musicquest.entities.Player
import com.p4.musicquest.ui.Element
import com.p4.musicquest.ui.Text

class Inventory(context: Context, val ui: UI, player: Player?) {

	var background = Element(ui, "ui/mainmenu_menubackground.png", UIRefCorner.CENTER, 0.5f, 0f, 1f, 2f)

	var text = Text(ui, ui.font, "Inventaire", UIRefCorner.TOP_LEFT, .1f, .14f, .6f)

	var itemInventoryList = arrayListOf<InventoryItem>()
	var slotList: ArrayList<Slot> = arrayListOf()

	init {
		// size of inventory
		slotList.add(Slot(context, ui, null,  .25f, 0.3f, 0.2f))
		slotList.add(Slot(context, ui, null,  .25f, 0.6f, 0.2f))
		slotList.add(Slot(context, ui, null,  .25f, 0.9f, 0.2f))
		slotList.add(Slot(context, ui, null,  .5f, 0.3f, 0.2f))
		slotList.add(Slot(context, ui, null,  .5f, 0.6f, 0.2f))
		slotList.add(Slot(context, ui, null,  .5f, 0.9f, 0.2f))
	}

	fun draw(shader: Shader, dt: Float) {
		for (i in 0..<itemInventoryList.size) {
			if (i < slotList.size) {
				slotList[i].update(itemInventoryList[i], shader, dt)
			}
		}
	}

	fun onTouchEvent(event: MotionEvent, xRes: Float, yRes: Float) {
		for (i in 0..<itemInventoryList.size) {
			if (i < slotList.size) {
				slotList[i].onTouchEvent(event, xRes, yRes, itemInventoryList[i])
			}
		}
	}

	fun contain(item: InventoryItem): Boolean {

		var iteratorItem = itemInventoryList.listIterator()

		while (iteratorItem.hasNext()) {
			val elem = iteratorItem.next()

			if (elem.name == item.name && elem.number > 0) {
				return true
			}
		}

		return false
	}

	fun insert(item: InventoryItem) {

		var iteratorItem = itemInventoryList.listIterator()

		while (iteratorItem.hasNext()) {
			val elem = iteratorItem.next()

			if (elem.name == item.name) {
				elem.number ++
				println(elem.name)
				println(elem.number)
				return
			}
		}
		item.number = 1
		itemInventoryList.add(item)

	}

	fun reduce(item: InventoryItem) {

		var iteratorItem = itemInventoryList.listIterator()

		while (iteratorItem.hasNext()) {
			val elem = iteratorItem.next()

			if (elem.name == item.name && elem.number > 0) {
				elem.number --
			}
		}

		// Delete input item from inventory if there is nothing left (see for a better implementation)

		iteratorItem = itemInventoryList.listIterator()
		while (iteratorItem.hasNext()) {
			val elem = iteratorItem.next()

			if (elem.number <= 0) {
				iteratorItem.remove()
				return
			}
		}
	}

}
package com.p4.musicquest.inventory

import android.content.Context
import android.view.MotionEvent
import com.p4.musicquest.Shader
import com.p4.musicquest.Texture
import com.p4.musicquest.UI
import com.p4.musicquest.UIRefCorner
import com.p4.musicquest.ui.Element
import com.p4.musicquest.ui.Font
import com.p4.musicquest.ui.Text

class Slot(val context: Context, val ui: UI, val texPath: ArrayList<String>?, val x: Float, val y: Float, val size: Float) {
	// TODO textPath pour le fond du slot
	// Slot of item in inventory

	val element = Element(ui, null, UIRefCorner.TOP_LEFT, x, y, size, size)

	// Variable for clickable

	var pressing = false
	var itemPointerId = -1

	var sizeFont = 10f
	var slotNumber: Text? = null

	fun update(item: InventoryItem?, shader: Shader, dt: Float) {
		if (item != null) {
			element.width = item.dimension[2]
			element.width = item.dimension[3]

			element.setTex(shader, Texture(context, item.texture))
			element.draw(shader, dt)

			// Show the number of this item in inventory

			if (item.number <= 1) {
				return
			}

			val font = Font(ui, context, sizeFont)

			slotNumber = Text(ui, font, item.number.toString(), UIRefCorner.TOP_LEFT, x + 0.12f, y + 0.085f, 0.07f)
			slotNumber!!.draw(shader, dt)

		}
	}

	fun onTouchEvent(event: MotionEvent, xRes: Float, yRes: Float, item: InventoryItem?) {
		val action = event.actionMasked
		val actionIndex = event.actionIndex
		val pointerId = event.getPointerId(actionIndex)

		val x =    event.getX(event.findPointerIndex(pointerId)) / xRes * 2 - 1f
		val y = - (event.getY(event.findPointerIndex(pointerId)) / yRes * 2 - 1f)

		when (action) {
			MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
				if (element.containsPoint(x, y) && itemPointerId == -1) {
					pressing = true
					itemPointerId = pointerId
				}
			}

			MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP-> {
				if (element.containsPoint(x, y) && pointerId == itemPointerId) {
					pressing = false
					itemPointerId = -1
					if (item != null) {
						item.onClick()

						if (item.name != "piece") {

							//ui.addMessage(item.name + " utilisé")

							// Delete item if consumable

							if (item.consumable) {
								ui.inventoryPlayer.reduce(item)
							}
						}
					}
				}
			}
		}

		if (!pressing) {
			return
		}

	}
}
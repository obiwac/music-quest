package com.p4.musicquest.inventory

import android.content.Context
import com.p4.musicquest.Texture

class InventoryItem(val name: String, tex: Texture, val dimension: FloatArray, val onClick: () -> Unit) {

	// A supprimer peut etre

	var texture = tex
	var number = 1
	var consumable = false
}
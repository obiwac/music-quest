package com.p4.musicquest.inventory

import android.content.Context

class InventoryItem(val name: String, texPath: String, val dimension: FloatArray, val onClick: () -> Unit) {

	// A supprimer peut etre

	var texture = texPath
	var number = 0
}
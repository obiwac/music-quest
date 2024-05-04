package com.p4.musicquest.ui

import android.content.Context
import com.p4.musicquest.UI
import com.p4.musicquest.UIRefCorner
import com.p4.musicquest.World

class Guide(val context: Context, ui: UI) {

	// UI

	val guideDialog = Dialog(context, ui) {
	}

	val size = 100f

	// Choose the text guide to show

	var step = 0

	// List of text

	val listTextString = arrayOf(
		"Allez parler au vieux du village", // 0
		"Trouver le premier disque dans la forêt", // 1
		"Allez parler au vieux du village pour savoir quoi faire du disque", // 2
		"Cliquez sur le disque dans l'inventaire près du jukebox", //3
		"Tuez le boss dans la zone de glace au Nord pour récupérer le deuxième disque", // 4
		"Retournez au village au jukebox pour utiliser le deuxième disque", // 5
		"Tuez le boss dans la zone de la plage à l'Ouest pour récupérer le troisième disque", // 6
		"Retournez au village au jukebox pour utiliser le troisième disque", // 7
		"Tuez le boss dans la zone du volcan dans le Sud pour récupérer le quatrième disque", // 8
		"Retournez au village au jukebox pour utiliser le quatrième disque", // 9
		"Tuez le dernier boss dans la zone de bonbons dans le Sud pour récupérer le dernier disque", // 10
		"Vous avez récuperé tous les disques ! Retournez au jukebox pour rétablir la musique dans le monde entier", // 11
		"Vous avez terminé le jeu, vous pouvez maintenant profiter de la musique et du monde que vous avez libéré"  //12
	)

	val listTextGuide = ArrayList<Text>()


	init {
		val font = Font(ui, context, 75f)

		for (i in listTextString.indices) {
			listTextGuide.add(Text(ui, font, listTextString[i], UIRefCorner.TOP_CENTER, .1f, 0.1f, .8f))
		}

		guideDialog.initDialog(listTextGuide[step])
	}

	fun defineText(id: Int) {
		// Choose the text to show in the guide. It chooses in function of action in World (talk to villager, item taken)

		step = maxOf(id, step)

		if (step >= listTextGuide.size) {
			return
		}

		guideDialog.initDialog(listTextGuide[step])
	}

}
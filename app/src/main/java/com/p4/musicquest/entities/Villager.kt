package com.p4.musicquest.entities

import android.content.Context
import com.p4.musicquest.Animator
import com.p4.musicquest.Camera
import com.p4.musicquest.Entity
import com.p4.musicquest.Renderer
import com.p4.musicquest.Shader
import com.p4.musicquest.Sprite
import com.p4.musicquest.SpriteSheet
import com.p4.musicquest.Texture
import com.p4.musicquest.UI
import com.p4.musicquest.UIRefCorner
import com.p4.musicquest.World
import com.p4.musicquest.ui.Font
import com.p4.musicquest.ui.Text
import android.opengl.GLES30 as gl

class Villager (private val context: Context, private val player: Player?, world: World, pos: Array<Float>, val renderer: Renderer, val texPath: String = "textures/Dwarf.png")
	: Entity(world, Animator(getSpritesheet(context, texPath)), pos, .6f, 1f) {
	companion object {
		private var spritesheets: MutableMap<String, ArrayList<Sprite>> = mutableMapOf()

		fun getSpritesheet(context: Context, texPath: String): ArrayList<Sprite> {
			if (spritesheets[texPath] == null) {
				spritesheets[texPath] = SpriteSheet(context).getSpriteList(texPath)
			}

			return spritesheets[texPath]!!
		}
	}

	enum class ACTION {
		DIALOG,
		SHOP,
	}

	var popup = Sprite.getPopup(context)

	var showSignal = false // for the interaction popup
	var isInteract = false

	var idGuide = 0 // text to show in guide when we click on this villager

	// Text of the villager (default)

	private var textForDialog = "Les oiseaux volent bas aujourd'hui. \nIl va pleuvoir."
	var textDialog: Text? = null

	var villagerState = ACTION.DIALOG

	override fun update(dt: Float) {

		if (player != null) {
			isInteract = player.hurtBox.intersection(collider)

			if (isInteract && player.isAttack) {
				// start popup dialog when player interacts with villager
				showSignal = false

				// Show right text for the guide

				renderer.ui.guide.defineText(idGuide)

				when(villagerState) {

					ACTION.DIALOG -> {
						// Show dialog
						textDialog?.let { renderer.ui.dialog.initDialog(it) }
						renderer.ui.uiState = UI.UIState.DIALOG
					}

					ACTION.SHOP -> {
						renderer.ui.uiState = UI.UIState.SHOP
					}
				}
			}

		}
		super.update(dt)
	}

	fun createText() {
		val font = Font(renderer.ui, context, 75f)
		textDialog = Text(renderer.ui, font, textForDialog, UIRefCorner.TOP_CENTER, .1f, 0.1f, .8f)
	}

	fun changeTextDialog(text: String) {
		textForDialog = text
	}

	fun drawEntity(shader: Shader, camera: Camera){
		if (showSignal) {
			gl.glDisable(gl.GL_DEPTH_TEST)
			popup.draw(shader, camera, position[0] + 0.2f , 0.4f, position[2] + 0.5f)
			gl.glEnable(gl.GL_DEPTH_TEST)
		}
	}
}
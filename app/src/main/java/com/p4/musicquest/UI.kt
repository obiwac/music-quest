package com.p4.musicquest

import android.content.Context
import android.view.MotionEvent
import com.p4.musicquest.entities.Player
import com.p4.musicquest.inventory.Inventory
import com.p4.musicquest.inventory.InventoryItem
import com.p4.musicquest.ui.Button
import com.p4.musicquest.ui.ButtonAnimated
import com.p4.musicquest.ui.Dialog
import com.p4.musicquest.World
import com.p4.musicquest.ui.Element
import com.p4.musicquest.ui.Font
import android.opengl.GLES30 as gl
import com.p4.musicquest.ui.Heart
import com.p4.musicquest.ui.Joystick
import com.p4.musicquest.ui.Message
import com.p4.musicquest.ui.Shop
import com.p4.musicquest.ui.Text
import java.nio.ByteBuffer
import java.nio.IntBuffer
import java.nio.FloatBuffer

enum class UIRefCorner {
	TOP_LEFT, TOP_RIGHT,
	BOTTOM_LEFT, BOTTOM_RIGHT,
	TOP_CENTER, BOTTOM_CENTER,
	CENTER
}

class UI(val context: Context, val player: Player) {
	val vao: Int
	var xRes = 1
	var yRes = 1
	var aspect = 1f

	enum class UIState {
		MENU,
		PLAYING,
		DEAD,
		DIALOG,
		INVENTORY,
		GUIDE,
		SHOP,
	}

	var uiState = UIState.MENU
	private val font = Font(this, context, 10f)

	// Show a message

	val messageUI = Message(context, this)

	val messageList = ArrayList<String>()
	val messageCounter = ArrayList<Int>()

	// game UI

	private val heart = Heart(this, player)
	private val joystick = Joystick(this, player)

	// TODO texture here is temporary
	private val sword = Button(this, "ui/joystick-thumb.png", UIRefCorner.BOTTOM_RIGHT, .05f, .1f, 0.2f, 0.2f) {
		player.attackWithSword()
	}

	private val testText = Text(this, font, "Lorem Ipsum dolor\nsit amet. This line\nhere is a little\nlonger than the\nprevious. This\none is shorter.", UIRefCorner.TOP_LEFT, .1f, .5f, .8f)

	// menu UI

	var listAnimation = arrayListOf("ui/mainmenu_button_start_1.png","ui/mainmenu_button_start_2.png" )
	private val buttonStartAnim = ButtonAnimated(this, listAnimation, UIRefCorner.TOP_CENTER, .05f, 0.5f, 0.6f, 0.25f) {
		uiState = UIState.PLAYING
	}

	private val menuBackground = Element(this, "ui/mainmenu_menubackground.png", UIRefCorner.CENTER, 0.5f, 0f, 1f, 2f)

	// death screen UI

	private val buttonRestart = ButtonAnimated(this, listAnimation, UIRefCorner.TOP_CENTER, .05f, 0.5f, 0.6f, 0.25f) {
		player.resetPlayer()
		uiState = UIState.PLAYING
	}

	// dialog UI

	private val dialogBackground = Element(this, "ui/dialog_background.png", UIRefCorner.CENTER, 0.5f, 0f, 1f, 2f)
	val dialog = Dialog(context, this) {
		player.isAttack = false // eviter que le joueur spam le villageois
		uiState = UIState.PLAYING
	}

	// inventory UI

	var listAnimationInventory = arrayListOf("ui/inventory_button.png","ui/inventory_button2.png")
	private val inventoryButton = ButtonAnimated(this, listAnimationInventory, UIRefCorner.TOP_RIGHT,.05f, .1f, 0.2f, 0.2f) {

		// Open or close inventory
		if (uiState == UIState.INVENTORY) {
			uiState = UIState.PLAYING
		} else {
			uiState = UIState.INVENTORY
		}
	}
	// Guide quest
	private val guideButton = Button(this, "ui/button_guide.png", UIRefCorner.TOP_RIGHT,.05f, .3f, 0.2f, 0.2f) {

		// Open or close inventory
		if (uiState == UIState.GUIDE) {
			uiState = UIState.PLAYING
		} else {
			uiState = UIState.GUIDE
		}
	}
	val dialoog = Dialog(context, this) {
		println("ifckhatemylife")
	}


	var inventoryPlayer = Inventory(context, this, player)

	// shop UI

	val healthPotion = InventoryItem("potion de soin", "textures/potion_red.png", floatArrayOf(.25f, 0.3f, 0.2f, 0.2f)) {
		addMessage("potion de soin utilisé")
		player.health += 10
	}

	var shop = Shop(context, this, null, healthPotion)

	var listAnimationBuyButton = arrayListOf("ui/yes_button_1.png","ui/yes_button_2.png")
	var buyButton = ButtonAnimated(this, listAnimationBuyButton, UIRefCorner.TOP_CENTER, .05f, 1.15f, 0.6f, 0.25f) {

	}

	var listAnimationBackButton = arrayListOf("ui/button_back_1.png","ui/button_back_2.png")
	var backButton = ButtonAnimated(this, listAnimationBackButton, UIRefCorner.TOP_CENTER, .05f, 1.5f, 0.6f, 0.25f) {
		player.isAttack = false // eviter que le joueur spam le villageois
		uiState = UIState.PLAYING
	}

	init {
		val vertices = FloatBuffer.wrap(floatArrayOf(
			-.5f, -.5f, .5f, 1f, 0f,
			-.5f, +.5f, .5f, 0f, 0f,
			+.5f, +.5f, .5f, 0f, 1f,
			+.5f, -.5f, .5f, 1f, 1f,
		))

		val indices = ByteBuffer.wrap(byteArrayOf(
			0, 1, 2,
			0, 2, 3,
		))

		vertices.rewind()
		indices.rewind()

		// create vao

		val vaoBuf = IntBuffer.allocate(4)
		gl.glGenVertexArrays(1, vaoBuf)
		vao = vaoBuf[0]
		gl.glBindVertexArray(vao)

		// create VBO

		val vboBuf = IntBuffer.allocate(1)
		gl.glGenBuffers(1, vboBuf)
		val vbo = vboBuf[0]
		gl.glBindBuffer(gl.GL_ARRAY_BUFFER, vbo)

		gl.glBufferData(gl.GL_ARRAY_BUFFER, 4 * 4 * 5, vertices, gl.GL_STATIC_DRAW)

		gl.glVertexAttribPointer(0, 3, gl.GL_FLOAT, false, 4 * 5, 4 * 0)
		gl.glEnableVertexAttribArray(0)

		gl.glVertexAttribPointer(1, 2, gl.GL_FLOAT, false, 4 * 5, 4 * 3)
		gl.glEnableVertexAttribArray(1)

		// create IBO

		val iboBuf = IntBuffer.allocate(1)
		gl.glGenBuffers(1, iboBuf)
		val ibo = iboBuf[0]
		gl.glBindBuffer(gl.GL_ELEMENT_ARRAY_BUFFER, ibo)

		gl.glBufferData(gl.GL_ELEMENT_ARRAY_BUFFER, 6, indices, gl.GL_STATIC_DRAW)
	}

	fun updateResolution(xRes: Int, yRes: Int) {
		this.xRes = xRes
		this.yRes = yRes

		aspect = xRes.toFloat() / yRes
	}

	fun onTouchEvent(event: MotionEvent) {
		val x =   event.x / xRes * 2 - 1f
		val y = -(event.y / yRes * 2 - 1f)

		when(uiState) {
			UIState.MENU -> {
				buttonStartAnim.onTouchEvent(event, xRes.toFloat(), yRes.toFloat())
			}

			UIState.PLAYING -> {
				joystick.onTouchEvent(event, xRes.toFloat(), yRes.toFloat())
				sword.onTouchEvent(event, xRes.toFloat(), yRes.toFloat())
				inventoryButton.onTouchEvent(event, xRes.toFloat(), yRes.toFloat())
				guideButton.onTouchEvent(event, xRes.toFloat(), yRes.toFloat())
			}

			UIState.DEAD -> {
				buttonRestart.onTouchEvent(event, xRes.toFloat(), yRes.toFloat())
			}

			UIState.DIALOG -> {
				dialog.onTouchEvent(event, xRes.toFloat(), yRes.toFloat())
			}

			UIState.INVENTORY -> {
				inventoryButton.onTouchEvent(event, xRes.toFloat(), yRes.toFloat())
				inventoryPlayer.onTouchEvent(event, xRes.toFloat(), yRes.toFloat())
				guideButton.onTouchEvent(event, xRes.toFloat(), yRes.toFloat())
			}
			UIState.GUIDE -> {
				inventoryButton.onTouchEvent(event, xRes.toFloat(), yRes.toFloat())
				inventoryPlayer.onTouchEvent(event, xRes.toFloat(), yRes.toFloat())
				guideButton.onTouchEvent(event, xRes.toFloat(), yRes.toFloat())
			}

			UIState.SHOP -> {
				backButton.onTouchEvent(event, xRes.toFloat(), yRes.toFloat())

				buyButton.onClick = {
					shop.onTouchEvent(event, xRes.toFloat(), yRes.toFloat())
				}
				buyButton.onTouchEvent(event, xRes.toFloat(), yRes.toFloat())
			}
		}

	}

	fun draw(shader: Shader, dt: Float) {
		gl.glDisable(gl.GL_DEPTH_TEST)

		gl.glEnable(gl.GL_BLEND)
		gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA)

		gl.glBindVertexArray(vao)

		when(uiState) {
			UIState.MENU -> {
				menuBackground.draw(shader, dt)
				buttonStartAnim.draw(shader, dt)
			}

			UIState.PLAYING -> {
				heart.draw(shader, dt)
				joystick.draw(shader, dt)
				sword.draw(shader, dt)
				inventoryButton.draw(shader, dt)
				guideButton.draw(shader, dt)
			}

			UIState.DEAD -> {
				menuBackground.draw(shader, dt)
				buttonRestart.draw(shader, dt)

				// reset joystick
				joystick.thumb.targetX = Joystick.THUMB_INIT_X
				joystick.thumb.targetY = Joystick.THUMB_INIT_Y
				player.input[0] = 0f
				player.input[1] = 0f
			}

			UIState.DIALOG -> {
				dialogBackground.draw(shader, dt)

				// reset joystick and button

				joystick.thumb.targetX = Joystick.THUMB_INIT_X
				joystick.thumb.targetY = Joystick.THUMB_INIT_Y
				player.input[0] = 0f
				player.input[1] = 0f

				sword.pressing = false
				sword.buttonPointerId = -1

				// Draw text of the dialog

				dialog.draw(shader, dt)
			}

			UIState.INVENTORY -> {

				// reset joystick and button

				joystick.thumb.targetX = Joystick.THUMB_INIT_X
				joystick.thumb.targetY = Joystick.THUMB_INIT_Y
				player.input[0] = 0f
				player.input[1] = 0f

				sword.pressing = false
				sword.buttonPointerId = -1

				// Show inventory

				inventoryPlayer.background.draw(shader, dt)
				inventoryButton.draw(shader, dt)
				inventoryPlayer.draw(shader, dt)
				guideButton.draw(shader,dt)

			}

			UIState.GUIDE ->{
				inventoryPlayer.background.draw(shader, dt)
				inventoryButton.draw(shader, dt)
				dialoog.initDialog(World.AppConfig.guideText,100f)
				dialoog.draw(shader,dt)
				guideButton.draw(shader,dt)
			}

			UIState.SHOP -> {

				// reset joystick and button
				joystick.thumb.targetX = Joystick.THUMB_INIT_X
				joystick.thumb.targetY = Joystick.THUMB_INIT_Y
				player.input[0] = 0f
				player.input[1] = 0f

				sword.pressing = false
				sword.buttonPointerId = -1

            	shop.shopBackground.draw(shader, dt)
				buyButton.draw(shader, dt)
				backButton.draw(shader, dt)
				shop.draw(shader, dt)

			}
		}

		messageUI.draw(shader, dt)

	}

	fun addMessage(text: String) {

		messageList.add(text)
		messageCounter.add(0)
	}
}
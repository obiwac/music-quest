package com.p4.musicquest

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent

class Game(val context: Context) {

	// start game loop

	enum class GameState {
		MENU,
		PLAYING
	}

	var currentGameState = GameState.MENU

	val renderer = Renderer(context)

	fun onTouchEvent(event: MotionEvent): Boolean {
		when(currentGameState) {
			GameState.MENU -> renderer.uiMenu.onTouchEvents(event)
			GameState.PLAYING -> renderer.uiPlaying.onTouchEvent(event)
		}

		return true
	}

	fun changeRenderer() {
		println(currentGameState.toString())
		when(currentGameState) {
			GameState.MENU -> {renderer.rendererState = Renderer.RendererState.PLAYING}
			GameState.PLAYING -> {
				renderer.rendererState = Renderer.RendererState.PLAYING
				println(renderer.rendererState)
			}
		}
	}



}
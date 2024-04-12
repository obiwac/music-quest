package com.p4.musicquest

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GamePanel(context: Context): GLSurfaceView(context), GLSurfaceView.Renderer {

	var game = Game(context)

	override fun onTouchEvent(event: MotionEvent): Boolean {
		return game.onTouchEvent(event)
	}

	override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
	}

	override fun onSurfaceChanged(p0: GL10?, p1: Int, p2: Int) {
		TODO("Not yet implemented")
	}

	override fun onDrawFrame(p0: GL10?) {
		TODO("Not yet implemented")
	}


}
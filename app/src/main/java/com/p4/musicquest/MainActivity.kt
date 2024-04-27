package com.p4.musicquest

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import android.opengl.GLSurfaceView
import android.os.Bundle
//import en plus pour la musique
import android.view.MotionEvent
import android.view.WindowManager
import com.p4.musicquest.MusicManager
//pour la vie

import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class MainActivity : Activity() {
    private lateinit var view: GLSurfaceView
    private lateinit var renderer: Renderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        makeImmersive()

        renderer = Renderer(this)
        MusicManager.init(this)

        view = GLSurfaceView(this)
        view.setEGLContextClientVersion(2)
        view.setRenderer(renderer)

        // Disable screen rotation

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR

        setContentView(view)
    }

    private fun makeImmersive() {
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        window.decorView.setOnApplyWindowInsetsListener { view, windowInsets ->
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
            view.onApplyWindowInsets(windowInsets)
        }

        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        @SuppressLint("NewAPI") // XXX I fucking hate Android, more than React: https://stackoverflow.com/questions/49190381/fullscreen-app-with-displaycutout
        window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return true
        }

        renderer.ui.onTouchEvent(event)
        return true
    }

    override fun onResume() {
        super.onResume()
        MusicManager.resumeMusic()

    }

    override fun onPause() {
        super.onPause()
        MusicManager.pauseMusic()
    }

    override fun onDestroy() {
        super.onDestroy()
        MusicManager.stopMusic()
    }

}
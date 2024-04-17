package com.p4.musicquest

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer


@SuppressLint("StaticFieldLeak")
object MusicManager{
	private var mediaPlayer: MediaPlayer? = null
	private lateinit var context: Context

	fun init(context: Context){
		this.context = context
	}

	fun playMusic(resourceId: Int, loop: Boolean = true) {
		if (mediaPlayer == null) {
			mediaPlayer = MediaPlayer.create(context, resourceId).apply {
				isLooping = loop
				setOnCompletionListener {
					if (loop) it.start()
				}
				start()
			}
		} else {
			if (!mediaPlayer!!.isPlaying) {
				mediaPlayer!!.start()
			}
		}
	}

	fun stopMusic() {
		mediaPlayer?.stop()
		mediaPlayer?.release()
		mediaPlayer = null
	}

	fun pauseMusic() {
		if (mediaPlayer?.isPlaying == true) {
			mediaPlayer?.pause()
		}
	}

	fun resumeMusic() {
		if (mediaPlayer != null && !mediaPlayer!!.isPlaying) {
			mediaPlayer?.start()
		}
	}
}

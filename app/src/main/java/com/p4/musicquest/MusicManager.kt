package com.p4.musicquest

import android.content.Context
import android.media.MediaPlayer


class MusicManager(private val context: Context){
	var mediaPlayer : MediaPlayer?=null

	fun playMusic(resourceId: Int , loop : Boolean = true){
		stopMusic()

		mediaPlayer = MediaPlayer.create(context,resourceId).apply {
			isLooping = loop
			setOnCompletionListener {
				if (loop) it.start()
			}
			start()
		}
	}

	fun stopMusic(){
		if (mediaPlayer?.isPlaying == true){
			mediaPlayer?.pause()
		}
		mediaPlayer?.release()
		mediaPlayer=null

	}
	fun pauseMusic(){
		if (mediaPlayer?.isPlaying == true){
			mediaPlayer?.pause()
		}
	}
	fun resumeMusic(){
		if (mediaPlayer != null && !mediaPlayer!!.isPlaying){
			mediaPlayer?.start()
		}
	}
}

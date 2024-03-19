package com.p4.musicquest

import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.p4.musicquest.ui.theme.MusicQuestTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.manalkaff.jetstick.JoyStick
//import en plus pour la musique 
import android.media.MediaPlayer
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.ui.res.painterResource
import kotlin.math.sqrt

class MainActivity : ComponentActivity() {

    private lateinit var renderer: Renderer
    var mediaPlayer = MediaPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playMusic()
        setContent {
            MusicQuestTheme {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = {context ->
                        renderer = Renderer(context)
                        val surfaceView = GLSurfaceView(context)
                        surfaceView.setEGLContextClientVersion(3)
                        surfaceView.setRenderer(renderer)
                        surfaceView
                    },
                )
                Text(text = "418")
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                InstrumentButton(pianoMusic) {

                }
                InstrumentButton(guitareMusic) { playMusic(guitareMusic.audioResId) }
                InstrumentButton(fluteMusic) { playMusic(fluteMusic.audioResId) }
                InstrumentButton(trompetteMusic) { playMusic(trompetteMusic.audioResId) }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                //verticalArrangement = Arrangement.spacedBy(40.dp, Alignment.Bottom),
                modifier = Modifier
                    .width(200.dp)
                    .height(610.dp)
            ) {
                JoyStick(
                    Modifier.padding(0.dp),
                    size = 150.dp,
                    dotSize = 60.dp
                ){ x: Float, y: Float ->
                    if (renderer.player != null) { // wait until player is initialized
                        renderer.player!!.input[0] = x / 100
                        renderer.player!!.input[1] = y / 100
                    }
                }
            }
        }
    }

    // ici les fonction pour jouer la musique
    private fun playMusic(audioResId: Int) {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer = MediaPlayer.create(this, audioResId)
        mediaPlayer.isLooping = true // Jouer en boucle
        mediaPlayer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        color = Color.White,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MusicQuestTheme {
        Greeting("Android")
    }
}

@Composable
fun InstrumentButton(iconResId: Music, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .size(50.dp)
            .clickable { onClick.invoke() },
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = iconResId.iconResId),
            contentDescription = null,
            modifier = Modifier
                .padding(8.dp)
                .size(50.dp)

            // Espacement entre les images
        )

    }
}

@Preview(showBackground = true)
@Composable
fun InstrumentButtonPreview() {
    MusicQuestTheme {
        InstrumentButton(pianoMusic) { /* Pas besoin de faire quoi que ce soit ici pour la prévisualisation */ }
    }
}









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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.delay
//pour la vie
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import com.p4.musicquest.entities.Player

class MainActivity : ComponentActivity() {

    private var renderer: Renderer? = null
    val playerHealth = mutableIntStateOf(0)
    var mediaPlayer = MediaPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isRendererReady = mutableStateOf(false)

        setContent {
            MusicQuestTheme {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { context ->
                        renderer = Renderer(context)
                        val surfaceView = GLSurfaceView(context)
                        surfaceView.setEGLContextClientVersion(3)
                        surfaceView.setRenderer(renderer)

                        playMusic(pianoMusic.audioResId)
                        renderer?.setBottom()

                        isRendererReady.value = true
                        surfaceView
                    },
                )
                Text(text= "gros zizi")

                LaunchedEffect(true) {
                    while (renderer == null) {
                        delay(100)
                    }

                    // XXX I like pain

                    renderer?.let {
                        while (true) {
                            delay(10)
                            playerHealth.intValue = it.player?.health?.intValue ?: 0

                        }
                    }
                }


                if (isRendererReady.value) {
                    UI()
                }
            }
        }
    }


    @Composable
    fun UI() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Column pour le coeur
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                HealthIndicator()
            }

            // Column pour les boutons d'instrument

            InstrumentButton(pianoMusic) {
                playMusic(pianoMusic.audioResId)
                renderer?.setBottom()
            }
            InstrumentButton(guitareMusic) {
                playMusic(guitareMusic.audioResId)
                renderer?.setTop()
            }
            InstrumentButton(fluteMusic) {
                playMusic(fluteMusic.audioResId)
                renderer?.setLeft()
            }
            InstrumentButton(trompetteMusic) {
                playMusic(trompetteMusic.audioResId)
                renderer?.setRight()
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            //verticalArrangement = Arrangement.spacedBy(40.dp, Alignment.Bottom),
            modifier = Modifier
                .width(650.dp)
                .height(610.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                JoyStick(
                    Modifier.padding(horizontal = 20.dp),
                    size = 150.dp,
                    dotSize = 60.dp
                ){ x: Float, y: Float ->
                    if (renderer?.player != null) { // wait until player is initialized
                        renderer?.player!!.input[0] = x / 100
                        renderer?.player!!.input[1] = y / 100
                    }
                }
                var couldown by remember { mutableStateOf(false) }
                Button(onClick = {
                    if (!couldown) {
                        //renderer.shoot()
                        if (renderer!!.player != null) {
                            renderer!!.player!!.isAttack = true
                        }
                        couldown = true
                    }
                },
                    modifier = Modifier
                        .size(70.dp)
                        .padding(horizontal = 0.dp),
                    shape = CircleShape,
                    border= BorderStroke(2.dp, Color.White)
                ){
                    LaunchedEffect(couldown) {
                        if (couldown) {
                            delay(500)
                            couldown = false
                            if (renderer!!.player != null) {
                                renderer!!.player!!.isAttack = false
                            }

                        }

                    }

                }


                // couldown pour ne pas spammer l'attaque
                var cooldown by remember { mutableStateOf(false) }

                Button(onClick = {
                    if (!cooldown) {
                        renderer?.shoot()
                        couldown = true
                    }
                },
                    modifier = Modifier
                        .size(70.dp)
                        .padding(horizontal = 0.dp),
                    shape = CircleShape,
                    border= BorderStroke(2.dp, Color.White)
                ){
                    LaunchedEffect(cooldown) {
                        if (cooldown) {
                            delay(500)
                            cooldown = false
                        }
                    }
                }
            }

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

    @Composable
    fun HealthIndicator(modifier: Modifier = Modifier) {
        val health by remember { playerHealth }

        LaunchedEffect(playerHealth) {
            println("${playerHealth} ballz")
        }

        Box(modifier = modifier) {
            val icon =  Icons.Default.Favorite
            val color = calculateColor(health)
            Icon(
                icon,
                contentDescription = "Health Indicator",
                tint = color,
                modifier = Modifier.size(60.dp)
            )
        }
    }

    private fun calculateColor(health: Int): Color {

        return if(health > 16) {
            Color.Green
        }else if (health >8){
            Color.Yellow
        } else {
            Color.Red
        }
    }
    
    // ici les fonction pour jouer la musique
    private var isInBackground = false

    private fun playMusic(audioResId: Int) {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer = MediaPlayer.create(this,audioResId)
        mediaPlayer.isLooping = true // Jouer en boucle
        mediaPlayer.start()
    }

    override fun onPause() {
        super.onPause()
        isInBackground = true
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        if (isInBackground) {
            isInBackground = false
            mediaPlayer.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.delay
//pour la vie

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private var renderer: Renderer? = null
    val playerHealth = mutableIntStateOf(1)
    var mediaPlayer = MediaPlayer()
    var showEndGameImage by mutableStateOf(false)
    var endDialog by mutableStateOf(false)

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
                Text(text= "RTX ON")

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
    fun IsDead(life: MutableIntState) {
        if (life.intValue <= 0 ) {
            showEndGameImage()
        }
    }
    @Composable
    fun UI() {
        if (showEndGameImage && renderer != null) {
            Image(
                painter = painterResource(id = R.drawable.you_dead),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
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

                // popup text dialog
                var wantToDiscuss by remember { mutableStateOf(false) }
                var showDialog by remember { mutableStateOf(false) }

                LaunchedEffect(wantToDiscuss) {
                    if (wantToDiscuss) {
                        showDialog = true
                        endDialog = false
                    } else {
                        showDialog = false
                    }

                }

                if (showDialog) {
                    println("show message")
                    if (renderer!!.villager1 != null) {
                        DialogText(renderer!!.villager1!!.textForDialog)
                    }

                }

                LaunchedEffect(endDialog) {
                    if (endDialog) {
                        wantToDiscuss = false
                    }
                }

                Button(onClick = {
                    if (!couldown) {
                        //renderer.shoot()
                        if (renderer!!.player != null) {
                            renderer!!.player!!.isAttack = true

                            if (renderer!!.villager1!!.isInteract) {
                                println("want to dialog")
                                wantToDiscuss = true
                            } else {
                                wantToDiscuss = false
                            }
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
        var id: Int? = null;
        IsDead(playerHealth)
        if (health>18){id =R.drawable.heart_100}
        else if (health>15){id =R.drawable. heart_80}
        else if (health>13){id =R.drawable.heart_60}
        else if (health>9){id =R.drawable.heart_50}
        else if (health>5){id =R.drawable.heart_30}
        else {id =R.drawable.heart_20}
        Box(modifier = modifier) {
            Image(
                painter = painterResource(id),
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .size(80.dp)

                // Espacement entre les images
            )
        }
    }

    fun showEndGameImage() {
        if (renderer ==null)return
        showEndGameImage = true
        // Lancer une coroutine suspendue pour afficher l'image pendant 3 secondes
        CoroutineScope(Dispatchers.Main).launch {
            delay(3000) // Attendre pendant 3 secondes
            showEndGameImage = false // Masquer l'image après 3 secondes
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

    @Composable
    fun DialogText(textForDialog: String) {
        val shouldDismiss = remember {
            mutableStateOf(false)
        }

        if (shouldDismiss.value) return

        Dialog(onDismissRequest = {
            shouldDismiss.value = true
            endDialog = true
        }, properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp)
                    .clickable(onClick = { shouldDismiss.value = true }),
                shape = RoundedCornerShape(16.dp),
            ) {
                Text(
                    text = textForDialog,
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }

}

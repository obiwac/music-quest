package com.p4.musicquest

import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.p4.musicquest.ui.theme.MusicQuestTheme
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    private lateinit var renderer: Renderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            Row (
                modifier = Modifier
                    .padding(25.dp)
                    .height(540.dp),
                verticalAlignment = Alignment.Bottom,
            ){
                ArrowLeft()
                Spacer(modifier = Modifier.width(50.dp))
                ArrowRight()
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(40.dp, Alignment.Bottom),
                modifier = Modifier
                    .width(200.dp)
                    .height(610.dp)
            ) {
                ArrowUp()
                ArrowDown()
            }
        }
    }

    @Composable
    fun ArrowLeft() {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed by interactionSource.collectIsPressedAsState()

        Button(
            onClick = { },
            modifier = Modifier.size(50.dp),
            shape = CircleShape,
            border= BorderStroke(2.dp, Color.White),
            interactionSource = interactionSource // wut
        ) {
            Icon(
                Icons.Filled.KeyboardArrowLeft,
                contentDescription = "",
                modifier = Modifier.fillMaxHeight(1f).aspectRatio(100f),
                tint = Color.White
            )
            LaunchedEffect(isPressed) {
                when (isPressed) {
                    true -> renderer.camera.strafe[0] = -1
                    false -> renderer.camera.strafe[0] = 0
                }
            }
        }
    }

    @Composable
    fun ArrowRight() {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed by interactionSource.collectIsPressedAsState()

        Button(
            onClick = { },
            modifier = Modifier.size(50.dp),
            shape = CircleShape,
            border= BorderStroke(2.dp, Color.White),
            interactionSource = interactionSource // wut
        ) {
            Icon(
                Icons.Filled.KeyboardArrowRight,
                contentDescription = "",
                modifier = Modifier.fillMaxHeight(1f).aspectRatio(100f),
                tint = Color.White
            )
            LaunchedEffect(isPressed) {
                when (isPressed) {
                    true -> renderer.camera.strafe[0] = 1
                    false -> renderer.camera.strafe[0] = 0
                }
            }
        }
    }

    @Composable
    fun ArrowUp() {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed by interactionSource.collectIsPressedAsState()

        Button(
            onClick = { },
            modifier = Modifier.size(50.dp),
            shape = CircleShape,
            border= BorderStroke(2.dp, Color.White),
            interactionSource = interactionSource // wut
        ) {
            Icon(
                Icons.Filled.KeyboardArrowUp,
                contentDescription = "",
                modifier = Modifier.fillMaxHeight(1f).aspectRatio(100f),
                tint = Color.White
            )
            LaunchedEffect(isPressed) {
                when (isPressed) {
                    true -> renderer.camera.strafe[1] = 1
                    false -> renderer.camera.strafe[1] = 0
                }
            }
        }
    }

    @Composable
    fun ArrowDown() {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed by interactionSource.collectIsPressedAsState()

        Button(
            onClick = {  },
            modifier = Modifier.size(50.dp),
            shape = CircleShape,
            border= BorderStroke(2.dp, Color.White),
            interactionSource = interactionSource // wut
        ) {
            //Text(if (isPressed) "Pressed!" else "Not pressed")
            Icon(
                Icons.Filled.KeyboardArrowDown,
                contentDescription = "",
                modifier = Modifier.fillMaxHeight(1f).aspectRatio(100f),
                tint = Color.White
            )
            LaunchedEffect(isPressed) {
                when (isPressed) {
                    true -> renderer.camera.strafe[1] = -1
                    false -> renderer.camera.strafe[1] = 0
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






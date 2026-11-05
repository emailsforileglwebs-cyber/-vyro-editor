
package com.novx.vyro

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VyroEditor()
        }
    }
}

private val Background = Color(0xFF08090D)
private val Panel = Color(0xFF14161D)
private val Accent = Color(0xFF3978FF)
private val MainText = Color(0xFFF5F5F7)
private val SecondaryText = Color(0xFF888D9B)

@Composable
fun VyroEditor() {
    val context = LocalContext.current

    var videoUri by remember { mutableStateOf<Uri?>(null) }

    val player = remember {
        ExoPlayer.Builder(context).build()
    }

    val videoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            videoUri = uri
        }
    }

    LaunchedEffect(videoUri) {
        videoUri?.let { uri ->
            player.setMediaItem(MediaItem.fromUri(uri))
            player.prepare()
            player.playWhenReady = false
        }
    }

    DisposableEffect(player) {
        onDispose {
            player.release()
        }
    }

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(18.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "VYRO",
                    color = MainText,
                    fontSize = 27.sp,
                    letterSpacing = 4.sp
                )

                Text(
                    "STUDIO",
                    color = Accent,
                    fontSize = 12.sp,
                    letterSpacing = 2.sp
                )
            }

            Spacer(Modifier.height(24.dp))

            Text(
                "VIDEO EDITOR",
                color = SecondaryText,
                fontSize = 11.sp,
                letterSpacing = 2.sp
            )

            Spacer(Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Panel, RoundedCornerShape(18.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (videoUri != null) {
                    AndroidView(
                        factory = { ctx ->
                            PlayerView(ctx).apply {
                                this.player = player
                                useController = true
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "V",
                            color = Accent,
                            fontSize = 55.sp
                        )

                        Spacer(Modifier.height(10.dp))

                        Text(
                            "Your preview appears here",
                            color = MainText,
                            fontSize = 14.sp
                        )

                        Text(
                            "Import a video to get started",
                            color = SecondaryText,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    videoPicker.launch(arrayOf("video/*"))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Accent
                )
            ) {
                Text(
                    "IMPORT VIDEO",
                    letterSpacing = 2.sp
                )
            }

            Spacer(Modifier.height(20.dp))

            Text(
                "TIMELINE",
                color = SecondaryText,
                fontSize = 11.sp,
                letterSpacing = 2.sp
            )

            Spacer(Modifier.height(10.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                color = Panel,
                shape = RoundedCornerShape(14.dp)
            ) {
                Box(
                    modifier = Modifier.padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        if (videoUri != null)
                            "VIDEO LOADED"
                        else
                            "No clips yet",
                        color = if (videoUri != null)
                            Accent
                        else
                            SecondaryText,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("PROJECT", color = SecondaryText, fontSize = 11.sp)
                Text("VYRO 1.0", color = MainText, fontSize = 11.sp)
            }
        }
    }
}

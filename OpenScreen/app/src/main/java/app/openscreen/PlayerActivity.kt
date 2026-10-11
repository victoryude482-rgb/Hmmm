package app.openscreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class PlayerActivity : ComponentActivity() {
    private var player: ExoPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url = intent.getStringExtra("url")
        val title = intent.getStringExtra("title") ?: "Now playing"
        if (url.isNullOrBlank() || !url.startsWith("https://")) { finish(); return }
        player = ExoPlayer.Builder(this).build().apply { setMediaItem(MediaItem.fromUri(url)); prepare(); playWhenReady = true }
        setContent {
            MaterialTheme(colorScheme = darkColorScheme(background = Color(0xFF09090D), surface = Color(0xFF17171F), primary = Color(0xFFD9FF62))) {
                Column(Modifier.fillMaxSize().padding(12.dp)) {
                    Text(title, style = MaterialTheme.typography.titleMedium, color = Color.White)
                    Spacer(Modifier.height(8.dp))
                    AndroidView(factory = { context -> PlayerView(context).apply { player = this@PlayerActivity.player; useController = true } }, modifier = Modifier.fillMaxWidth().weight(1f))
                    Text("Playback works only for direct video URLs supplied by a rights holder.", color = Color.LightGray, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
    override fun onStop() { super.onStop(); player?.pause() }
    override fun onDestroy() { player?.release(); player = null; super.onDestroy() }
}
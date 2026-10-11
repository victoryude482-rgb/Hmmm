package app.openscreen

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Ink = Color(0xFF09090D)
private val Panel = Color(0xFF17171F)
private val Lime = Color(0xFFD9FF62)
private val Muted = Color(0xFFA4A3B1)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { OpenScreenApp() }
    }
}

@Composable
private fun OpenScreenApp() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("openscreen", Context.MODE_PRIVATE) }
    var query by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("All") }
    var page by remember { mutableStateOf("Home") }
    var selected by remember { mutableStateOf<Movie?>(null) }
    var refresh by remember { mutableIntStateOf(0) }
    val saved = remember(refresh) { prefs.getStringSet("watchlist", emptySet())?.toSet() ?: emptySet() }
    val recent = remember(refresh) { prefs.getStringSet("recent", emptySet())?.toList()?.reversed() ?: emptyList() }
    val genres = listOf("All", "Action", "Adventure", "Animation", "Comedy", "Drama", "Horror", "Sci-Fi")
    val visible = Movie.demo.filter { movie ->
        (genre == "All" || movie.genre.equals(genre, true)) &&
        (movie.title.contains(query, true) || movie.genre.contains(query, true) || movie.year.toString().contains(query)) &&
        (page != "Watchlist" || movie.id in saved) &&
        (page != "Recently watched" || movie.id in recent)
    }.let { list -> if (page == "Trending") list.sortedByDescending { it.rating.toFloatOrNull() ?: 0f } else list }

    MaterialTheme(colorScheme = darkColorScheme(primary = Lime, background = Ink, surface = Panel, onBackground = Color.White, onSurface = Color.White)) {
        Scaffold(
            containerColor = Ink,
            topBar = {
                Column(Modifier.fillMaxWidth().background(Ink).padding(horizontal = 18.dp, vertical = 14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(34.dp).clip(RoundedCornerShape(10.dp)).background(Lime), contentAlignment = Alignment.Center) {
                            Text("▶", color = Ink, fontWeight = FontWeight.Black)
                        }
                        Spacer(Modifier.width(10.dp))
                        Text("OPEN", color = Color.White, fontWeight = FontWeight.Black, fontSize = 21.sp)
                        Text("SCREEN", color = Lime, fontWeight = FontWeight.Black, fontSize = 21.sp)
                        Spacer(Modifier.weight(1f))
                        Icon(Icons.Default.Movie, contentDescription = null, tint = Lime)
                    }
                    Spacer(Modifier.height(16.dp))
                    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Panel).padding(horizontal = 12.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Search, contentDescription = null, tint = Muted)
                        Spacer(Modifier.width(8.dp))
                        Box(Modifier.weight(1f)) {
                            if (query.isEmpty()) Text("Search movies, genres, years...", color = Muted, fontSize = 13.sp)
                            BasicTextField(value = query, onValueChange = { query = it }, singleLine = true, textStyle = TextStyle(color = Color.White, fontSize = 13.sp), modifier = Modifier.fillMaxWidth())
                        }
                        if (query.isNotEmpty()) Icon(Icons.Default.Close, contentDescription = "Clear search", tint = Muted, modifier = Modifier.clickable { query = "" })
                    }
                    Spacer(Modifier.height(13.dp))
                    Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Home", "Trending", "Watchlist", "Recently watched").forEach { item ->
                            FilterChip(selected = page == item, onClick = { page = item }, label = { Text(item, fontSize = 12.sp) }, colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Lime, selectedLabelColor = Ink))
                        }
                    }
                }
            },
            bottomBar = {
                NavigationBar(containerColor = Panel) {
                    listOf("Home" to Icons.Default.Home, "Movies" to Icons.Default.Explore, "Watchlist" to Icons.Default.Favorite, "About" to Icons.Default.Info).forEach { (label, icon) ->
                        NavigationBarItem(selected = page == label || (label == "Movies" && page == "Trending"), onClick = { page = label }, icon = { Icon(icon, contentDescription = null) }, label = { Text(label, fontSize = 10.sp) }, colors = NavigationBarItemDefaults.colors(selectedIconColor = Lime, selectedTextColor = Lime, indicatorColor = Color(0xFF303321), unselectedIconColor = Muted, unselectedTextColor = Muted))
                    }
                }
            }
        ) { padding ->
            when (page) {
                "About" -> Column(Modifier.padding(padding).padding(22.dp)) {
                    Text("About OpenScreen", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))
                    Text("Discover movies, save a watchlist, and find official trailers and licensed viewing sources.", color = Muted)
                    Spacer(Modifier.height(16.dp))
                    Text("This app does not host copyrighted movies. Streaming and downloads are available only from links whose rights and terms permit them. Some links open the official source in your browser.", color = Muted)
                    Spacer(Modifier.height(14.dp))
                    Text("The prototype uses a small demo catalog. Live movie metadata can be added after setting up a provider and reviewing its terms.", color = Lime)
                    TextButton(onClick = { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.themoviedb.org/"))) }) { Text("Visit TMDB") }
                }
                else -> LazyColumn(contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(padding)) {
                    item {
                        if (page == "Home") {
                            Text("YOUR NEXT MOVIE NIGHT", color = Lime, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                            Spacer(Modifier.height(8.dp))
                            Text("Stories worth staying in for.", fontSize = 27.sp, lineHeight = 31.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Spacer(Modifier.height(6.dp))
                            Text("Discover films, open movies and official trailers.", fontSize = 13.sp, color = Muted)
                            Spacer(Modifier.height(18.dp))
                        } else {
                            Text(page, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))
                        }
                        Text("BROWSE BY GENRE", color = Muted, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
                        Spacer(Modifier.height(8.dp))
                        Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                            genres.forEach { item ->
                                FilterChip(selected = genre == item, onClick = { genre = item }, label = { Text(item, fontSize = 11.sp) }, colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Lime, selectedLabelColor = Ink))
                            }
                        }
                        Spacer(Modifier.height(10.dp))
                        Text(if (page == "Trending") "TOP RATED" else if (page == "Watchlist") "SAVED FOR LATER" else if (page == "Recently watched") "PICK UP WHERE YOU LEFT OFF" else "DISCOVER", color = Lime, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
                    }
                    if (visible.isEmpty()) item { Text("Nothing here yet. Try another genre or save a movie to your watchlist.", color = Muted, modifier = Modifier.padding(vertical = 28.dp)) }
                    items(visible, key = { it.id }) { movie ->
                        MovieRow(movie = movie, saved = movie.id in saved, onClick = {
                            val nextRecent = (prefs.getStringSet("recent", emptySet()) ?: emptySet()).toMutableSet()
                            nextRecent.remove(movie.id)
                            nextRecent.add(movie.id)
                            prefs.edit().putStringSet("recent", nextRecent).apply()
                            refresh++
                            selected = movie
                        }, onSave = {
                            val next = saved.toMutableSet()
                            if (movie.id in next) next.remove(movie.id) else next.add(movie.id)
                            prefs.edit().putStringSet("watchlist", next).apply()
                            refresh++
                        })
                    }
                    item {
                        Spacer(Modifier.height(8.dp))
                        Text("Catalog entries are demo data. Verify a source's license and download terms before using or sharing any film.", color = Muted, fontSize = 10.sp, lineHeight = 15.sp)
                    }
                }
            }
        }
        selected?.let { movie ->
            AlertDialog(
                onDismissRequest = { selected = null },
                containerColor = Panel,
                title = { Text(movie.title, fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text("${movie.year}  •  ${movie.genre}  •  ★ ${movie.rating}", color = Lime, fontSize = 12.sp)
                        Spacer(Modifier.height(10.dp))
                        Text(movie.overview, color = Muted, fontSize = 13.sp)
                        Spacer(Modifier.height(12.dp))
                        Text(if (movie.openLicensed) "Open-license project: check the official page for exact attribution and download terms." else "Rights may vary by edition and region. Verify the rights holder before downloading.", color = Muted, fontSize = 11.sp)
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (movie.streamUrl != null) {
                            context.startActivity(Intent(context, PlayerActivity::class.java).putExtra("url", movie.streamUrl).putExtra("title", movie.title))
                        } else {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(movie.watchUrl)))
                        }
                        selected = null
                    }) { Text(if (movie.streamUrl != null) "Play sample" else "Open source", color = Lime) }
                },
                dismissButton = {
                    Row {
                        TextButton(onClick = { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(movie.trailerUrl))) }) { Text("Trailer", color = Lime) }
                        if (movie.downloadUrl != null && movie.openLicensed) TextButton(onClick = { enqueueDownload(context, movie); selected = null }) { Text("Download", color = Lime) }
                    }
                }
            )
        }
    }
}

@Composable
private fun MovieRow(movie: Movie, saved: Boolean, onClick: () -> Unit, onSave: () -> Unit) {
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(Panel).clickable(onClick = onClick).padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.width(80.dp).height(112.dp).clip(RoundedCornerShape(9.dp)).background(Color(0xFF282833)), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text("▶", color = Lime, fontSize = 24.sp)
            Text(movie.genre.uppercase(), color = Muted, fontSize = 8.sp, letterSpacing = 1.sp)
        }
        Spacer(Modifier.width(13.dp))
        Column(Modifier.weight(1f)) {
            Text(movie.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(Modifier.height(5.dp))
            Text("${movie.year}  •  ${movie.genre}  •  ★ ${movie.rating}", fontSize = 10.sp, color = Lime)
            Spacer(Modifier.height(8.dp))
            Text(movie.overview, fontSize = 11.sp, lineHeight = 15.sp, color = Muted, maxLines = 3)
            Spacer(Modifier.height(7.dp))
            Text(if (movie.streamUrl != null) "▶ PLAYABLE SAMPLE" else "OFFICIAL SOURCE ↗", color = Muted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        }
        IconButton(onClick = onSave) {
            Icon(if (saved) Icons.Default.Favorite else Icons.Default.FavoriteBorder, contentDescription = "Toggle watchlist", tint = if (saved) Lime else Muted)
        }
    }
}

private fun enqueueDownload(context: Context, movie: Movie) {
    val url = movie.downloadUrl ?: return
    if (!movie.openLicensed || !url.startsWith("https://")) return
    val request = DownloadManager.Request(Uri.parse(url))
        .setTitle(movie.title)
        .setDescription("Open-license sample movie download")
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "OpenScreen-${movie.title.replace(" ", "-")}.mp4")
        .setAllowedOverMetered(false)
        .setAllowedOverRoaming(false)
    (context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager).enqueue(request)
}

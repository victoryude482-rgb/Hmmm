package app.openscreen

data class Movie(
    val id: String,
    val title: String,
    val year: Int,
    val genre: String,
    val rating: String,
    val overview: String,
    val watchUrl: String,
    val streamUrl: String? = null,
    val downloadUrl: String? = null,
    val openLicensed: Boolean = false
) {
    val trailerUrl: String get() = "https://www.youtube.com/results?search_query=" + title.trim().replace(" ", "+") + "+official+trailer"
    companion object {
        val demo = listOf(
            Movie("bbb","Big Buck Bunny",2008,"Animation","7.5","An open animated short from the Blender Foundation. Review the project page for its Creative Commons license and attribution.","https://peach.blender.org/", "https://storage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4", "https://storage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4", true),
            Movie("sintel","Sintel",2010,"Adventure","7.4","An open movie from the Blender Foundation. Check the official page for current license and download details.","https://durian.blender.org/", openLicensed = true),
            Movie("tears","Tears of Steel",2012,"Sci-Fi","7.0","A science-fiction short produced by the Blender Foundation and released as an open movie.","https://mango.blender.org/", openLicensed = true),
            Movie("cosmos","Cosmos Laundromat",2015,"Animation","7.2","An imaginative open animation project from the Blender Institute.","https://studio.blender.org/films/cosmos-laundromat/", openLicensed = true),
            Movie("metropolis","Metropolis",1927,"Sci-Fi","8.3","A pioneering silent science-fiction film. Rights for restored editions may differ by country.","https://archive.org/search?query=Metropolis+1927+film"),
            Movie("trip","A Trip to the Moon",1902,"Adventure","8.0","An influential fantasy short. Some restored editions may have separate rights.","https://archive.org/search?query=A+Trip+to+the+Moon+1902"),
            Movie("kid","The Kid",1921,"Comedy","8.2","Charlie Chaplin's silent comedy-drama. Verify the edition's rights before downloading or redistributing.","https://archive.org/search?query=Charlie+Chaplin+The+Kid+1921"),
            Movie("livingdead","Night of the Living Dead",1968,"Horror","7.8","An independent horror classic. Confirm territorial status and edition rights before use.","https://archive.org/search?query=Night+of+the+Living+Dead+1968")
        )
    }
}
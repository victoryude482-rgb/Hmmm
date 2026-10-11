# OpenScreen — Native Android movie discovery

OpenScreen is a Kotlin + Jetpack Compose Android app starter. It includes search, genre filters, a top-rated/trending view, local watchlist, recently opened titles, movie details, YouTube trailer search, Media3 video playback for a configured direct sample, and Android DownloadManager support for that sample.

## Current prototype
- Works without a paid API key or backend.
- Contains a small demo catalog; it is not a live catalog of all films currently streaming.
- Some entries open official project pages or Internet Archive search results. Those links do not guarantee a title is available for streaming or downloading.
- One open-movie sample is configured for direct playback and download. Check its current license, host terms, and attribution requirements before redistributing the sample or building on it.
- No DRM bypass, MovieBox scraping, or unauthorized copyrighted-film hosting is included.
- The project source has not yet been compiled or tested on a physical Android device.

## Build an APK using GitHub Actions
The workflow lives at the repository root in .github/workflows/openscreen-build.yml and compiles the project in the OpenScreen folder.

1. Open the repository's **Actions** tab.
2. Select **Build OpenScreen APK**.
3. Tap **Run workflow** and confirm.
4. Wait for the run to finish, then open the run and download the **OpenScreen-debug-apk** artifact.
5. Extract the ZIP on your Android phone and tap the APK to install. Android may ask you to allow installs from your browser or file manager.

The current source is stored in the OpenScreen/ folder of the Hmmm repository because the connected GitHub actions available to this chat do not provide repository creation. To meet the requested repository name, create a new GitHub repository named OpenScreen and move the contents of this folder to its root, including the root .github/workflows/openscreen-build.yml workflow. Alternatively, rename the current repository from GitHub Settings if you do not need to keep its existing project name.

## Build locally
Open this folder as a Gradle project in Android Studio with JDK 17 and Android SDK Platform 36 installed. Run the app configuration on an Android phone or emulator.

## Planned improvements
- Connect a live movie metadata provider after configuring its key and attribution.
- Add real source availability and territory checks.
- Improve poster artwork, player error handling, download progress and cancellation.
- Add tests and a signed release build.

## Data attribution
If TMDB data is added, review the current API terms and show the required attribution. TMDB is a metadata provider, not a source of full movie streams.
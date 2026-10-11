# Cinebox 🎬

A responsive, MovieBox-inspired movie discovery web app starter built with plain HTML, CSS, and JavaScript. Designed to run easily from an Android phone or be hosted as a static website.

## Included
- Dark cinematic interface with responsive mobile layout
- Movie cards, ratings, genres, and search
- Trending and watchlist views
- Save/remove titles using browser local storage
- Movie details modal and YouTube trailer search
- No build step, paid API key, or package installation required

## Run it
Open `index.html` in a browser, or serve the folder with any static web server. For a public website, enable GitHub Pages in the repository's **Settings → Pages** and select the main branch and root folder.

## Movie content and licensing
This starter is a discovery interface, not a film-hosting service. Trailer actions open YouTube search results. It does not scrape MovieBox or provide unauthorized copyrighted streams/downloads. Before launch, replace sample metadata and poster references with data from a permitted provider and link playback only to official or licensed sources.

## Optional next steps
1. Connect a movie metadata API such as TMDB using its current API terms and required attribution.
2. Add official free streaming providers and region-aware availability.
3. Add a service worker and web app manifest for installable PWA behavior.
4. Add authentication and cloud-synced watchlists if needed.

## Project files
- `index.html` — page structure
- `styles.css` — visual design and responsive layout
- `app.js` — sample catalog and app interactions

This is an early prototype; the sample movie list is local demo data and not a live catalog of currently streamable films.
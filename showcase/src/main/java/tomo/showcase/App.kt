package tomo.showcase

import android.app.Activity
import android.app.Application
import tomo.Tomo
import tomo.showcase.data.Movie
import tomo.showcase.data.MovieId
import tomo.showcase.data.TMDBService

class App : Application() {
    val service by lazy { TMDBService(BuildConfig.TMDB_API_KEY) }
    var moviesCache = mapOf<MovieId, Movie>()

    override fun onCreate() {
        super.onCreate()

        Tomo.initialize(this, log = true)
    }
}

val Activity.app
    get() = (application as App)
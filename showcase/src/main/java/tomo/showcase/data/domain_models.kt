package tomo.showcase.data

import java.io.Serializable
import java.net.URL

data class Movie(
    val movieId: MovieId,
    val title: MovieTitle,
    val score: MovieScore,
    val overview: MovieOverview,
    val releaseDate: ReleaseDate,
    val posterUrl: PosterUrl,
    val backdropUrl: BackdropUrl,
    val genres: List<Genre>
) : Serializable

inline class MovieId(val value: Int)
inline class MovieTitle(val value: String)
data class MovieScore(val value: Int) : Serializable {
    init {
        require(value in 0..10) { "Score must be between [0, 10]" }
    }
}

inline class MovieOverview(val value: String)
inline class PosterUrl(val value: URL)
inline class BackdropUrl(val value: URL)
data class ReleaseDate(val year: Int, val month: Int, val day: Int) : Serializable

enum class Genre : Serializable {
    Action,
    Adventure,
    Animation,
    Comedy,
    Crime,
    Documentary,
    Drama,
    Family,
    Fantasy,
    History,
    Horror,
    Music,
    Mystery,
    Romance,
    SciFi,
    TV,
    Thriller,
    War,
    Western
}


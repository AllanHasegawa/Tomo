package tomo.showcase.data

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.net.URL
import kotlin.math.roundToInt

class TMDBService(private val apiKey: String) {
    private val service = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(API::class.java)

    fun getPopularMovies(onSuccess: (List<Movie>) -> Unit, onError: (Throwable) -> Unit) {
        service.getPopularMovies(apiKey)
            .enqueue(object : Callback<PopularMoviesResponseMoshi> {
                override fun onFailure(call: Call<PopularMoviesResponseMoshi>, t: Throwable) {
                    onError(t)
                }

                override fun onResponse(
                    call: Call<PopularMoviesResponseMoshi>,
                    response: Response<PopularMoviesResponseMoshi>
                ) {
                    if (response.isSuccessful) {
                        println(response.body()!!.results)
                        onSuccess(
                            response.body()!!.results.map { it.toDomain() }
                        )
                    }
                }
            })
    }
}

private fun String.tmdbPathToFullPath() = "https://image.tmdb.org/t/p/w500" + this

private data class PopularMoviesResponseMoshi(val results: List<PopularMovieMoshi>)

private data class PopularMovieMoshi(
    val id: Int,
    val title: String,
    val vote_average: Float,
    val overview: String,
    val poster_path: String,
    val backdrop_path: String,
    val genre_ids: List<Int>,
    val release_date: String
) {
    fun toDomain() =
        Movie(
            movieId = MovieId(id),
            title = MovieTitle(title),
            score = MovieScore(vote_average.roundToInt()),
            overview = MovieOverview(overview),
            posterUrl = PosterUrl(URL(poster_path.tmdbPathToFullPath())),
            backdropUrl = BackdropUrl(URL(backdrop_path.tmdbPathToFullPath())),
            genres = genre_ids.genreIdsToDomain(),
            releaseDate = release_date.parseReleaseDate()
        )
}

private interface API {
    @GET("movie/popular")
    fun getPopularMovies(@Query("api_key") apiKey: String): Call<PopularMoviesResponseMoshi>
}

private fun List<Int>.genreIdsToDomain() = mapNotNull(::genreIdToDomain)

private fun String.parseReleaseDate(): ReleaseDate {
    val split = split("-")
    if (split.size != 3) throw IllegalArgumentException("Release date format unknown for: $this")

    return ReleaseDate(year = split[0].toInt(), month = split[1].toInt(), day = split[2].toInt())
}

private fun genreIdToDomain(genreId: Int) = when (genreId) {
    28 -> Genre.Action
    12 -> Genre.Adventure
    16 -> Genre.Animation
    35 -> Genre.Comedy
    80 -> Genre.Crime
    99 -> Genre.Documentary
    18 -> Genre.Drama
    10751 -> Genre.Family
    14 -> Genre.Fantasy
    36 -> Genre.History
    27 -> Genre.Horror
    10402 -> Genre.Music
    9648 -> Genre.Mystery
    10749 -> Genre.Romance
    878 -> Genre.SciFi
    10770 -> Genre.TV
    53 -> Genre.Thriller
    10752 -> Genre.War
    37 -> Genre.Western
    else -> null
}
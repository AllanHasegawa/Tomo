package tomo.showcase.detailsScreen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.layout_details_content.*
import tomo.Tomo
import tomo.showcase.PicassoTomoBackgroundGenerator
import tomo.showcase.R
import tomo.showcase.app
import tomo.showcase.common.handleCommonMenuClick
import tomo.showcase.common.inflateCommonMenu
import tomo.showcase.common.post
import tomo.showcase.data.Genre
import tomo.showcase.data.Movie
import tomo.showcase.data.MovieId
import tomo.showcase.data.ReleaseDate
import java.text.SimpleDateFormat

class DetailsActivity : AppCompatActivity() {
    companion object {
        private const val BKEY_MOVIE_ID = "details_movie_key"

        fun launchIntent(context: Context, movieId: MovieId): Intent =
            Intent(context, DetailsActivity::class.java)
                .putExtra(BKEY_MOVIE_ID, movieId.value)
    }

    private lateinit var movie: Movie

    private val viewsToReveal by lazy {
        listOf(
            detailsBackdropIv,
            detailsOverBackgroundIv,
            detailsTitleTv,
            detailsScoreLl,
            detailsOverviewTv,
            detailsGenreLabelTv,
            detailsGenreTv,
            detailsReleaseDateLabelTv,
            detailsReleaseDateTv
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        supportPostponeEnterTransition()

        val movieId = MovieId(intent.getIntExtra(BKEY_MOVIE_ID, -1))
        movie = app.moviesCache[movieId] ?: return

        hideViews()
        setupBackdropAndBackground(onFinish = ::revealViews)
        setupPoster(onFinish = { supportStartPostponedEnterTransition() })
        setupContent()
        setupToolbar()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        inflateCommonMenu(this, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (handleCommonMenuClick(this, item)) return true

        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        supportFinishAfterTransition()
    }

    private fun setupPoster(onFinish: () -> Unit) {
        Picasso.get()
            .load(movie.posterUrl.value.toString())
            .noFade()
            .fit()
            .into(detailsPosterIv, object : Callback {
                override fun onSuccess() {
                    onFinish()
                }

                override fun onError(e: java.lang.Exception) {
                    onFinish()
                }
            })
    }

    private fun setupContent() {
        detailsTitleTv.text = movie.title.value
        detailsOverviewTv.text = movie.overview.value
        detailsReleaseDateTv.text = movie.releaseDate.toDisplayText()
        detailsGenreTv.text = movie.genres.toDisplayText()

        renderScore(numberOfFullStars = movie.score.value / 2, showAHalfStar = movie.score.value % 2 == 1)
    }

    private fun setupBackdropAndBackground(onFinish: () -> Unit) {
        post {
            Tomo.applyAdaptiveBackgroundGenerator(detailsInitialBackgroundIv, darkTheme = true)
        }

        Picasso.get()
            .load(movie.backdropUrl.value.toString())
            .centerCrop()
            .fit()
            .into(detailsBackdropIv)

        Picasso.get()
            .load(movie.backdropUrl.value.toString())
            .fit()
            .noFade()
            .transform(PicassoTomoBackgroundGenerator(darkTheme = true))
            .into(detailsOverBackgroundIv, object : Callback {
                override fun onSuccess() = onFinish()
                override fun onError(e: Exception?) = Unit
            })
    }

    private fun setupToolbar() {
        setSupportActionBar(detailsToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }

    @SuppressLint("SimpleDateFormat")
    private fun ReleaseDate.toDisplayText(): String {
        val asString = "$year-$month-$day"
        val simpleDataFormat = SimpleDateFormat("yyyy-MM-dd")
        val date = simpleDataFormat.parse(asString)
        return DateFormat.getLongDateFormat(this@DetailsActivity).format(date)
    }

    private fun List<Genre>.toDisplayText() = joinToString(", ") { it.name }

    private fun hideViews() {
        viewsToReveal.forEach { it.alpha = 0f }
    }

    private fun revealViews() {
        viewsToReveal.forEach { view ->
            view.alpha = 0f
            view.animate()
                .alpha(1f)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .setDuration(800L)
                .start()
        }
    }

    private fun renderScore(numberOfFullStars: Int, showAHalfStar: Boolean) {
        detailsScoreLl.removeAllViews()
        val inflateInScoreLL = { id: Int ->
            val view = layoutInflater.inflate(R.layout.layout_poster_score_icon, detailsScoreLl, false)

            view.findViewById<ImageView>(R.id.posterScoreIconIv)
                .setImageResource(id)

            detailsScoreLl.addView(view)
        }
        (0 until numberOfFullStars).forEach { _ ->
            inflateInScoreLL(R.drawable.ic_star_filled)
        }
        if (showAHalfStar) {
            inflateInScoreLL(R.drawable.ic_star_half)
        }
    }
}

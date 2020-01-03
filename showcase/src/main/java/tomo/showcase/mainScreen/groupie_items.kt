package tomo.showcase.mainScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_hi.*
import kotlinx.android.synthetic.main.item_poster.*
import tomo.showcase.R
import tomo.showcase.common.windowSize
import tomo.showcase.data.Genre
import tomo.showcase.data.Movie
import tomo.showcase.data.MovieId
import kotlin.math.roundToInt

class HiItem : Item(0) {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        with(viewHolder) {
            hiPopcornView.scaleViewUp()
                .setStartDelay(2_000L)
                .start()
        }
    }

    override fun getLayout(): Int = R.layout.item_hi

    override fun equals(other: Any?): Boolean = other is HiItem

    override fun hashCode(): Int = 0

    fun View.scaleViewUp(): ViewPropertyAnimator {
        alpha = 1f
        scaleX = 0f
        scaleY = 0f

        return animate()
            .scaleX(1f)
            .scaleY(1f)
            .setInterpolator(OvershootInterpolator())
            .setDuration(1_000L)
    }
}

class PosterItem(private val movie: Movie, private val onClick: (MovieId, View) -> Unit) : Item() {
    companion object {
        private const val POSTER_ASPECT_RATIO = 27.0 / 40.0
        private const val POSTER_WIDTH_PERCENT = 0.4
        private const val MAX_GENRE_ICONS = 5
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) = with(viewHolder) {
        posterCard.setOnClickListener { onClick(movie.movieId, posterIv) }

        posterTitleTv.text = movie.title.value

        renderPoster()
        cacheBackdrop()

        val inflater = LayoutInflater.from(itemView.context)
        renderGenreIcons(inflater)

        val score = movie.score.value
        renderScore(inflater, numberOfFullStars = score / 2, showAHalfStar = score % 2 != 0)

        revealInfoWithAnimation()
    }

    override fun unbind(holder: GroupieViewHolder) {
        super.unbind(holder)
        holder.posterBackground.clearAnimation()
    }

    private fun cacheBackdrop() {
        Picasso.get()
            .load(movie.backdropUrl.value.toString())
            .fetch()
    }

    private fun GroupieViewHolder.revealInfoWithAnimation() {
        posterBackground.post {
            posterBackground.translationX = -(posterBackground.width).toFloat()

            posterBackground.animate()
                .translationX(0f)
                .setStartDelay(200L)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .start()
        }
    }

    private fun GroupieViewHolder.renderPoster() {
        val screenWidthPx = itemView.windowSize().x
        val posterWidthPx = screenWidthPx * POSTER_WIDTH_PERCENT
        val posterHeightPx = posterWidthPx / POSTER_ASPECT_RATIO

        posterIv.layoutParams = posterIv.layoutParams.apply {
            width = posterWidthPx.roundToInt()
            height = posterHeightPx.roundToInt()
        }

        Picasso.get()
            .load(movie.posterUrl.value.toString())
            .placeholder(R.drawable.poster_placeholder)
            .fit()
            .into(posterIv)
    }

    private fun GroupieViewHolder.renderGenreIcons(inflater: LayoutInflater): LayoutInflater {
        posterGenreLl.removeAllViews()
        movie.genres.map(::genreToDrawableId).take(MAX_GENRE_ICONS).forEach { drawableId ->
            val view = inflater.inflate(R.layout.layout_poster_genre_icon, posterGenreLl, false)

            view.findViewById<ImageView>(R.id.posterGenreIconIv)
                .setImageResource(drawableId)

            posterGenreLl.addView(view)
        }
        return inflater
    }

    private fun GroupieViewHolder.renderScore(
        inflater: LayoutInflater,
        numberOfFullStars: Int,
        showAHalfStar: Boolean
    ) {
        posterScoreLl.removeAllViews()
        val inflateInScoreLL = { id: Int ->
            val view = inflater.inflate(R.layout.layout_poster_score_icon, posterGenreLl, false)

            view.findViewById<ImageView>(R.id.posterScoreIconIv)
                .setImageResource(id)

            posterScoreLl.addView(view)
        }
        (0 until numberOfFullStars).forEach { _ ->
            inflateInScoreLL(R.drawable.ic_star_filled)
        }
        if (showAHalfStar) {
            inflateInScoreLL(R.drawable.ic_star_half)
        }
    }

    override fun getLayout(): Int = R.layout.item_poster

    override fun isSameAs(other: com.xwray.groupie.Item<*>): Boolean =
        (other as? PosterItem)?.movie?.movieId == movie.movieId

    override fun equals(other: Any?): Boolean = isSameAs(other as com.xwray.groupie.Item<*>)

    override fun hashCode(): Int = movie.movieId.hashCode()
}

class SpaceItem : Item(1) {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) = Unit
    override fun getLayout(): Int = R.layout.item_space
}

private fun genreToDrawableId(genre: Genre) = when (genre) {
    Genre.Action -> R.drawable.ic_action
    Genre.Adventure -> R.drawable.ic_adventure
    Genre.Animation -> R.drawable.ic_animation
    Genre.Comedy -> R.drawable.ic_comedy
    Genre.Crime -> R.drawable.ic_crime
    Genre.Documentary -> R.drawable.ic_documentary
    Genre.Drama -> R.drawable.ic_drama
    Genre.Family -> R.drawable.ic_family
    Genre.Fantasy -> R.drawable.ic_fantasy
    Genre.History -> R.drawable.ic_history
    Genre.Horror -> R.drawable.ic_horror
    Genre.Music -> R.drawable.ic_music
    Genre.Mystery -> R.drawable.ic_mystery
    Genre.Romance -> R.drawable.ic_romance
    Genre.SciFi -> R.drawable.ic_scifi
    Genre.TV -> R.drawable.ic_tv
    Genre.Thriller -> R.drawable.ic_thriller
    Genre.War -> R.drawable.ic_war
    Genre.Western -> R.drawable.ic_western
}
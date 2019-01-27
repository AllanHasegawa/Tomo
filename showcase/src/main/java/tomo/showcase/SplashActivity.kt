package tomo.showcase

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_splash.*
import tomo.Tomo
import tomo.showcase.common.post
import tomo.showcase.data.Movie
import tomo.showcase.mainScreen.MainActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // When the view is just created, the ImageView has zero width and height, therefore,
        // making it unable for Tomo to properly process it.
        // Using a post here delays this computation to on the next frame, when the ImageView
        // has been properly loaded.
        post {
            Tomo.applyAdaptiveBackgroundGenerator(splashBackgroundIv, darkTheme = true)
        }

        app.service.getPopularMovies(
            onSuccess = ::handleMoviesLoaded,
            onError = { Log.e("TomoShowcase", "Error getting posters", it) })
    }

    private fun handleMoviesLoaded(list: List<Movie>) {
        app.moviesCache = list.map { it.movieId to it }.toMap()

        splashTomoViewTv.scaleViewUpThenVanish()
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }
            })
            .start()
    }

    private fun View.scaleViewUpThenVanish(): ViewPropertyAnimator {
        alpha = 1f
        scaleX = 1f
        scaleY = 1f

        return animate()
            .alpha(0f)
            .scaleX(10f)
            .scaleY(10f)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setDuration(800L)
    }
}

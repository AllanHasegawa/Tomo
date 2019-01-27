package tomo.showcase.mainScreen

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import jp.wasabeef.recyclerview.animators.LandingAnimator
import kotlinx.android.synthetic.main.activity_main.*
import tomo.Tomo
import tomo.showcase.data.MovieId
import tomo.showcase.R
import tomo.showcase.app
import tomo.showcase.common.handleCommonMenuClick
import tomo.showcase.common.inflateCommonMenu
import tomo.showcase.detailsScreen.DetailsActivity
import tomo.showcase.common.post

class MainActivity : AppCompatActivity() {
    private val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainRv.layoutManager = LinearLayoutManager(this)
        mainRv.adapter = adapter
        mainRv.itemAnimator = LandingAnimator().apply {
            addDuration = 1000
            removeDuration = 1000
        }

        post {
            Tomo.applyAdaptiveBackgroundGenerator(mainBackgroundIv, darkTheme = true)
            renderPosters()
        }

        setupToolbar()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        inflateCommonMenu(this, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (handleCommonMenuClick(this, item)) return true
        return super.onOptionsItemSelected(item)
    }

    private fun renderPosters() {
        val movies = app.moviesCache.values

        val posterItems = movies.map {
            PosterItem(
                movie = it,
                onClick = ::launchDetailsActivity
            )
        }

        val items = listOf(HiItem()) +
                SpaceItem() +
                posterItems +
                SpaceItem()

        adapter.updateAsync(items)
    }

    private fun setupToolbar() {
        setSupportActionBar(mainToolbar)
        supportActionBar?.title = ""
    }

    private fun launchDetailsActivity(movieId: MovieId, posterView: View) {
        val options = ActivityOptionsCompat
            .makeSceneTransitionAnimation(this, posterView, "poster")
        startActivity(DetailsActivity.launchIntent(this, movieId), options.toBundle())
    }
}

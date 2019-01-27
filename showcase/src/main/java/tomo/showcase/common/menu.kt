package tomo.showcase.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat.startActivity
import tomo.showcase.BuildConfig
import tomo.showcase.R

fun inflateCommonMenu(activity: Activity, menu: Menu) {
    activity.menuInflater.inflate(R.menu.menu, menu)
    val forkMeItem = menu.findItem(R.id.menuForkMe)
    forkMeItem.actionView.setOnClickListener {
        activity.onOptionsItemSelected(forkMeItem)
    }
}

fun handleCommonMenuClick(activity: Activity, item: MenuItem): Boolean {
    when (item.itemId) {
        R.id.menuForkMe -> {
            launchProjectsUrl(activity)
            return true
        }
    }

    return false
}

private fun launchProjectsUrl(context: Context) {
    val url = BuildConfig.PROJECT_URL
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(url)
    }
    startActivity(context, intent, null)
}
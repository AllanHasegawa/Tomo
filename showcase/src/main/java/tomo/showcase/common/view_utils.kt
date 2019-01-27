package tomo.showcase.common

import android.app.Activity
import android.graphics.Point
import android.view.View

fun View.windowSize(): Point {
    val display = (context as Activity).windowManager.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size
}

fun Activity.post(body: () -> Unit) {
    findViewById<View>(android.R.id.content).post(body)
}

fun Activity.postDelayed(timeMs: Long, body: () -> Unit) {
    findViewById<View>(android.R.id.content).postDelayed(body, timeMs)
}

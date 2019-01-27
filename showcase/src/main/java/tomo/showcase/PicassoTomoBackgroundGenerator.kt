package tomo.showcase

import android.graphics.Bitmap
import com.squareup.picasso.Transformation
import tomo.Tomo

class PicassoTomoBackgroundGenerator(private val darkTheme: Boolean) : Transformation {
    override fun key(): String = this.javaClass.simpleName

    override fun transform(source: Bitmap): Bitmap =
        Tomo.applyAdaptiveBackgroundGenerator(source, darkTheme = darkTheme)
}
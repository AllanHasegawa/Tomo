package tomo

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import tomo.internal.TomoImpl
import tomo.internal.transformImageView

interface Tomo {
    fun destroy()

    /**
     * Transforms any [Bitmap] into a background. The transformation is composed of:
     *
     * 1) Color leveling so the image can be adapted to work with either dark or light theme.
     * 2) A strong Gaussian Blur to remove details from the original image.
     * 3) A high quality and definition gray noise overlaid on top to give an elegant visual.
     *
     * @param[bitmap] The source bitmap. It'll be recycled.
     * @param[darkTheme]
     *
     * @return A transformed [Bitmap].
     */
    fun applyAdaptiveBackgroundGenerator(bitmap: Bitmap, darkTheme: Boolean): Bitmap

    /**
     * Transforms any [ImageView] into a background. The transformation is composed of:
     *
     * 1) Color leveling so the image can be adapted to work with either dark or light theme.
     * 2) A strong Gaussian Blur to remove details from the original image.
     * 3) A high quality and definition gray noise overlaid on top to give an elegant visual.
     *
     * @param[imageView] The [ImageView] to be transformed. It must be properly initialized with
     * width and height greater than zero and its image fully loaded.
     * @param[darkTheme]
     */
    fun applyAdaptiveBackgroundGenerator(imageView: ImageView, darkTheme: Boolean) =
        transformImageView(imageView) { applyAdaptiveBackgroundGenerator(it, darkTheme) }

    fun applyCustomTransformation(bitmap: Bitmap, builder: TomoTransformsDslContext.() -> Unit): Bitmap

    companion object : Tomo {
        private var instance: Tomo? = null

        private val isInitialized: Boolean
            get() = instance != null

        fun initialize(context: Context, log: Boolean = false) {
            instance = TomoImpl(context, log)
        }

        override fun destroy() {
            instance?.destroy()
            instance = null
        }

        override fun applyAdaptiveBackgroundGenerator(bitmap: Bitmap, darkTheme: Boolean): Bitmap {
            requireInitialization()

            return instance!!.applyAdaptiveBackgroundGenerator(bitmap, darkTheme)
        }

        override fun applyCustomTransformation(
            bitmap: Bitmap,
            builder: TomoTransformsDslContext.() -> Unit
        ): Bitmap {
            requireInitialization()

            return instance!!.applyCustomTransformation(bitmap, builder)
        }

        private fun requireInitialization() {
            if (!isInitialized) {
                throw IllegalStateException("Tomo already destroyed")
            }
        }
    }
}

package tomo.internal

import android.graphics.Bitmap
import tomo.buildTomoPipeline

internal fun adaptiveBackgroundGeneratorPipeline(
    initialBitmap: Bitmap,
    isDark: Boolean
) = buildTomoPipeline(initialBitmap) {
    val blurMaxAllowedRadius = 25f
    val blurWantedRadius = 300f
    val iArea = (initialSize.width * initialSize.height).toFloat()
    val scaleArea = 1920f * 1080f

    val blurScale = ((blurMaxAllowedRadius / blurWantedRadius) * iArea) / scaleArea

    resize(
        newWidth = (initialSize.width * blurScale).toInt(),
        newHeight = (initialSize.height * blurScale).toInt()
    )

    if (isDark) {
        valueClamp(
            lowValue = 0.05f,
            highValue = 0.3f,
            saturationMultiplier = 1.3f,
            saturationLowValue = 0f,
            saturationHighValue = 1f
        )
    } else {
        valueClamp(
            lowValue = 0.8f,
            highValue = 0.95f,
            saturationMultiplier = 0.5f,
            saturationLowValue = 0f,
            saturationHighValue = 0.3f
        )
    }

    blur(blurMaxAllowedRadius)

    resize(
        newWidth = initialSize.width / 2,
        newHeight = initialSize.height / 2
    )

    grayNoise()
}

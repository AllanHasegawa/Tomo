package tomo

import android.graphics.Bitmap
import android.util.Size
import tomo.transforms.*

internal fun buildTomoPipeline(initialBitmap: Bitmap, body: TomoTransformsDslContext.() -> Unit) =
    TomoTransformsDslContext(initialBitmap)(body)

class TomoTransformsDslContext(initialBitmap: Bitmap) {
    private var transformsParams = mutableListOf<Any>()

    val initialSize = Size(initialBitmap.width, initialBitmap.height)

    operator fun invoke(body: TomoTransformsDslContext.() -> Unit): List<Any> {
        body(this)
        return transformsParams.toList()
    }

    fun resize(newWidth: Int, newHeight: Int, filter: Boolean = true) {
        transformsParams.add(
            ResizeTransformDataParams(newWidth = newWidth, newHeight = newHeight, filter = filter)
        )
    }

    fun blur(radius: Float) {
        transformsParams.add(
            BlurTransformParams(radius = radius)
        )
    }

    fun grayNoise(randomSeed: Long = 42) {
        transformsParams.add(
            GrayNoiseTransformParams(randomSeed = randomSeed)
        )
    }

    fun hsvNoise(
        dulling: Int = 4,
        hDistance: Float = 10f,
        sDistance: Float = 0.1f,
        vDistance: Float = 0.1f
    ) {
        transformsParams.add(
            HsvNoiseTransformParams(
                dulling = dulling,
                hDistance = hDistance,
                sDistance = sDistance,
                vDistance = vDistance
            )
        )
    }

    fun valueClamp(
        lowValue: Float,
        highValue: Float,
        saturationMultiplier: Float,
        saturationLowValue: Float,
        saturationHighValue: Float
    ) {
        transformsParams.add(
            ValueClampTransformParams(
                lowValue = lowValue,
                highValue = highValue,
                saturationMultiplier = saturationMultiplier,
                saturationLowValue = saturationLowValue,
                saturationHighValue = saturationHighValue
            )
        )
    }
}


package tomo.internal

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.RenderScript
import tomo.Tomo
import tomo.TomoTransformsDslContext
import tomo.internal.logger.LogCatLogger
import tomo.internal.logger.NoOpLogger
import tomo.internal.transforms.BlurTransform
import tomo.internal.transforms.GrayNoiseTransform
import tomo.internal.transforms.HsvNoiseTransform
import tomo.internal.transforms.ResizeTransform
import tomo.internal.transforms.Transform
import tomo.internal.transforms.ValueClampTransform

internal class TomoImpl(context: Context, log: Boolean) : Tomo {
    private var rs: RenderScript? = null
    private var transforms: List<Transform>? = null
    private var transformsPipeline: TransformsPipeline? = null

    private val isInitialized: Boolean
        get() = rs != null

    init {
        rs = RenderScript.create(context)
        transforms = initializeTransforms(rs!!)
        val logger = when (log) {
            true -> LogCatLogger(debug = true)
            else -> NoOpLogger
        }
        transformsPipeline = TransformsPipeline(transforms!!, logger)
    }

    override fun destroy() {
        transforms?.forEach { it.destroy() }
        rs?.destroy()
        transforms = null
        rs = null
        transformsPipeline = null
    }

    override fun applyAdaptiveBackgroundGenerator(bitmap: Bitmap, darkTheme: Boolean): Bitmap {
        requireInitialization()

        val transformsParams = adaptiveBackgroundGeneratorPipeline(bitmap, isDark = darkTheme)

        return processBitmap(bitmap, transformsParams)
    }

    override fun applyCustomTransformation(bitmap: Bitmap, builder: TomoTransformsDslContext.() -> Unit): Bitmap {
        requireInitialization()

        val transformsParams = TomoTransformsDslContext(bitmap)(builder)

        return processBitmap(bitmap, transformsParams)
    }

    private fun processBitmap(bitmap: Bitmap, transformsParams: List<Any>): Bitmap {
        val output = transformsPipeline!!.processImage(bitmap, transformsParams)
        if (bitmap !== output && !bitmap.isRecycled) {
            bitmap.recycle()
        }
        return output
    }

    private fun requireInitialization() {
        if (!isInitialized) {
            throw IllegalStateException("Tomo already destroyed")
        }
    }
}

private fun initializeTransforms(rs: RenderScript): List<Transform> =
    listOf(
        BlurTransform(rs),
        ResizeTransform(),
        ValueClampTransform(rs),
        HsvNoiseTransform(rs),
        GrayNoiseTransform(rs)
    )

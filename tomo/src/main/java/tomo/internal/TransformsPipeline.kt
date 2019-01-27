package tomo.internal

import android.graphics.Bitmap
import tomo.internal.logger.Logger
import tomo.internal.transforms.Transform

internal class TransformsPipeline(
    private val transforms: List<Transform>,
    private val log: Logger
) {
    fun processImage(bitmap: Bitmap, transformsParams: List<Any>): Bitmap {
        val initialData = Transform.DataInOut(
            inAllocation = null,
            outAllocation = null,
            bitmap = bitmap
        )

        val lastData = transformsParams
            .fold(initialData) { acc, param ->
                log.d { "Processing param: $param" }

                val transform = findTransform(param)

                executeAndComputeTime(transform.name) {
                    transform.transform(acc, param)
                }
            }

        with(lastData) {
            inAllocation?.destroy()
            outAllocation?.destroy()
        }

        return lastData.bitmap
    }

    private fun findTransform(params: Any) =
        transforms.firstOrNull { it.canTransformWithParam(params) }
            ?: throw IllegalStateException("Transform missing for params: $params")

    private fun <R> executeAndComputeTime(tag: String, block: () -> R): R {
        val start = System.currentTimeMillis()
        val result = block()
        val end = System.currentTimeMillis()
        val milliseconds = (end - start)
        log.d { "TIME: [$tag] took ${milliseconds}ms" }
        return result
    }
}

package tomo.internal.transforms

import android.graphics.Bitmap
import tomo.transforms.ResizeTransformDataParams

internal class ResizeTransform : Transform("Resize", ResizeTransformDataParams::class) {

    override fun transform(dataInOut: DataInOut, params: Any): DataInOut = with(dataInOut) {
        with(params as ResizeTransformDataParams) {
            inAllocation?.destroy()
            outAllocation?.destroy()

            val out = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, filter)
            bitmap.recycle()

            DataInOut(
                inAllocation = null,
                outAllocation = null,
                bitmap = out
            )
        }
    }

    override fun destroy() = Unit
}

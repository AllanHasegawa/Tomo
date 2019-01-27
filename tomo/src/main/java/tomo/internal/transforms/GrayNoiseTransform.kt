package tomo.internal.transforms

import android.renderscript.RenderScript
import tomo.rs.ScriptC_gray_noise
import tomo.transforms.GrayNoiseTransformParams

internal class GrayNoiseTransform(
    private val rs: RenderScript
) : Transform("Gray Noise", GrayNoiseTransformParams::class) {
    private val script = ScriptC_gray_noise(rs)

    override fun transform(dataInOut: DataInOut, params: Any): DataInOut = with(params as GrayNoiseTransformParams) {
        val (myInAllocation, myOutAllocation) = Transform.prepareAllocations(rs, dataInOut)

        with(script) {
            _randomSeed = randomSeed

            forEach_pixel(myInAllocation, myOutAllocation)
        }

        myOutAllocation.copyTo(dataInOut.bitmap)

        DataInOut(
            myInAllocation,
            myOutAllocation,
            dataInOut.bitmap
        )
    }

    override fun destroy() {
        script.destroy()
    }
}

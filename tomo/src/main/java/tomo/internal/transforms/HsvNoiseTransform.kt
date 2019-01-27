package tomo.internal.transforms

import android.renderscript.RenderScript
import tomo.rs.ScriptC_hsv_noise
import tomo.transforms.HsvNoiseTransformParams

internal class HsvNoiseTransform(
    private val rs: RenderScript
) : Transform("HSV Noise", HsvNoiseTransformParams::class) {
    private val script = ScriptC_hsv_noise(rs)

    override fun transform(dataInOut: DataInOut, params: Any): DataInOut = with(params as HsvNoiseTransformParams) {
        val (myInAllocation, myOutAllocation) = Transform.prepareAllocations(rs, dataInOut)

        with(script) {
            _dulling = dulling
            _hDistance = hDistance
            _sDistance = sDistance
            _vDistance = vDistance

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

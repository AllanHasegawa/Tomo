package tomo.internal.transforms

import android.renderscript.RenderScript
import tomo.rs.ScriptC_value_clamp
import tomo.transforms.ValueClampTransformParams

internal class ValueClampTransform(
    private val rs: RenderScript
) : Transform("Value Clamp", ValueClampTransformParams::class) {
    private val script = ScriptC_value_clamp(rs)

    override fun transform(dataInOut: DataInOut, params: Any): DataInOut = with(params as ValueClampTransformParams) {
        val (myInAllocation, myOutAllocation) = Transform.prepareAllocations(rs, dataInOut)

        with(script) {
            _lowValue = lowValue
            _highValue = highValue
            _saturationMultiplier = saturationMultiplier
            _saturationLowValue = saturationLowValue
            _saturationHighValue = saturationHighValue
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

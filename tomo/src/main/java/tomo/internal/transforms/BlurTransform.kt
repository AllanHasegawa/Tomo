package tomo.internal.transforms

import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import tomo.transforms.BlurTransformParams

internal class BlurTransform(
    private val rs: RenderScript
) : Transform("Blur", BlurTransformParams::class) {
    private val script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))

    override fun transform(dataInOut: DataInOut, params: Any): DataInOut = with(params as BlurTransformParams) {
        val radius = params.radius
        require(radius <= 25f && radius > 0f) { "Radius must be between [0, 25)" }

        val (myInAllocation, myOutAllocation) = Transform.prepareAllocations(rs, dataInOut)

        with(script) {
            setRadius(radius)

            setInput(myInAllocation)
            forEach(myOutAllocation)
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

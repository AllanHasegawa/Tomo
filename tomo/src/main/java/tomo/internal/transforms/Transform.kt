package tomo.internal.transforms

import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.RenderScript
import kotlin.reflect.KClass

/**
 * A base transformation class.
 *
 * @constructor[name] Its name.
 * @constructor[paramsT] The type of the parameter. It must be unique.
 */
internal abstract class Transform(val name: String, private val paramsT: KClass<*>) {
    data class DataInOut(
        val inAllocation: Allocation?,
        val outAllocation: Allocation?,
        val bitmap: Bitmap
    )

    abstract fun transform(dataInOut: DataInOut, params: Any): DataInOut

    abstract fun destroy()

    fun canTransformWithParam(param: Any) = param.javaClass == paramsT.java

    companion object {
        fun prepareAllocations(rs: RenderScript, dataInOut: DataInOut): Pair<Allocation, Allocation> =
            with(dataInOut) {
                if (inAllocation != null && outAllocation != null) {
                    outAllocation to inAllocation
                } else {
                    val tempAlloc = Allocation.createFromBitmap(rs, bitmap)
                    val outAlloc = Allocation.createTyped(rs, tempAlloc.type)
                    tempAlloc to outAlloc
                }
            }
    }
}

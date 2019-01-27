package tomo.transforms

import java.util.*
import kotlin.math.absoluteValue

data class GrayNoiseTransformParams(
    val randomSeed: Long = defaultRandomSeed()
) {
    companion object {
        fun defaultRandomSeed() = Random().nextLong().absoluteValue % Int.MAX_VALUE
    }

    init {
        require(randomSeed >= 0 && randomSeed <= Int.MAX_VALUE) {
            "randomSeed must be in the interval [0, Int.MAX_VALUE)"
        }
    }
}

package tomo.transforms

data class ValueClampTransformParams(
    val lowValue: Float,
    val highValue: Float,
    val saturationMultiplier: Float,
    val saturationLowValue: Float,
    val saturationHighValue: Float
)

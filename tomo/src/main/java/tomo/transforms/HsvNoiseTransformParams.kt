package tomo.transforms

data class HsvNoiseTransformParams(
    val dulling: Int = 4,
    val hDistance: Float = 10f,
    val sDistance: Float = 0.1f,
    val vDistance: Float = 0.1f
)

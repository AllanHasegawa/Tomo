package tomo.transforms

data class ResizeTransformDataParams(
    val newWidth: Int,
    val newHeight: Int,
    val filter: Boolean
)

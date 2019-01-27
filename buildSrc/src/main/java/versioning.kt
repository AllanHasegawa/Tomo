object Versioning {
    private val semantic = Semantic(
        major = 0,
        minor = 0,
        patch = 1
    )

    val version = semantic.toVersion()
}

data class Version(val code: Int, val name: String)

private data class Semantic(
    val major: Int,
    val minor: Int,
    val patch: Int
) {
    fun toVersion() = Version(
        code = major * 10_000 + minor * 100 + patch,
        name = "$major.$minor.$patch"
    )
}
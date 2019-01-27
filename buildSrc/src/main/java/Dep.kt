object Dep {
    private val v = Versions

    // Kotlin

    const val kotlinStd = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${v.kotlin}"
    const val junit = "junit:junit:${v.junit}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${v.retrofit}"
    const val retrofitMoshi = "com.squareup.retrofit2:converter-moshi:${v.retrofit}"

    // Android

    const val appCompat = "androidx.appcompat:appcompat:${v.appCompat}"
    const val testRunner = "androidx.test:runner:${v.testRunner}"
    const val espresso = "androidx.test.espresso:espresso-core:${v.espresso}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${v.constraintLayout}"
    const val picasso = "com.squareup.picasso:picasso:${v.picasso}"
    const val groupie = "com.xwray:groupie:${v.groupie}"
    const val groupieKt = "com.xwray:groupie-kotlin-android-extensions:${v.groupie}"
    const val material = "com.google.android.material:material:${v.material}"
    const val rebound = "com.facebook.rebound:rebound:${v.rebound}"
    const val rvAnimators = "jp.wasabeef:recyclerview-animators:${v.rvAnimators}"
}

object Versions {
    // Kotlin

    const val kotlin = "1.3.20"
    const val junit = "4.12"
    const val retrofit = "2.5.0"

    // Android

    const val appCompat = "1.0.2"
    const val material = "1.0.0"
    const val testRunner = "1.1.1"
    const val espresso = "3.1.1"
    const val constraintLayout = "2.0.0-alpha3"
    const val picasso = "2.71828"
    const val groupie = "2.3.0"
    const val rebound = "0.3.8"
    const val rvAnimators = "3.0.0"
}

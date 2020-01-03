import java.util.*

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
}

fun loadTMDBApiKey() =
    rootProject
        .file("local.properties")
        .let {
            fun showMissingWarning() = logger.warn("TMDB API KEY NOT SETUP PROPERLY.")

            val value = when (it.exists()) {
                true -> Properties().run {
                    load(it.inputStream())
                    getProperty("tomo.showcase.tmdb.apiKey")
                }
                false -> null
            }

            when (value) {
                null -> {
                    showMissingWarning()
                    "missing_api_key"
                }
                else -> value
            }
        }


android {
    compileSdkVersion(AndroidConfig.compileSdkVersion)

    defaultConfig {
        applicationId = "tomo.showcase"
        minSdkVersion(AndroidConfig.minSdkVersion)
        targetSdkVersion(AndroidConfig.targetSdkVersion)
        versionCode = Versioning.version.code
        versionName = Versioning.version.name

        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "PROJECT_URL", "\"https://github.com/AllanHasegawa/Tomo\"")

        val tmdbApiKey = loadTMDBApiKey()
        buildConfigField("String", "TMDB_API_KEY", "\"$tmdbApiKey\"")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    // IMPORTANT!  Enables view caching in viewholders.
    // See: https://github.com/Kotlin/KEEP/blob/master/proposals/android-extensions-entity-caching.md
    androidExtensions {
        isExperimental = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    with(Dep) {
        implementation(project(":tomo"))
        implementation(kotlinStd)
        implementation(appCompat)
        implementation(constraintLayout)
        implementation(picasso)
        implementation(groupie)
        implementation(groupieKt)
        implementation(material)
        implementation(retrofit)
        implementation(retrofitMoshi)
        implementation(rebound)
        implementation(rvAnimators)
    }
}

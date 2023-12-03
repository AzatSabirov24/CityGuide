plugins {
    `android-library`
    `kotlin-android`
    kotlin("plugin.serialization")
}

apply<MainGradlePlugin>()

android {
    namespace = "com.asabirov.search"

    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = ProjectConfig.kotlinCompilerExtensionVersion
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    retrofit()
    hilt()
    compose()
    coil()
    navigation()

    junit()
    implementation ("com.google.maps.android:maps-compose:4.1.1")
    implementation ("com.google.accompanist:accompanist-permissions:0.32.0")

//    implementation ("androidx.paging:paging-compose:3.3.0-alpha02")

    implementation ("androidx.paging:paging-compose:1.0.0-alpha18")


    // Optionally, you can include the Compose utils library for Clustering,
    // Street View metadata checks, etc.
    implementation  ("com.google.maps.android:maps-compose-utils:4.1.1")

    // Optionally, you can include the widgets library for ScaleBar, etc.
    implementation  ("com.google.maps.android:maps-compose-widgets:4.1.1")

    // modules
    core()
    coreUi()
}
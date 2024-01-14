plugins {
    `android-library`
    `kotlin-android`
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

    junit()
    implementation ("com.google.maps.android:maps-compose:4.1.1")
    implementation ("com.google.accompanist:accompanist-permissions:0.32.0")
    implementation ("androidx.paging:paging-compose:3.2.1")
    implementation  ("com.google.maps.android:maps-compose-utils:4.1.1")
    implementation  ("com.google.maps.android:maps-compose-widgets:4.1.1")

    // modules
    core()
    coreUi()
}
plugins {
    `android-library`
    `kotlin-android`
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
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

    junit()

    // modules
    core()
}
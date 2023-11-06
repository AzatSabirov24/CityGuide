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

//    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
//    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")

    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
}
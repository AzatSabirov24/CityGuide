plugins {
    `android-library`
    `kotlin-android`
}

apply<MainGradlePlugin>()

android {
    namespace = "com.asabirov.core"
}

dependencies {

    playServicesLocation()
    googlePlaces()
    hilt()

    junit()
}
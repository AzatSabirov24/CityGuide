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

    compose()
    composeTests()

    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
}
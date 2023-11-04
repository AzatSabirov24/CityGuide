plugins {
    `android-library`
    `kotlin-android`
}

apply<MainGradlePlugin>()

android {
    namespace = "com.asabirov.search"
}

dependencies {
    retrofit()
    hilt()
}
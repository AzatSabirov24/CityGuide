import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project

object Dependencies {

    // android core
    const val androidCoreKtx = "androidx.core:core-ktx:${Versions.androidCoreKtx}"

    // lifecycle ktx
    const val lifecycleKtx = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleKtx}"

    // compose
    const val activityCompose = "androidx.activity:activity-compose:${Versions.activityCompose}"
    const val composeMaterial = "androidx.compose.material3:material3:${Versions.composeMaterial3}"
    const val composeUi = "androidx.compose.ui:ui:${Versions.compose}"
    const val composeUiGraphics = "androidx.compose.ui:ui-graphics:${Versions.compose}"
    const val composeUiTooling = "androidx.compose.ui:ui-tooling:${Versions.compose}"
    const val composeUiToolingPreview = "androidx.compose.ui:ui-tooling-preview:${Versions.compose}"
    const val composeRuntime = "androidx.compose.runtime:runtime:${Versions.compose}"
    const val composeBom = "androidx.compose:compose-bom:${Versions.composeBom}"

    // hilt
    const val hiltAndroid = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val hiltCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
    const val hiltAgp = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"
    const val hiltNavigationComposeVersion =
        "androidx.hilt:hilt-navigation-compose:${Versions.hiltNavigationComposeVersion}"

    // okHttp
    const val okHttp = "com.squareup.okhttp3:okhttp:${Versions.okHttp}"
    const val okHttpLoggingInterceptor =
        "com.squareup.okhttp3:logging-interceptor:${Versions.okHttp}"

    // retrofit
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val gsonConverter = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"

    // room
    const val roomRuntime = "androidx.room:room-runtime:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.room}"

    // navigation
    const val navigationCompose =
        "androidx.navigation:navigation-compose:${Versions.navigationCompose}"

    // location services
    const val playCervicesLocation =
        "com.google.android.gms:play-services-location:${Versions.playCervicesLocation}"

    // google places
    const val googlePlaces = "com.google.android.libraries.places:places:${Versions.googlePlaces}"

    // coil
    const val coil = "io.coil-kt:coil-compose:${Versions.coil}"

    // jUnit
    const val jUnit = "junit:junit:${Versions.jUnit}"
    const val jUnitAndroid = "androidx.test.ext:junit:${Versions.jUnitAndroid}"

    // composeTests
    const val composeTestBom = "androidx.compose:compose-bom:${Versions.composeTestBom}"
    const val composeJUnit = "androidx.compose.ui:${Versions.composeJUnit}"
    const val composeUiTestManifest = "androidx.compose.ui:${Versions.composeUiTestManifest}"
}

fun DependencyHandler.androidCoreKtx() {
    implementation(Dependencies.androidCoreKtx)
}

fun DependencyHandler.lifecycleKtx() {
    implementation(Dependencies.lifecycleKtx)
}

fun DependencyHandler.retrofit() {
    implementation(Dependencies.retrofit)
    implementation(Dependencies.gsonConverter)
    implementation(Dependencies.okHttp)
    implementation(Dependencies.okHttpLoggingInterceptor)
}

fun DependencyHandler.room() {
    implementation(Dependencies.roomKtx)
    ksp(Dependencies.roomCompiler)
}

fun DependencyHandler.compose() {
    implementation(Dependencies.activityCompose)
    implementation(Dependencies.composeUi)
    implementation(Dependencies.composeRuntime)
    implementation(Dependencies.composeUiGraphics)
    implementation(Dependencies.composeUiTooling)
    implementation(Dependencies.composeMaterial)
    implementation(platform(Dependencies.composeBom))
    debugImplementation(Dependencies.composeUiToolingPreview)
}

fun DependencyHandler.hilt() {
    implementation(Dependencies.hiltAndroid)
    ksp(Dependencies.hiltCompiler)
    implementation(Dependencies.hiltNavigationComposeVersion)
}

fun DependencyHandler.navigation() {
    implementation(Dependencies.navigationCompose)
}

fun DependencyHandler.playServicesLocation() {
    implementation(Dependencies.playCervicesLocation)
}

fun DependencyHandler.googlePlaces() {
    implementation(Dependencies.googlePlaces)
}

fun DependencyHandler.coil() {
    implementation(Dependencies.coil)
}

// Test

fun DependencyHandler.junit() {
    testImplementation(Dependencies.jUnit)
    androidTestImplementation(Dependencies.jUnitAndroid)
}

fun DependencyHandler.composeTests() {
    androidTestImplementation(platform(Dependencies.composeTestBom))
    androidTestImplementation(Dependencies.composeJUnit)
    debugImplementation(Dependencies.composeUiTestManifest)
}

// modules

fun DependencyHandler.core() {
    implementation(project(":core"))
}

fun DependencyHandler.coreUi() {
    implementation(project(":core_ui"))
}

fun DependencyHandler.search() {
    implementation(project(":search"))
}
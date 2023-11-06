plugins {
    id("com.google.devtools.ksp") version "1.9.20-RC-1.0.13" apply false
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
}

buildscript {
    dependencies {
        classpath(Dependencies.hiltAgp)
    }
}
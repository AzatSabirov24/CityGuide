plugins {
    id("com.google.devtools.ksp") version "1.9.20-RC-1.0.13" apply false
    kotlin("plugin.serialization") version "1.9.20"
}

buildscript {
    dependencies {
        classpath(Dependencies.hiltAgp)
    }
}
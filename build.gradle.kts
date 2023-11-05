plugins {
    id("com.google.devtools.ksp") version "1.9.20-RC-1.0.13" apply false
}

buildscript {
    dependencies {
        classpath(Dependencies.hiltAgp)
    }
}
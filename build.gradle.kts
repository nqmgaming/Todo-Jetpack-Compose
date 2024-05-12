// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.51.1")
    }
}
plugins {
    alias(libs.plugins.android.application) apply false
    id("com.android.library") version "8.4.0" apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("com.google.devtools.ksp") version "1.9.23-1.0.20" apply false
}
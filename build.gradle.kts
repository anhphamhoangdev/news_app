buildscript {
    dependencies {
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.7")
        classpath("com.google.devtools.ksp:symbol-processing-gradle-plugin:1.9.0-1.0.13")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.google.gms.google.services) apply false
    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false

}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.androidLibrary) apply false

    //ksp
    id("com.google.devtools.ksp") version "1.9.21-1.0.15"

    //hilt
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
}
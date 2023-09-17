// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application").version("7.3.1").apply(false)
    id("com.android.library").version("7.3.1").apply(false)
    kotlin("android").version("1.7.10").apply(false)
    id("com.google.dagger.hilt.android").version("2.44").apply(false)
    id("com.google.gms.google-services") version "4.3.15" apply(false)
    id("com.google.devtools.ksp") version "1.8.0-1.0.9" apply false
    //id("com.android.library") version "7.3.1" apply false
    //id("org.jetbrains.kotlin.android") version "1.8.0" apply false

}
buildscript {

    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
    dependencies {
        classpath(Build.androidBuildTools)
        classpath(Build.hiltAndroid)
        classpath(Build.kotlinGradlePlugin)


    }
}
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

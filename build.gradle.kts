// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application").version("8.0.1").apply(false)
    id("com.android.library").version("8.0.1").apply(false)
    kotlin("android").version("1.8.10").apply(false)
    id("com.google.dagger.hilt.android").version("2.44").apply(false)


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
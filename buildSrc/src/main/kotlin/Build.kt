object Build {

    private const val androidToolsBuildVersion = "7.3.1"
    const val androidBuildTools = "com.android.tools.build:gradle:$androidToolsBuildVersion"

    const val hiltAndroid = "com.google.dagger:hilt-android-gradle-plugin:${Hilt.hiltVersion}"

    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.version}"
}
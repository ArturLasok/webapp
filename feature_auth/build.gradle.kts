plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    kotlin(KotlinPlugins.serialization) version Kotlin.version
}

android {
    namespace = "com.arturlasok.feature_auth"
    compileSdk = Android.compileSdk

    defaultConfig {
        minSdk = Android.minSdk
        targetSdk = Android.targetSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
       // consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Compose.composeKotlinCompilerVersion
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    //Modules to :feature_auth
    implementation(project(Modules.feature_core))
    //Hilt
    implementation(AndroidX.hiltNav)
    implementation(Hilt.hiltAndroid)
    kapt(Hilt.hiltKapt)
    //CoreKtx
    implementation(AndroidX.coreKtx)
    //Compose
    implementation(Compose.activityCompose)
    implementation(Compose.composeUi)
    implementation(Compose.composeUiTooling)
    implementation(Compose.composeFundation)
    implementation(Compose.composeMaterial)

}
// Allow references to generated code
kapt {
    correctErrorTypes = true
}
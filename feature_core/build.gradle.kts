plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    kotlin(KotlinPlugins.serialization) version Kotlin.version
}

android {
    namespace = "com.arturlasok.feature_core"
    compileSdk = Android.compileSdk

    defaultConfig {
        minSdk = Android.minSdk
        targetSdk = Android.targetSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

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

    //DataStore
    implementation(AndroidX.dataStoreCore)
    implementation(AndroidX.dataStorePreferences)
    //Hilt
    implementation(Hilt.hiltAndroid)
    kapt(Hilt.hiltKapt)
    implementation(AndroidX.hiltNav)
    //Compose
    implementation(Compose.activityCompose)
    implementation(Compose.composeUi)
    implementation(Compose.composeUiTooling)
    implementation(Compose.composeFundation)
    implementation(Compose.composeMaterial)
    //coreKtx
    implementation(AndroidX.coreKtx)
}
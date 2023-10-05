plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    kotlin(KotlinPlugins.serialization) version Kotlin.version
}

android {
    namespace = "com.arturlasok.feature_creator"
    compileSdk = Android.compileSdk

    defaultConfig {
        minSdk = Android.minSdk
        targetSdk = Android.targetSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        //consumerProguardFiles("consumer-rules.pro")
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
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation("org.mongodb:bson-kotlinx:4.10.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    //Modules to :feature_core
    implementation(project(Modules.feature_core))
    //Hilt
    implementation(AndroidX.hiltNav)
    implementation(Hilt.hiltAndroid)
    kapt(Hilt.hiltKapt)
    //CoreKtx
    //implementation(AndroidX.coreKtx)
    //Compose
    implementation(Compose.activityCompose)
    implementation(Compose.composeUi)
    implementation(Compose.composeUiTooling)
    implementation(Compose.composeFundation)
    implementation(Compose.composeMaterial)
    implementation(Compose.activityCompose)
    implementation(Compose.navigationCompose)
    implementation(Compose.composeIcons)
    //Firebase
    implementation(platform(Firebase.firebaseBom))
    implementation(Firebase.firebaseAnalytics)
    implementation(Firebase.firebaseAuth)
    //Ktor
    implementation(Ktor.ktorClient)
    implementation(Ktor.ktorNegotiation)
    implementation(Ktor.ktorJson)
    implementation(Ktor.ktorSerialization)
}
// Allow references to generated code
kapt {
    correctErrorTypes = true
}
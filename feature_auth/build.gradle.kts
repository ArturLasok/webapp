plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    kotlin(KotlinPlugins.serialization) version Kotlin.version
    //id("com.google.gms.google-services")
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
        getByName("release") {
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
    //Modules to :feature_auth
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

    //vm lifecycle
   // implementation(AndroidX.lifeCyc)
   // implementation(AndroidX.lifeCycVm)
   // implementation(AndroidX.lifektx)
}
// Allow references to generated code
kapt {
    correctErrorTypes = true
}
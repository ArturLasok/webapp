plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    kotlin(KotlinPlugins.serialization) version Kotlin.version
    //id("com.google.gms.google-services")
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
        getByName("release") {
            buildConfigField("String","BASEAPIURL","\"http://server873539.nazwa.pl\"")
            buildConfigField("String","APPURL","\"web\"")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            buildConfigField("String","BASEAPIURL","\"http://server873539.nazwa.pl\"")
            buildConfigField("String","APPURL","\"web\"")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
    //implementation(project(Modules.feature_auth))
    implementation("org.mongodb:bson-kotlinx:4.10.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    //DataStore
    implementation(AndroidX.dataStoreCore)
    implementation(AndroidX.dataStorePreferences)
    //Hilt
    implementation(Hilt.hiltAndroid)
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("junit:junit:4.12")
    kapt(Hilt.hiltKapt)
    implementation(AndroidX.hiltNav)
    //Compose
    implementation(Compose.activityCompose)
    implementation(Compose.composeUi)
    implementation(Compose.composeUiTooling)
    implementation(Compose.composeFundation)
    implementation(Compose.composeMaterial)
    implementation(Compose.composeIcons)
    //Ktor
    implementation(Ktor.ktorClient)
    implementation(Ktor.ktorNegotiation)
    implementation(Ktor.ktorJson)
    implementation(Ktor.ktorSerialization)
    //Room
    implementation(Room.roomRuntime)
    annotationProcessor(Room.roomAnnotationProcessor)
    ksp(Room.roomKotlinSymbolProcessing)
    implementation(Room.roomKtx)
    //coreKtx
    //implementation(AndroidX.coreKtx)
    //Firebase
    implementation(platform(Firebase.firebaseBom))
    implementation(Firebase.firebaseAnalytics)
    implementation(Firebase.firebaseAuth)
    //vm lifecycle
    //implementation(AndroidX.lifeCyc)
   // implementation(AndroidX.lifeCycVm)
   // implementation(AndroidX.lifektx)
    //Test
    testImplementation("androidx.compose.ui:ui-test-junit4-android:1.5.3")
}
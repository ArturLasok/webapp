
plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    kotlin(KotlinPlugins.serialization) version Kotlin.version
    id("com.google.gms.google-services")
}

android {
    namespace = "com.arturlasok.webapp"
    compileSdk = Android.compileSdk
    defaultConfig {
        applicationId = Android.appId
        minSdk = Android.minSdk
        targetSdk = Android.targetSdk
        versionCode = Android.versionCode
        versionName = Android.versionName

        multiDexEnabled = true
    }
    buildFeatures {
        compose = true
        //viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Compose.composeKotlinCompilerVersion
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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
    kotlin {
        jvmToolchain {
            this.languageVersion.set(JavaLanguageVersion.of(11))
        }
    }
}

dependencies {

    //implementation("com.android.support:multidex:1.0.3")
    implementation("androidx.compose.runtime:runtime:1.2.1")
    //kapt("androidx.hilt:hilt-compiler:1.0.0")
    //implementation("androidx.core:core-ktx:1.10.1")

   // implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation(platform("androidx.compose:compose-bom:2022.10.00"))
    //Modules to :app
    implementation(project(Modules.feature_core))
    implementation(project(Modules.feature_auth))
    //Compose
    implementation(Compose.composeUi)
    implementation(Compose.composeUiTooling)
    implementation(Compose.composeUiToolingPreview)
    implementation(Compose.composeFundation)
    implementation(Compose.composeMaterial)
    implementation(Compose.activityCompose)
    implementation(Compose.navigationCompose)
    //DataStore
    implementation(AndroidX.dataStoreCore)
    implementation(AndroidX.dataStorePreferences)
    //AppCompat
    implementation(AndroidX.appCompat)
    //Accompanist
    implementation(Accompanist.accompanistUiController)
    //Hilt
    implementation(AndroidX.hiltNav)
    implementation(Hilt.hiltAndroid)
    kapt(Hilt.hiltKapt)
    //Ktor
    implementation(Ktor.ktorClient)
    implementation(Ktor.ktorNegotiation)
    implementation(Ktor.ktorJson)
    implementation(Ktor.ktorAndroid)
    //Firebase
    implementation(platform(Firebase.firebaseBom))
    implementation(Firebase.firebaseAnalytics)
    implementation(Firebase.firebaseAuth)
    //Room
    implementation(Room.roomRuntime)
    annotationProcessor(Room.roomAnnotationProcessor)
    ksp(Room.roomKotlinSymbolProcessing)
    implementation(Room.roomKtx)

    //Test
/*
    // Core library
    val androidXTestVersion = "1.5.0"
    androidTestImplementation("androidx.test:core:$androidXTestVersion")

    // AndroidJUnitRunner and JUnit Rules
    val testRunnerVersion = "1.5.2"
    androidTestImplementation("androidx.test:runner:$testRunnerVersion")
    val testRulesVersion = "1.5.0"
    androidTestImplementation("androidx.test:rules:$testRulesVersion")

    // Assertions
    val testJunitVersion = "1.1.5"
    androidTestImplementation("androidx.test.ext:junit:$testJunitVersion")
    val truthVersion = "1.5.0"
    androidTestImplementation("androidx.test.ext:truth:$truthVersion")

    // Espresso dependencies
    val espressoVersion = "3.5.1"
    androidTestImplementation( "androidx.test.espresso:espresso-core:$espressoVersion")
    androidTestImplementation( "androidx.test.espresso:espresso-contrib:$espressoVersion")
    androidTestImplementation( "androidx.test.espresso:espresso-intents:$espressoVersion")
    androidTestImplementation( "androidx.test.espresso:espresso-accessibility:$espressoVersion")
    androidTestImplementation( "androidx.test.espresso:espresso-web:$espressoVersion")
    androidTestImplementation( "androidx.test.espresso.idling:idling-concurrent:$espressoVersion")
    androidTestImplementation( "androidx.test.espresso:espresso-idling-resource:$espressoVersion")
   */
}


// Allow references to generated code
kapt {
    correctErrorTypes = true
}
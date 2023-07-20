plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    kotlin(KotlinPlugins.serialization) version Kotlin.version
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
            isMinifyEnabled = false
        }

    }
    compileOptions {

        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {

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
    //CoreKtx
    implementation(AndroidX.coreKtx)



    //Test

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
}
// Allow references to generated code
kapt {
    correctErrorTypes = true
}
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    alias(libs.plugins.daggerHilt.android)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.carlostorres.weatherapppositivo"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.carlostorres.weatherapppositivo"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        dataBinding = true
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    //Fragments
    implementation(libs.androidx.fragment)

    //Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)

    //DaggerHilt
    implementation(libs.daggerHilt.android)
    kapt(libs.daggerHilt.android.compiler)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.retrofit.converter.gson)

    //Google Maps
    implementation(libs.maps.ktx)
    implementation(libs.maps.utils.ktx)
    implementation(libs.android.maps.utils)

    //Location
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)

    implementation (libs.easywaylocation)

    implementation (libs.places.ktx)

    implementation(libs.volley)

    //Compose
    implementation (libs.androidx.compose.bom)
    implementation (libs.androidx.activity.compose)
    implementation (libs.androidx.ui)
    implementation (libs.androidx.material3)
    implementation (libs.androidx.ui.tooling.preview)

}
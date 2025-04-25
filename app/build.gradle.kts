plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    alias(libs.plugins.daggerHilt.android)
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
    implementation("com.google.maps.android:maps-ktx:3.2.0")
    implementation("com.google.maps.android:maps-utils-ktx:3.2.0")
    implementation("com.google.maps.android:android-maps-utils:2.2.3")

    //Location
    implementation("com.google.android.gms:play-services-maps:18.0.2")
    implementation("com.google.android.gms:play-services-location:20.0.0")

    implementation ("com.github.prabhat1707:EasyWayLocation:2.4")

    implementation ("com.google.maps.android:places-ktx:2.0.0")

    implementation("com.android.volley:volley:1.2.1")

}
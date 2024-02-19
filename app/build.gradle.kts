plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs")
}

android {
    namespace = "com.seanlooong.exerciseandroid"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.seanlooong.exerciseandroid"
        minSdk = 29
        targetSdk = 33
        versionCode = 2
        versionName = "1.1.0"

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
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
    implementation("androidx.camera:camera-mlkit-vision:1.3.0-alpha05")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    // Concurrent library for asynchronous coroutines
    implementation("androidx.concurrent:concurrent-futures-ktx:1.1.0")
    // camera
    // CameraX core library
    implementation("androidx.camera:camera-core:1.3.0-alpha05")
    implementation("androidx.camera:camera-camera2:1.3.0-alpha05")
    // CameraX Lifecycle library
    implementation("androidx.camera:camera-lifecycle:1.3.0-alpha05")
    // CameraX View class
    implementation("androidx.camera:camera-view:1.3.0-alpha05")
    // CameraX Extensions library
    implementation("androidx.camera:camera-extensions:1.3.0-alpha05")
    //WindowManager
    implementation("androidx.window:window:1.1.0-beta01")
    // Glide
    implementation("com.github.bumptech.glide:glide:4.12.0")
    kapt("com.github.bumptech.glide:compiler:4.11.0")
    // barcode 二维码扫描
    implementation("com.google.mlkit:barcode-scanning:17.1.0")
    // Tensorflow lite dependencies
    implementation("org.tensorflow:tensorflow-lite:2.9.0")
    implementation("org.tensorflow:tensorflow-lite-gpu:2.9.0")
    implementation("org.tensorflow:tensorflow-lite-support:0.4.2")
}
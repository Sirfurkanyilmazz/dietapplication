plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.dietapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dietapplication"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // Compose
    implementation(platform(libs.androidx.compose.bom.v20241001))
    implementation(libs.androidx.activity.compose.v192)
    implementation(libs.material3)
    implementation(libs.ui)
    implementation(libs.ui.tooling.preview)
    implementation(libs.androidx.navigation.runtime.android)
    implementation(libs.androidx.navigation.compose.android)
    implementation(libs.androidx.webkit)
    debugImplementation(libs.ui.tooling)

// Coroutines
    implementation(libs.kotlinx.coroutines.android)

// Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)

// Retrofit + OkHttp
    implementation(libs.retrofit)
    implementation(libs.converter.moshi)
    implementation(libs.logging.interceptor)

// CameraX
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)

// ML Kit OCR (Play Services)
    implementation(libs.play.services.mlkit.text.recognition)

// WorkManager
    implementation(libs.androidx.work.runtime.ktx)

// Coil (görsel yükleme)
    implementation(libs.coil.compose)
    implementation(libs.material.icons.extended)

}


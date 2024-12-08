plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.chefroad"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.chefroad"
        minSdk = 34
        targetSdk = 34
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.firebase.auth.ktx)

    implementation(libs.androidx.runtime.livedata)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)

    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation ("com.google.code.gson:gson:2.10.1")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.0")

    implementation ("com.google.firebase:firebase-database-ktx:20.1.0") // Firebase Realtime Database
    implementation ("com.google.firebase:firebase-firestore-ktx:24.6.0") // Firebase Firestore
    implementation ("com.google.firebase:firebase-auth-ktx:21.0.3") // Firebase Auth
    implementation ("com.google.firebase:firebase-vertexai:1.0.0") // Firebase Vertex AI (버전 필요)

    //implementation(libs.firebase.database.ktx)
    //implementation(libs.firebase.firestore.ktx)
    //implementation(libs.firebase.vertexai)
    //implementation(libs.androidx.room.common)

    //implementation ("com.google.firebase:firebase-firestore:24.6.0")  // FirebaseFirestore 의존성 추가
    //implementation ("com.google.firebase:firebase-auth:21.0.7")      // FirebaseAuth 의존성 (만약 안 추가되어 있다면)

    implementation (libs.map.sdk)
}
kapt {
    correctErrorTypes = true
}
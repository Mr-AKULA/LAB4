plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.geometrycalculator"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.geometrycalculator"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        // Информация об авторе в APK
        buildConfigField ("String", "AUTHOR_NAME", "\"Алексеев Павел Васильевич\"")
        buildConfigField ("String", "AUTHOR_GROUP", "\"ИСТ-33\"")
        buildConfigField("String", "BUILD_DATE", "\"04.12.2025 02:59\"")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
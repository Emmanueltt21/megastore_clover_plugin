plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.loyalty.api18" // Keep your existing namespace
    compileSdk = 35

    defaultConfig {
        applicationId = "com.loyalty.api18" // Keep your existing app ID
        minSdk = 21 // Clover devices support API 24+
       // targetSdk = 25 // Use 33 for better Clover compatibility
        targetSdk = 33 // Use 33 for better Clover compatibility
        versionCode = 10
        versionName = "10.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // IMPORTANT: Clover-specific configurations
        multiDexEnabled = true

        // Add build config fields for Clover
        buildConfigField("String", "CLOVER_APP_ID", "\"your_clover_app_id_here\"")
        buildConfigField("boolean", "IS_CLOVER_BUILD", "true")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // For production Clover apps, you'll need proper signing
            signingConfig = signingConfigs.getByName("debug") // DEMO ONLY
        }
        debug {
            isDebuggable = true
            isMinifyEnabled = false
           // applicationIdSuffix = ".debug"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // CRITICAL: Packaging options for Clover SDK
    packaging {
        resources {
            excludes += setOf(
                "META-INF/DEPENDENCIES",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt",
                "META-INF/ASL2.0",
                "META-INF/LGPL2.1"
            )
        }
    }

    // Enable build features if needed
    buildFeatures {
        buildConfig = true
        viewBinding = true // Optional: for easier view management
    }
}

dependencies {
    // Standard Android dependencies
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // CLOVER SDK DEPENDENCIES - CRITICAL FOR INTEGRATION
    // Core Clover SDK
    implementation(libs.clover.android.sdk)

    // Remote Pay SDK (for payment processing)
   // implementation(libs.remote.pay.android.sdk)

    // Clover Connector SDK (for device integration)
   // implementation(libs.clover.android.connector.sdk)

    // REQUIRED SUPPORT LIBRARIES FOR CLOVER
    // JSON processing (Clover SDK dependency)
    implementation("com.google.code.gson:gson:2.10.1")
//    implementation("org.json:json:20230618")

    // HTTP client (for Clover API calls)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Retrofit for REST API calls
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // MultiDex support (Clover SDK is large)
    implementation("androidx.multidex:multidex:2.0.1")

    // Logging (recommended for debugging Clover integration)
    implementation("com.jakewharton.timber:timber:5.0.1")

    // OPTIONAL: Additional Clover-related dependencies
    // Clover CFD (Customer Facing Display) if needed
    // implementation("com.clover.sdk:cfd-android-sdk:4.1.4")

    // For notifications
    implementation("androidx.core:core:1.12.0")
    implementation("androidx.work:work-runtime:2.9.0") // Optional: for background tasks

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
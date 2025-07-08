plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize") // Add this line for Parcelable support
}

android {
    namespace = "com.example.mad3"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mad3"
        minSdk = 31
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        viewBinding =true
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
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
//    implementation("androidx.recyclerview:recyclerview:1.3.1")
//    implementation("com.google.android.material:material:1.9.0")
//    implementation("androidx.cardview:cardview:1.0.0")
//    implementation("com.google.code.gson:gson:2.10.1")
//    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
//
//
//    implementation ("androidx.core:core-ktx:1.7.0")
//    implementation ("androidx.appcompat:appcompat:1.4.1")
//    implementation ("com.google.android.material:material:1.5.0")
//    implementation ("androidx.constraintlayout:constraintlayout:2.1.3")
//    implementation ("androidx.recyclerview:recyclerview:1.3.2")
//    implementation ("com.google.android.material:material:1.11.0")

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.activity:activity-ktx:1.7.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.google.code.gson:gson:2.10.1")
 

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")








    // Removed duplicate entry

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
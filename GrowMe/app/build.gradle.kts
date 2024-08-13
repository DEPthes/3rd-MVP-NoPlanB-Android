import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

val properties = gradleLocalProperties(rootDir, providers)

android {
    namespace = "com.growme.growme"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.growme.growme"
        minSdk = 29
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "KAKAO_NATIVE_KEY",
            "\"${properties.getProperty("KAKAO_NATIVE_KEY")}\""
        )
        manifestPlaceholders["KAKAO_NATIVE_KEY"] = properties.getProperty("KAKAO_NATIVE_KEY")
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
        buildConfig = true
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

    // Kakao Login
    implementation(libs.v2.user)

    // Retrofit2
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // LoggerUtils
    implementation(libs.logger)

    // LoggingInterceptor
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // ViewModel
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    // Lottie
    implementation(libs.lottie)

    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.1")
}
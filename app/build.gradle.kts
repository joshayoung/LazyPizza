import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization")
    id("org.jlleitschuh.gradle.ktlint") version "13.1.0"
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp")
//    id("androidx.room")
}

val localProperties =
    Properties().apply {
        load(File(rootProject.projectDir, "local.properties").inputStream())
    }

android {
    namespace = "com.joshayoung.lazypizza"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.joshayoung.lazypizza"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    ktlint {
        android = true
        ignoreFailures = false
        reporters {
            reporter(ReporterType.PLAIN)
            reporter(ReporterType.CHECKSTYLE)
            reporter(ReporterType.SARIF)
        }
    }

    buildTypes {
        debug {
            buildConfigField(
                "String",
                "API_ENDPOINT",
                localProperties.getProperty("API_ENDPOINT")
            )
            buildConfigField(
                "String",
                "API_PROJECT_ID",
                localProperties.getProperty("API_PROJECT_ID")
            )
            buildConfigField(
                "String",
                "BUCKET_ID",
                localProperties.getProperty("BUCKET_ID")
            )
            buildConfigField(
                "String",
                "DATABASE_ID",
                localProperties.getProperty("DATABASE_ID")
            )
            buildConfigField(
                "String",
                "AUTH_USERNAME",
                localProperties.getProperty("AUTH_USERNAME")
            )
            buildConfigField(
                "String",
                "AUTH_EMAIL",
                localProperties.getProperty("AUTH_EMAIL")
            )
            buildConfigField(
                "String",
                "AUTH_PASSWORD",
                localProperties.getProperty("AUTH_PASSWORD")
            )
            buildConfigField(
                "String",
                "AUTH_HEADER",
                localProperties.getProperty("AUTH_HEADER")
            )
            buildConfigField(
                "String",
                "PIZZA_COLLECTION_ID",
                localProperties.getProperty("PIZZA_COLLECTION_ID")
            )
            buildConfigField(
                "String",
                "DRINK_COLLECTION_ID",
                localProperties.getProperty("DRINK_COLLECTION_ID")
            )
            buildConfigField(
                "String",
                "SAUCES_COLLECTION_ID",
                localProperties.getProperty("SAUCES_COLLECTION_ID")
            )
            buildConfigField(
                "String",
                "ICE_CREAM_COLLECTION_ID",
                localProperties.getProperty("ICE_CREAM_COLLECTION_ID")
            )
            buildConfigField(
                "String",
                "TOPPINGS_COLLECTION_ID",
                localProperties.getProperty("TOPPINGS_COLLECTION_ID")
            )
            buildConfigField(
                "String",
                "MENU_ITEMS_COLLECTION_ID",
                localProperties.getProperty("MENU_ITEMS_COLLECTION_ID")
            )
        }

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
        compose = true
        buildConfig = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.coil)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.sdk.for1.android)
    implementation(libs.material3.adaptive)

    implementation(libs.androidx.datastore.datastore.preferences)
    implementation(libs.androidx.datastore)

    implementation(libs.symbol.processing)
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
}
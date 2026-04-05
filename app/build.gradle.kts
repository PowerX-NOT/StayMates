plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

import org.gradle.api.GradleException
import java.io.File

android {
    namespace = "com.android.staymates"
    compileSdk = 36

    buildFeatures {
        compose = true
        buildConfig = true
    }

    val dotEnvFile = File(rootProject.projectDir, ".env")
    val dotEnv: Map<String, String> = if (dotEnvFile.exists()) {
        dotEnvFile
            .readLines()
            .asSequence()
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .filterNot { it.startsWith("#") }
            .mapNotNull { line ->
                val idx = line.indexOf('=')
                if (idx <= 0) null else {
                    val key = line.substring(0, idx).trim()
                    val value = line.substring(idx + 1).trim().trim('"').trim('\'')
                    key to value
                }
            }
            .toMap()
    } else {
        emptyMap()
    }

    defaultConfig {
        applicationId = "com.android.staymates"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val supabaseUrl = dotEnv["SUPABASE_URL"] ?: System.getenv("SUPABASE_URL")
        val supabaseAnonKey = dotEnv["SUPABASE_ANON_KEY"] ?: System.getenv("SUPABASE_ANON_KEY")

        if (supabaseUrl.isNullOrBlank()) {
            throw GradleException("Missing SUPABASE_URL. Add it to .env (root) or export SUPABASE_URL before building.")
        }
        if (supabaseAnonKey.isNullOrBlank()) {
            throw GradleException("Missing SUPABASE_ANON_KEY. Add it to .env (root) or export SUPABASE_ANON_KEY before building.")
        }

        buildConfigField("String", "SUPABASE_URL", "\"$supabaseUrl\"")
        buildConfigField("String", "SUPABASE_ANON_KEY", "\"$supabaseAnonKey\"")
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
        // jvmTarget is configured via kotlin { compilerOptions { } } below
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.supabase.postgrest)
    implementation(libs.supabase.auth)
    implementation(libs.ktor.client.android)
    implementation(libs.kotlinx.serialization.json)

    debugImplementation(libs.androidx.compose.ui.tooling)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
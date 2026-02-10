plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.stability.analyzer)
    alias(libs.plugins.dependency.guard)
    alias(libs.plugins.module.graph.assert)
}

android {
    namespace = "com.example.carsolution"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.carsolution"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isDebuggable = true
            isMinifyEnabled = false
        }

        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
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
    buildFeatures {
        compose = true
    }
}

composeCompiler {
    stabilityConfigurationFiles.add(rootProject.layout.projectDirectory.file("compose-stability.conf"))
}

moduleGraphAssert {
    maxHeight = 4
    restricted = arrayOf(
        ":feature:.* -X> :data",       // feature는 data 직접 접근 금지
        ":feature:.* -X> :app",        // feature는 app 의존 금지
        ":domain -X> :data",           // domain은 data 모를 것
        ":domain -X> :feature:.*",     // domain은 feature 모를 것
        ":domain -X> :app",            // domain은 app 모를 것
        ":domain -X> :core:.*",        // domain은 core 모를 것
        ":data -X> :feature:.*",       // data는 feature 모를 것
        ":data -X> :app",             // data는 app 모를 것
    )
}

dependencyGuard {
    configuration("releaseRuntimeClasspath")
}

dependencies {
    // Modules
    implementation(project(":core:common"))
    implementation(project(":core:navigation"))
    implementation(project(":core:designsystem"))
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":feature:insurance"))
    implementation(project(":feature:fuel"))
    implementation(project(":feature:usedcar"))
    implementation(project(":feature:accident"))
    implementation(project(":feature:auth"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.kotlinx.serialization.json)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

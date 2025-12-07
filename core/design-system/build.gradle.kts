plugins {
    id("build-logic.android-library")
    alias(libs.plugins.compose)
}

android {
    namespace = "dev.aleksrychkov.scrooge.core.designsystem"

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(projects.core.resources)
}

dependencies {
    implementation(libs.android.material)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons)
    implementation(libs.androidx.ui.tooling.preview.android)

    implementation(libs.kotlinx.immutable.collections)

    debugImplementation(libs.androidx.ui.tooling)
}

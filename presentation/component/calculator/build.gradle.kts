plugins {
    id("build-logic.android-library")
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "dev.aleksrychkov.scrooge.presentation.component.calculator"
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(projects.core.designSystem)
    implementation(projects.core.resources)
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.ui.tooling.preview.android)

    implementation(libs.decompose.decompose)
    implementation(libs.decompose.extensionsComposeJetbrains)

    debugImplementation(libs.androidx.ui.tooling)
}

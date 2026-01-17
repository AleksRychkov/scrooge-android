plugins {
    id("build-logic.android-library")
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "dev.aleksrychkov.scrooge.presentation.screen.main.tabs"
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(projects.presentation.screen.hub)
    implementation(projects.presentation.screen.reportAnnualTotal)
    implementation(projects.presentation.screen.settings)

    implementation(projects.core.designSystem)
    implementation(projects.core.resources)
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons)

    implementation(libs.decompose.decompose)
    implementation(libs.decompose.extensionsComposeJetbrains)
}

plugins {
    id("build-logic.android-library")
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "dev.aleksrychkov.scrooge.presentation.screen.settings"
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(projects.core.designSystem)
    implementation(projects.core.entity)
    implementation(projects.core.resources)
    implementation(projects.core.router)

    implementation(projects.presentation.component.category)
    implementation(projects.presentation.component.settingsTheme)
    implementation(projects.presentation.component.settingsTransfer)
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)

    implementation(libs.decompose.decompose)
    implementation(libs.decompose.extensionsComposeJetbrains)
}

plugins {
    id("build-logic.android-library")
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "dev.aleksrychkov.scrooge.presentation.screen.root"
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(projects.presentation.screen.main.root)

    implementation(projects.core.designSystem)
    implementation(projects.core.di)
    implementation(projects.core.udfExtensions)

    implementation(projects.feature.theme.api)
    implementation(projects.feature.transfer.api)
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.splashscreen)

    implementation(libs.decompose.decompose)
    implementation(libs.decompose.extensionsComposeJetbrains)
}

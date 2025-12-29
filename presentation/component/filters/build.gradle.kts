plugins {
    id("build-logic.android-library")
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "dev.aleksrychkov.scrooge.presentation.component.filters"
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(projects.core.designSystem)
    implementation(projects.core.di)
    implementation(projects.core.resources)
    implementation(projects.core.udfExtensions)

    implementation(projects.feature.tag.api)
    implementation(projects.feature.transaction.api)
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.ui.tooling.preview.android)

    implementation(libs.decompose.decompose)
    implementation(libs.decompose.extensionsComposeJetbrains)

    implementation(libs.kotlinx.datetime)

    debugImplementation(libs.androidx.ui.tooling)
}

plugins {
    id("build-logic.android-library")
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "dev.aleksrychkov.scrooge.component.transactioncrud"
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(projects.core.designSystem)

    implementation(projects.common.entity)
    implementation(projects.common.resources)

    implementation(projects.core.router)
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)

    implementation(libs.decompose.decompose)
    implementation(libs.decompose.extensionsComposeJetbrains)
}

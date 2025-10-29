plugins {
    id("build-logic.android-library")
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "dev.aleksrychkov.scrooge.component.transactionform"
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(projects.component.category)
    implementation(projects.component.currency)
    implementation(projects.component.tag)

    implementation(projects.core.designSystem)
    implementation(projects.core.entity)
    implementation(projects.core.resources)
    implementation(projects.core.router)
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)

    implementation(libs.decompose.decompose)
    implementation(libs.decompose.extensionsComposeJetbrains)
}

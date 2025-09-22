plugins {
    id("build-logic.android-library")
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "dev.aleksrychkov.scrooge.component.mainTabs"
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(projects.component.report)
    implementation(projects.component.settings)
    implementation(projects.component.transactions)

    implementation(projects.common.resources)

    implementation(projects.core.designSystem)
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)

    implementation(libs.decompose.decompose)
    implementation(libs.decompose.extensionsComposeJetbrains)
}

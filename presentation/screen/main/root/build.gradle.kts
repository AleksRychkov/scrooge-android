plugins {
    id("build-logic.android-library")
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "dev.aleksrychkov.scrooge.presentation.screen.main.root"
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(projects.presentation.screen.limits)
    implementation(projects.presentation.screen.main.tabs)
    implementation(projects.presentation.screen.reportCategoryTotal)
    implementation(projects.presentation.screen.transactions)
    implementation(projects.presentation.screen.transactionForm)

    implementation(projects.core.entity)
    implementation(projects.core.router)
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)

    implementation(libs.decompose.decompose)
    implementation(libs.decompose.extensionsComposeJetbrains)

    implementation(libs.kotlinx.serialization.json)
}

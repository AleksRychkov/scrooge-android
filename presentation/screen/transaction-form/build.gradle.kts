plugins {
    id("build-logic.android-library")
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "dev.aleksrychkov.scrooge.presentation.screen.transactionform"
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(projects.presentation.component.category)
    implementation(projects.presentation.component.currency)
    implementation(projects.presentation.component.tags)

    implementation(projects.core.designSystem)
    implementation(projects.core.di)
    implementation(projects.core.entity)
    implementation(projects.core.resources)
    implementation(projects.core.router)
    implementation(projects.core.udfExtensions)

    implementation(projects.feature.currency.api)
    implementation(projects.feature.transaction.api)
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons)
    implementation(libs.androidx.ui.tooling.preview.android)

    implementation(libs.decompose.decompose)
    implementation(libs.decompose.extensionsComposeJetbrains)

    implementation(libs.kotlinx.datetime)

    debugImplementation(libs.androidx.ui.tooling)

    testImplementation(libs.junit)

    testRuntimeOnly(libs.junit.platform.launcher)
}

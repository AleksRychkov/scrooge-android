plugins {
    id("build-logic.android-library")
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "dev.aleksrychkov.scrooge.presentation.component.transactionlist"
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(projects.core.designSystem)
    implementation(projects.core.di)
    implementation(projects.core.entity)
    implementation(projects.core.resources)
    implementation(projects.core.router)
    implementation(projects.core.udfExtensions)

    implementation(projects.feature.category.api)
    implementation(projects.feature.transaction.api)
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.ui.tooling.preview.android)
    implementation(libs.androidx.paging)
    implementation(libs.androidx.paging.compose)

    implementation(libs.decompose.decompose)
    implementation(libs.decompose.extensionsComposeJetbrains)

    implementation(libs.kotlinx.datetime)

    debugImplementation(libs.androidx.ui.tooling)

    testImplementation(libs.junit)

    testRuntimeOnly(libs.junit.platform.launcher)
}

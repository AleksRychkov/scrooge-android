plugins {
    id("build-logic.android-library")
    alias(libs.plugins.compose)
}

android {
    namespace = "dev.aleksrychkov.scrooge.presentation.component.balancelinechart"
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(projects.core.designSystem)
    implementation(projects.core.di)
    implementation(projects.core.entity)
    implementation(projects.core.udfExtensions)
    implementation(projects.core.utils)
    implementation(projects.feature.reports.api)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.ui.tooling.preview.android)
    implementation(libs.decompose.decompose)
    implementation(libs.kotlinx.immutable.collections)
    implementation(libs.vico.compose)
    implementation(libs.vico.compose.m3)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testRuntimeOnly(libs.junit.platform.launcher)

    debugImplementation(libs.androidx.ui.tooling)
}

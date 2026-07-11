plugins {
    id("build-logic.android-library")
    alias(libs.plugins.compose)
}

android {
    namespace = "dev.aleksrychkov.scrooge.presentation.screen.charts"
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(projects.core.designSystem)
    implementation(projects.core.di)
    implementation(projects.core.entity)
    implementation(projects.core.resources)
    implementation(projects.core.udfExtensions)
    implementation(projects.presentation.component.balanceLineChart)
    implementation(projects.presentation.component.categoryLineChart)
    implementation(projects.presentation.component.filters)
    implementation(projects.feature.currency.api)
    implementation(projects.feature.category.api)
    implementation(projects.feature.transaction.api)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.ui.tooling.preview.android)
    implementation(libs.decompose.decompose)
    implementation(libs.decompose.extensionsComposeJetbrains)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testRuntimeOnly(libs.junit.platform.launcher)

    debugImplementation(libs.androidx.ui.tooling)
}

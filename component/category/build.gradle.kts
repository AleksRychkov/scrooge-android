plugins {
    id("build-logic.android-library")
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "dev.aleksrychkov.scrooge.component.category"
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

    implementation(projects.feature.category.api)
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)

    implementation(libs.decompose.decompose)
    implementation(libs.decompose.extensionsComposeJetbrains)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)

    testRuntimeOnly(libs.junit.platform.launcher)
}

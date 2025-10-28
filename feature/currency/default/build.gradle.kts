plugins {
    id("build-logic.android-library")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "dev.aleksrychkov.scrooge.feature.currency"
}

dependencies {
    implementation(projects.feature.currency.api)

    implementation(projects.core.di)
}

dependencies {
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)

    testRuntimeOnly(libs.junit.platform.launcher)
}

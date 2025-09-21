plugins {
    id("build-logic.android-library")
}

android {
    namespace = "dev.aleksrychkov.scrooge.feature.category"
}

dependencies {
    implementation(projects.common.database.api)

    implementation(projects.feature.category.api)

    implementation(projects.core.di)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)

    testRuntimeOnly(libs.junit.platform.launcher)
}

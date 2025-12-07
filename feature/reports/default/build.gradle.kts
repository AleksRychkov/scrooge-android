plugins {
    id("build-logic.android-library")
}

android {
    namespace = "dev.aleksrychkov.scrooge.feature.reports"
}

dependencies {
    implementation(projects.core.database.api)

    implementation(projects.feature.reports.api)

    implementation(projects.core.di)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.datetime)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)

    testRuntimeOnly(libs.junit.platform.launcher)
}

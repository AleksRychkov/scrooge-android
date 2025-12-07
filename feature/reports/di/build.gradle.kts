plugins {
    id("build-logic.android-library")
}

android {
    namespace = "dev.aleksrychkov.scrooge.feature.reports.di"
}

dependencies {
    implementation(projects.core.database.api)

    implementation(projects.core.di)

    implementation(projects.feature.reports.api)
    implementation(projects.feature.reports.default)
}

dependencies {
    implementation(libs.androidx.startup)
}
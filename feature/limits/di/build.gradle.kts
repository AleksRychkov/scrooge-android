plugins {
    id("build-logic.android-library")
}

android {
    namespace = "dev.aleksrychkov.scrooge.feature.limits.di"
}

dependencies {
    implementation(projects.core.database.api)

    implementation(projects.core.di)

    implementation(projects.feature.limits.api)
    implementation(projects.feature.limits.default)
}

dependencies {
    implementation(libs.androidx.startup)
}
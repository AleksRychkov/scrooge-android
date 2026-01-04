plugins {
    id("build-logic.android-library")
}

android {
    namespace = "dev.aleksrychkov.scrooge.feature.theme.di"
}

dependencies {
    implementation(projects.core.di)

    implementation(projects.feature.theme.api)
    implementation(projects.feature.theme.default)
}

dependencies {
    implementation(libs.androidx.startup)
}
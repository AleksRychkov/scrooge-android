plugins {
    id("build-logic.android-library")
}

android {
    namespace = "dev.aleksrychkov.scrooge.feature.currency.di"
}

dependencies {
    implementation(projects.core.di)

    implementation(projects.feature.currency.api)
    implementation(projects.feature.currency.default)
}

dependencies {
    implementation(libs.androidx.startup)
}
plugins {
    id("build-logic.android-library")
}

android {
    namespace = "dev.aleksrychkov.scrooge.feature.transfer.di"
}

dependencies {
    implementation(projects.core.di)

    implementation(projects.feature.transfer.api)
    implementation(projects.feature.transfer.default)
}

dependencies {
    implementation(libs.androidx.startup)
}
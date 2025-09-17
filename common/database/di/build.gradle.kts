plugins {
    id("build-logic.android-library")
}

android {
    namespace = "dev.aleksrychkov.scrooge.common.database.di"
}

dependencies {
    implementation(projects.common.database.api)
    implementation(projects.common.database.default)

    implementation(projects.core.di)
}

dependencies {
    implementation(libs.androidx.startup)
}
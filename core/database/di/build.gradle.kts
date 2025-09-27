plugins {
    id("build-logic.android-library")
}

android {
    namespace = "dev.aleksrychkov.scrooge.core.database.di"
}

dependencies {
    implementation(projects.core.database.api)
    implementation(projects.core.database.default)

    implementation(projects.core.di)
}

dependencies {
    implementation(libs.androidx.startup)
}
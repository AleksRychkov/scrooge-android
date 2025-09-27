plugins {
    id("build-logic.android-library")
}

android {
    namespace = "dev.aleksrychkov.scrooge.feature.category.di"
}

dependencies {
    implementation(projects.core.database.api)

    implementation(projects.core.di)

    implementation(projects.feature.category.api)
    implementation(projects.feature.category.default)
}

dependencies {
    implementation(libs.androidx.startup)
}
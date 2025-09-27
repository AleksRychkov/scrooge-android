plugins {
    id("build-logic.android-library")
}

android {
    namespace = "dev.aleksrychkov.scrooge.feature.tag.di"
}

dependencies {
    implementation(projects.core.database.api)

    implementation(projects.core.di)

    implementation(projects.feature.tag.api)
    implementation(projects.feature.tag.default)
}

dependencies {
    implementation(libs.androidx.startup)
}
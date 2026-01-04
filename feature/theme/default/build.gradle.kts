plugins {
    id("build-logic.android-library")
}

android {
    namespace = "dev.aleksrychkov.scrooge.feature.theme"
}

dependencies {
    implementation(projects.feature.theme.api)

    implementation(projects.core.di)
    implementation(projects.core.utils)
}

dependencies {
    implementation(libs.androidx.datastore.preferences)
}

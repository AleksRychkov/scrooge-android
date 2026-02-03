plugins {
    id("build-logic.android-library")
}

android {
    namespace = "dev.aleksrychkov.scrooge.feature.limits"
}

dependencies {
    implementation(projects.core.database.api)
    implementation(projects.core.di)
    implementation(projects.core.utils)

    implementation(projects.feature.limits.api)
}

plugins {
    id("build-logic.android-library")
}

android {
    namespace = "dev.aleksrychkov.scrooge.feature.category"
}

dependencies {
    implementation(projects.core.database.api)

    implementation(projects.feature.category.api)

    implementation(projects.core.di)
    implementation(projects.core.resources)
}

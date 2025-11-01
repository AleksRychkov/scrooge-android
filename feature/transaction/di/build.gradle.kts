plugins {
    id("build-logic.android-library")
}

android {
    namespace = "dev.aleksrychkov.scrooge.feature.transaction.di"
}

dependencies {
    implementation(projects.core.database.api)

    implementation(projects.core.di)

    implementation(projects.feature.transaction.api)
    implementation(projects.feature.transaction.default)
}

dependencies {
    implementation(libs.androidx.startup)
}
plugins {
    id("build-logic.android-library")
}

android {
    namespace = "dev.aleksrychkov.scrooge.feature.transfer"
}

dependencies {
    implementation(projects.feature.transfer.api)

    implementation(projects.core.di)
    implementation(projects.core.utils)
    implementation(projects.core.resources)
}

dependencies {
    implementation(libs.androidx.datastore.preferences)
}

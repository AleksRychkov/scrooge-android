plugins {
    id("build-logic.android-library")
}

android {
    namespace = "dev.aleksrychkov.scrooge.core.resources"
}

dependencies {
    implementation(projects.core.di)
}

dependencies {
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.startup)
}

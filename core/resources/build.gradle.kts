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
    implementation(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.material.icons)
    api(libs.androidx.compose.material.icons.extended)
}

plugins {
    id("build-logic.library")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(projects.core.entity)
}

dependencies {
    implementation(libs.decompose.decompose)
}

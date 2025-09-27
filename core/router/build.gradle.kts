plugins {
    id("build-logic.library")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(libs.decompose.decompose)
}

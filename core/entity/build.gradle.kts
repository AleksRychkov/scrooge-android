plugins {
    id("build-logic.library")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    api(libs.kotlinx.datetime)
    api(libs.kotlinx.immutable.collections)

    implementation(libs.kotlinx.serialization.json)
}

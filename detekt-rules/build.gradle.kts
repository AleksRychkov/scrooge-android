plugins {
    id("build-logic.library")
}

dependencies {
    implementation(libs.detekt.api) // use your Detekt version
    implementation(kotlin("stdlib"))
}

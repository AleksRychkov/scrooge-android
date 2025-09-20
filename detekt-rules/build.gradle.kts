plugins {
    id("build-logic.library")
}

dependencies {
    compileOnly(libs.detekt.api)

    implementation(libs.androidx.annotation)
    implementation(kotlin("stdlib"))

    testImplementation(libs.detekt.test)
    testImplementation(libs.junit)

    testRuntimeOnly(libs.junit.platform.launcher)
}

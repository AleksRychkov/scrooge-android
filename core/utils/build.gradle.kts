plugins {
    id("build-logic.library")
}

dependencies {
    testImplementation(libs.junit)
    testRuntimeOnly(libs.junit.platform.launcher)
}

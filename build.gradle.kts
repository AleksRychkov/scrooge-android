plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.sqldelight) apply false
}

allprojects {
    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

subprojects {
    apply(from = "$rootDir/config/detekt/detekt.gradle")
}

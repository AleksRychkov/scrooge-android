import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.sqldelight) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}

allprojects {
    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging {
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
            events(
                TestLogEvent.STARTED,
                TestLogEvent.FAILED,
                TestLogEvent.PASSED,
                TestLogEvent.SKIPPED,
            )
            showStandardStreams = true
        }
    }
}

subprojects {
    apply(from = "$rootDir/config/detekt/detekt.gradle")
}

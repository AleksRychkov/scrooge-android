// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.detekt)
}

allprojects {
    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

val detektFormatting = libs.detekt.formatting
subprojects {
    apply {
        plugin("io.gitlab.arturbosch.detekt")
    }

    detekt {
        config.from(rootProject.files("config/detekt/detekt.yml"))
        buildUponDefaultConfig = true
        parallel = true
    }

    dependencies {
        detektPlugins(detektFormatting)
    }
}

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

// todo: move to build logic as separate plugin?:
// https://github.com/LinX64/CoinCap/pull/26/files#diff-8cff73265af19c059547b76aca8882cbaa3209291406f52df1dafbbc78e80c46
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

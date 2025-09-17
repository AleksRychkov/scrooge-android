plugins {
    id("build-logic.android-library")
    id("app.cash.sqldelight")
}

android {
    namespace = "dev.aleksrychkov.scrooge.common.database"
}

dependencies {
    implementation(projects.common.database.api)

    implementation(projects.core.di)
}

dependencies {
    implementation(libs.bundles.sqldelight)

    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.junit)
    testRuntimeOnly(libs.junit.platform.launcher)
}

sqldelight {
    databases {
        create("Scrooge") {
            packageName = "dev.aleksrychkov.scrooge.common.database"
            generateAsync = true
        }
    }
}

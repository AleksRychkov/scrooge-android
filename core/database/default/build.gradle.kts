plugins {
    id("build-logic.android-library")
    id("app.cash.sqldelight")
}

android {
    namespace = "dev.aleksrychkov.scrooge.core.database"
}

dependencies {
    implementation(projects.core.database.api)

    implementation(projects.core.di)
}

dependencies {
    implementation(libs.bundles.sqldelight)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.datetime)

    testImplementation(libs.junit)
    testRuntimeOnly(libs.junit.platform.launcher)
}

sqldelight {
    databases {
        create("Scrooge") {
            packageName = "dev.aleksrychkov.scrooge.core.database"
            generateAsync = true
        }
    }
}

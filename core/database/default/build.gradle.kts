plugins {
    id("build-logic.android-library")
    id("app.cash.sqldelight")
}

android {
    namespace = "dev.aleksrychkov.scrooge.core.database"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.core.database.api)

    implementation(projects.core.di)
}

dependencies {
    implementation(libs.androidx.paging)

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

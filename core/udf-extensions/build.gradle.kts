plugins {
    id("build-logic.android-library")
}

android {
    namespace = "dev.aleksrychkov.scrooge.core.udfextensions"
}

dependencies {
    api(projects.core.udf)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.essenty.instance.keeper)
}

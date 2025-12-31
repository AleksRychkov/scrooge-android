plugins {
    id("build-logic.library")
}

dependencies {
    api(projects.core.entity)
}

dependencies {
    implementation(libs.androidx.paging.common)

    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.immutable.collections)
}

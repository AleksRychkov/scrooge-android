plugins {
    id("build-logic.library")
}

dependencies {
    api(projects.core.entity)
    api(projects.core.utils)
}

dependencies {
    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.immutable.collections)
}

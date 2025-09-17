plugins {
    id("build-logic.library")
}

dependencies {
    api(projects.common.entity)
}

dependencies {
    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.immutable.collections)
}

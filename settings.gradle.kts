@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("android-build-logic")

    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "scrooge-android"
include(":app")

// detekt
include(":detekt-rules")

// common
include(":common:entity")
include(":common:database:api")
include(":common:database:default")
include(":common:database:di")

// core
include(":core:di")
include(":core:utils")

// feature
include(":feature:tag:api")
include(":feature:tag:default")
include(":feature:tag:di")

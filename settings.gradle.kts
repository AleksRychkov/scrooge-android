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
include(":config:detekt:rules")

// common
//include(":common:entity")

// component
include(":component:main")
include(":component:main-tabs")
include(":component:report")
include(":component:root")
include(":component:settings")
include(":component:transactions")
include(":component:transaction-crud")

// core
include(":core:database:api")
include(":core:database:default")
include(":core:database:di")

include(":core:design-system")
include(":core:di")
include(":core:entity")
include(":core:resources")
include(":core:router")
include(":core:udf")
include(":core:udf-extensions")
include(":core:utils")

// feature
include(":feature:category:api")
include(":feature:category:default")
include(":feature:category:di")

include(":feature:currency:api")
include(":feature:currency:default")
include(":feature:currency:di")

include(":feature:tag:api")
include(":feature:tag:default")
include(":feature:tag:di")

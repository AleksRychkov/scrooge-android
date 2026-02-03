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
listOf(
    "category",
    "currency",
    "reports",
    "tag",
    "transaction",
    "transfer",
    "theme",
    "limits",
).forEach {
    include(":feature:$it:api")
    include(":feature:$it:default")
    include(":feature:$it:di")
}

// presentation component
include(":presentation:component:calculator")
include(":presentation:component:currency")
include(":presentation:component:category")
include(":presentation:component:filters")
include(":presentation:component:period-total")
include(":presentation:component:settings-theme")
include(":presentation:component:settings-transfer")
include(":presentation:component:tags")
include(":presentation:component:transaction-list")

// presentation screen
include(":presentation:screen:hub")
include(":presentation:screen:limits")
include(":presentation:screen:main:root")
include(":presentation:screen:main:tabs")
include(":presentation:screen:report-annual-total")
include(":presentation:screen:report-category-total")
include(":presentation:screen:root")
include(":presentation:screen:settings")
include(":presentation:screen:transactions")
include(":presentation:screen:transaction-form")
include(":presentation:screen:transfer")

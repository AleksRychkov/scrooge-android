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

// component
include(":component:category")
include(":component:currency")
include(":component:settings")
include(":component:tag")

include(":component:report:annual-total")
include(":component:report:category-total")
include(":component:report:period-total-embedded")
include(":component:report:root")

include(":component:transaction:form")
include(":component:transaction:list")
include(":component:transaction:root")

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
listOf("category", "currency", "reports", "tag", "transaction").forEach {
    include(":feature:$it:api")
    include(":feature:$it:default")
    include(":feature:$it:di")
}

// presentation
include(":presentation:component")
include(":presentation:screen:main:root")
include(":presentation:screen:main:tabs")
include(":presentation:screen:root")

plugins {
    id("build-logic.application")
}

android {
    buildFeatures {
        buildConfig = true
    }

    signingConfigs {
        create("_debug") {
            val keystore =
                project.layout.projectDirectory.file("debug.jks").asFile
            storeFile = keystore
            storePassword = "debug_"
            keyAlias = "debug_"
            keyPassword = "debug_"
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            signingConfig = signingConfigs.getByName("_debug")
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("_debug")
        }
    }
}

dependencies {
    implementation(libs.android.material)
}

dependencies {
    implementation(projects.component.root)

    implementation(projects.core.database.di)
    implementation(projects.core.resources)
    implementation(projects.feature.category.di)
    implementation(projects.feature.currency.di)
    implementation(projects.feature.reports.di)
    implementation(projects.feature.tag.di)
    implementation(projects.feature.transaction.di)
}

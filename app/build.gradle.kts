plugins {
    id("build-logic.application")
}

android {
    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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

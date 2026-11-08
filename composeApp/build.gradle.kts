plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.android.lint)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.compose.multiplatform) // Compose Multiplatform + resources
}

kotlin {
    android {
        namespace = "com.isep.composeapp"
        compileSdk {
            version = release(37)
        }
        minSdk = 24

        // Needed by Compose Resources (packaged in the APK built by :app).
        androidResources {
            enable = true
        }
    }

    // Apple Silicon simulator + real devices. One static framework for Xcode.
    listOf(iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp" // the name used in Swift: import ComposeApp
            isStatic = true
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                api(project(":shared"))

                // Compose Multiplatform (one set of libraries for Android AND iOS), declared in the version catalog
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.ui)
                implementation(libs.compose.components.resources)
                implementation(libs.jetbrains.lifecycle.viewmodel.compose)
                implementation(libs.jetbrains.lifecycle.runtime.compose)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }
}

// Where the generated Res class lives (import com.isep.composeapp.resources.Res)
compose.resources {
    packageOfResClass = "com.isep.composeapp.resources"
}

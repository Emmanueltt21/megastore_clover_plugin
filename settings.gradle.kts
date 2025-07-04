pluginManagement {
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

        // CRITICAL: Add Clover's Maven repository
        maven {
            url = uri("https://clover.jfrog.io/artifactory/clover-libs-release-local/")
            name = "CloverSDK"
        }

        // Alternative Clover repository (backup)
       /* maven {
            url = uri("https://nexus.dev.clover.com/content/repositories/snapshots/")
            name = "CloverSnapshots"
        }*/
    }
}

rootProject.name = "MegaStore"
include(":app")
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
    }
}

rootProject.name = "CarInfo"
include(":app")
include(":core:common")
include(":core:navigation")
include(":core:designsystem")
include(":domain")
include(":data")
include(":feature:insurance")
include(":feature:fuel")
include(":feature:vehiclespec")
include(":feature:accident")
include(":feature:auth")

rootProject.name = "revanced-hitster-db-patch"

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/revanced/registry")
            credentials {
                username = providers.gradleProperty("gpr.user").orNull 
                    ?: System.getenv("GITHUB_ACTOR")
                    ?: providers.gradleProperty("githubUsername").orNull
                    ?: "dummy" // Fallback to allow build to proceed (will fail if no auth)
                password = providers.gradleProperty("gpr.key").orNull 
                    ?: System.getenv("GITHUB_TOKEN")
                    ?: providers.gradleProperty("githubToken").orNull
                    ?: ""
            }
        }
    }
}

plugins {
    id("app.revanced.patches") version "1.0.0-dev.5"
}

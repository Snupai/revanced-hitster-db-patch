group = "app.snupai"

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-receivers")
    }
}

patches {
    about {
        name = "ReVanced HITSTER Database Patch"
        description = "Patches for HITSTER app to use custom gameset_database.json endpoint"
        source = "git@github.com:Snupai/revanced-hitster-db-patch.git"
        author = "Snupai"
        contact = "nya@snupai.me"
        website = "https://snupai.me"
        license = "GNU General Public License v3.0"
    }
}

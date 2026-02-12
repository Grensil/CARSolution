// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.stability.analyzer) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.dependency.guard) apply false
    alias(libs.plugins.module.graph.assert) apply false
    alias(libs.plugins.kotlinter) apply false
    alias(libs.plugins.android.cache.fix) apply false
    alias(libs.plugins.sonarlint) apply false
    alias(libs.plugins.kover) apply false
}

tasks.register<Copy>("installGitHooks") {
    from("scripts/pre-commit")
    into(".git/hooks")
    filePermissions { unix("rwxr-xr-x") }
}

tasks.named("prepareKotlinBuildScriptModel") {
    dependsOn("installGitHooks")
}

subprojects {
    // Kover — test coverage
    apply(plugin = "org.jetbrains.kotlinx.kover")

    // Detekt — Kotlin static analysis
    apply(plugin = "dev.detekt")

    // Kotlinter — Kotlin code formatter
    pluginManager.withPlugin("org.jetbrains.kotlin.android") {
        apply(plugin = "org.jmailen.kotlinter")
    }
    pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
        apply(plugin = "org.jmailen.kotlinter")
    }

    // SonarLint — code quality analysis
    pluginManager.withPlugin("org.jetbrains.kotlin.android") {
        apply(plugin = "name.remal.sonarlint")
    }
    pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
        apply(plugin = "name.remal.sonarlint")
    }

    // Android Cache Fix — build cache optimization
    pluginManager.withPlugin("com.android.application") {
        apply(plugin = "org.gradle.android.cache-fix")
    }
    pluginManager.withPlugin("com.android.library") {
        apply(plugin = "org.gradle.android.cache-fix")
    }
}

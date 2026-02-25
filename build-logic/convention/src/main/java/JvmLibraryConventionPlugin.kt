import dev.optimal.tracker.convention.configureKotlinJvm
import dev.optimal.tracker.convention.configureSpotlessForJvm
import dev.optimal.tracker.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.configuration.BuildFeatures
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import javax.inject.Inject

abstract class JvmLibraryConventionPlugin : Plugin<Project> {
    @get:Inject abstract val buildFeatures: BuildFeatures
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "org.jetbrains.kotlin.jvm")
            apply(plugin = "nowinandroid.android.lint")

            configureKotlinJvm()
            configureSpotlessForJvm()
            dependencies {
                "testImplementation"(libs.findLibrary("kotlin.test").get())
            }
        }
    }
}
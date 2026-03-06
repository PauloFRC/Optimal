
import com.android.build.api.dsl.ApplicationExtension
import dev.optimal.tracker.convention.configureKotlinAndroid
import dev.optimal.tracker.convention.configureSpotlessForAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

abstract class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.application")
            apply(plugin = "optimal.android.lint")

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 36
                testOptions.animationsDisabled = true
            }
            configureSpotlessForAndroid()
        }
    }
}

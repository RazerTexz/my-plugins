import com.aliucord.gradle.AliucordExtension
import com.android.build.gradle.LibraryExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidExtension

subprojects {
    pluginManager.apply("com.aliucord.plugin")
    pluginManager.apply("com.android.library")
    pluginManager.apply("org.jetbrains.kotlin.android")

    configure<AliucordExtension> {
        github("https://github.com/RazerTexz/My-plugins")
    }

    configure<LibraryExtension> {
        namespace = "com.github.razertexz"
        compileSdk = 36

        defaultConfig {
            minSdk = 21
        }

        buildFeatures {
            resValues = false
            shaders = false
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
        }
    }

    configure<KotlinAndroidExtension> {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_21
        }
    }

    dependencies {
        "compileOnly"("com.aliucord:Aliucord:2.11.0")
        "compileOnly"("com.discord:discord:126021")
        "compileOnly"("org.jetbrains.kotlin:kotlin-stdlib:1.5.31")
    }
}
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.multiplatform") version "1.9.22" apply false
    id("org.jetbrains.compose") version "1.6.10" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id ("maven-publish")
}
//val githubProperties = Properties()
//githubProperties.load(FileInputStream(rootProject.file("github.properties")))

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.github.MounyaK"
            artifactId = "statemachine"
            version = "1.0"
            //artifact("$buildDir/outputs/aar/statemachine-release.aar")

            afterEvaluate {
                from(components["release"])
            }
        }
    }

//    repositories {
//        maven {
//            name = "custom-compose-modules"
//            url = uri("https://maven.pkg.github.com/MounyaK/custom-compose-modules")
//
//            credentials {
//                /**Create github.properties in root project folder file with gpr.usr=GITHUB_USER_ID  & gpr.key=PERSONAL_ACCESS_TOKEN**/
//                username = (githubProperties["gpr.usr"] ?: System.getenv("GPR_USER")).toString()
//                password = (githubProperties["gpr.key"] ?: System.getenv("GPR_API_KEY")).toString()
//            }
//        }
//    }
}

android {
    namespace = "com.kamydev.statemachine"
    compileSdk = 34

    defaultConfig {
        minSdk = 25

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    publishing {
        singleVariant("release") {
            // if you don't want sources/javadoc, remove these lines
            withSourcesJar()
            withJavadocJar()
        }
    }
}



dependencies {

//    implementation("androidx.core:core-ktx:1.12.0")
//    implementation("androidx.appcompat:appcompat:1.6.1")
//    implementation("com.google.android.material:material:1.11.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}


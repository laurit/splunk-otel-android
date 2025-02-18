import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
}

val localProperties = Properties()
localProperties.load(FileInputStream(rootProject.file("local.properties")))

android {
    namespace = "com.splunk.android.sample"

    compileSdk = 33
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId = "com.splunk.android.sample"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    buildTypes {
        all {
            val realm = localProperties["rum.realm"] as String?
            val accessToken = localProperties["rum.access.token"] as String?
            resValue("string", "rum_realm", realm ?: "us0")
            resValue("string", "rum_access_token", accessToken ?: "dummyAuth")
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        release {
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true

        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }
}

val otelVersion = "1.26.0"
val otelAlphaVersion = "$otelVersion-alpha"
val otelInstrumentationVersion = "1.27.0"
val otelInstrumentationAlphaVersion = "$otelInstrumentationVersion-alpha"

dependencies {
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.webkit:webkit:1.7.0")
    implementation("androidx.browser:browser:1.5.0")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment:2.6.0")
    implementation("androidx.navigation:navigation-ui:2.6.0")
    implementation(project(":splunk-otel-android"))
    implementation(project(":splunk-otel-android-volley"))
    implementation("com.android.volley:volley:1.2.1")
    implementation("io.opentelemetry.instrumentation:opentelemetry-instrumentation-api:$otelInstrumentationVersion")
    implementation("io.opentelemetry.instrumentation:opentelemetry-instrumentation-api-semconv:$otelInstrumentationAlphaVersion")

    implementation("io.opentelemetry:opentelemetry-api-events:$otelAlphaVersion")
    implementation("io.opentelemetry:opentelemetry-sdk-logs:$otelAlphaVersion")
    implementation("io.opentelemetry:opentelemetry-sdk:$otelAlphaVersion")

    testImplementation("junit:junit:4.13.2")
}

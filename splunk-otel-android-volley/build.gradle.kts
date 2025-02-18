import java.time.Duration

plugins {
    id("com.android.library")
    id("splunk.android-library-conventions")
    id("splunk.errorprone-conventions")
}

android {
    namespace = "com.splunk.android.rum.volley"

    compileSdk = 33
    buildToolsVersion = "30.0.3"

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
        unitTests.isIncludeAndroidResources = true

        unitTests.all {
            it.testLogging.showStandardStreams = true
            it.testLogging {
                events("started", "passed", "failed")
            }
        }
    }
}

val otelVersion = "1.27.0"
val otelAlphaVersion = "$otelVersion-alpha"
val otelInstrumentationAlphaVersion = "1.28.0-alpha-SNAPSHOT"

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.navigation:navigation-fragment:2.6.0")
    compileOnly("com.android.volley:volley:1.2.1")

    implementation(project(":splunk-otel-android"))

    api(platform("io.opentelemetry:opentelemetry-bom:$otelVersion"))
    api("io.opentelemetry:opentelemetry-api")
    implementation("io.opentelemetry:opentelemetry-sdk")

    implementation(platform("io.opentelemetry:opentelemetry-bom-alpha:$otelAlphaVersion"))
    implementation("io.opentelemetry:opentelemetry-semconv")

    implementation(platform("io.opentelemetry.instrumentation:opentelemetry-instrumentation-bom-alpha:$otelInstrumentationAlphaVersion"))
    implementation("io.opentelemetry.instrumentation:opentelemetry-instrumentation-api")
    implementation("io.opentelemetry.instrumentation:opentelemetry-instrumentation-api-semconv")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("io.opentelemetry:opentelemetry-sdk-testing")
    testImplementation("org.robolectric:robolectric:4.10.3")
    testImplementation("org.mockito:mockito-core:5.3.1")
    testImplementation("androidx.test:core:1.5.0")
    testImplementation("com.google.mockwebserver:mockwebserver:20130706")
    testImplementation("com.android.volley:volley:1.2.1")
    testImplementation("org.apache.httpcomponents:httpclient:4.5.14")
}

tasks.withType<Test>().configureEach {
    timeout.set(Duration.ofMinutes(15))
}

extra["pomName"] = "Splunk Otel Android Volley"
description = "A library for instrumenting Android applications using Volley Library for Splunk RUM"

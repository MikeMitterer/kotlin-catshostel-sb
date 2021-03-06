@file:Suppress("PropertyName")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinCoroutines_version: String by project

val logback_version: String by project
val swagger_version: String by project
val openapi_version: String by project
val joda_version: String by project
val commons_lang_version: String by project

val jjwt_version: String by project

val mybatis_version: String by project
val sqlite_version: String by project
val postgres_version: String by project

val gson_version: String by project
val coroutines_test_version: String by project
val auth0_version: String by project
val jwks_version: String by project
val keycloak_version: String by project

plugins {
    id("org.springframework.boot") version "2.2.7.RELEASE"          // https://bit.ly/2OmNhdS
    id("io.spring.dependency-management") version "1.0.11.RELEASE"  // https://bit.ly/3tcNPSx

    id("java")

    kotlin("jvm") version "1.4.31"                  // Version: https://bit.ly/3qzDy0Y
    kotlin("plugin.spring") version "1.4.31"        // Version: https://bit.ly/3qzDy0Y
}

group = "at.mikemitterer.catshostel"
version = "0.0.1"

java.sourceCompatibility = JavaVersion.VERSION_1_8

// springBoot {
//     mainClassName = "at.mikemitterer.catshostel.ApplicationKt"
// }

// - SourceSet —————————————————————————————————————————————————————————————————————————————————————

kotlin {
    sourceSets {
        getByName("main") {
            kotlin.srcDirs("src")
        }
        getByName("test") {
            kotlin.srcDirs("test/unit")
        }
    }
}

java {
    sourceSets {
        getByName("main").java.srcDirs("src")
        getByName("test").java.srcDirs("test/unit")
    }
}

sourceSets["main"].resources.srcDirs("resources")
sourceSets["test"].resources.srcDirs("test/resources")

// - Dependencies ——————————————————————————————————————————————————————————————————————————————————

val developmentOnly: Configuration by configurations.creating
configurations {
    runtimeClasspath {
        extendsFrom(developmentOnly)
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
}

dependencies {
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    
    // Without coroutines
    implementation("org.springframework.boot:spring-boot-starter-web")

    // With coroutines!
    // implementation("org.springframework.boot:spring-boot-starter-webflux")

    implementation("org.springframework.boot:spring-boot-starter-freemarker")
    // implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutines_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$kotlinCoroutines_version")

    
    // There seems to be a problem with GSON:
    //      https://github.com/springdoc/springdoc-openapi/issues/624
    // implementation("org.springdoc:springdoc-openapi-core:1.1.49")
    // implementation("org.springdoc:springdoc-openapi-ui:${openapi_version}")
    // implementation("org.springdoc:springdoc-openapi-webmvc-core:${openapi_version}")
    // implementation("org.springdoc:springdoc-openapi-security:${openapi_version}")
    // implementation("org.springdoc:springdoc-openapi-kotlin:${openapi_version}")

    // They don't like GSON either
    //      https://github.com/springfox/springfox/issues/2758
    //      http://www.programmersought.com/article/7688804471/
    // but at least there is an Adapter...
    // implementation("io.springfox:springfox-swagger2:$swagger_version")
    // implementation("io.springfox:springfox-swagger-ui:$swagger_version")

    // implementation("ch.qos.logback:logback-classic:$logback_version")

    // AuthServer ------------------------------------------------------------------------------------------------------
    implementation("org.keycloak:keycloak-spring-boot-starter:$keycloak_version")

    // Datenbank -------------------------------------------------------------------------------------------------------
    implementation("org.mybatis:mybatis:$mybatis_version")
    implementation( "org.postgresql:postgresql:$postgres_version")
    implementation( "org.xerial:sqlite-jdbc:$sqlite_version")

    // Eigene libs -----------------------------------------------------------------------------------------------------
    // implementation "at.mikemitterer:webapp.communication:$version_webAppCommunication"

    // JWT -------------------------------------------------------------------------------------------------------------
    implementation("io.jsonwebtoken:jjwt-api:$jjwt_version")
    implementation("io.jsonwebtoken:jjwt-impl:$jjwt_version")
    implementation("io.jsonwebtoken:jjwt-gson:$jjwt_version")

    // Sonstiges -------------------------------------------------------------------------------------------------------
    implementation( "joda-time:joda-time:$joda_version")
    implementation("org.apache.commons:commons-lang3:$commons_lang_version")
    implementation("com.google.code.gson:gson:$gson_version")

    // Test ============================================================================================================

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutines_test_version")

    // JWT (auth0 testing)
    testImplementation("com.auth0:java-jwt:$auth0_version")
    testImplementation("com.auth0:jwks-rsa:$jwks_version")
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    launchScript()
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}


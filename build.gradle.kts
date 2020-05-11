@file:Suppress("PropertyName")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val logback_version: String by project
val swagger_version: String by project
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

plugins {
	id("org.springframework.boot") version "2.2.7.RELEASE"
	id("io.spring.dependency-management") version "1.0.9.RELEASE"

    id("java")
    
	kotlin("jvm") version "1.3.72"
	kotlin("plugin.spring") version "1.3.72"
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
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-freemarker")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("io.springfox:springfox-swagger2:$swagger_version")
    implementation("io.springfox:springfox-swagger-ui:$swagger_version")

    // implementation("ch.qos.logback:logback-classic:$logback_version")

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


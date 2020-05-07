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


plugins {
	id("org.springframework.boot") version "2.3.0.RC1"
	id("io.spring.dependency-management") version "1.0.9.RELEASE"
    
	kotlin("jvm") version "1.3.72"
	kotlin("plugin.spring") version "1.3.72"
}

group = "at.mikemitterer"
version = "0.0.1"

java.sourceCompatibility = JavaVersion.VERSION_1_8

// - SourceSet —————————————————————————————————————————————————————————————————————————————————————

kotlin.sourceSets["main"].kotlin.srcDirs("src")
kotlin.sourceSets["test"].kotlin.srcDirs("test/unit")

sourceSets["main"].resources.srcDirs("resources")
sourceSets["test"].resources.srcDirs("test/resources")

// - Dependencies ——————————————————————————————————————————————————————————————————————————————————

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-freemarker")
	implementation("org.springframework.boot:spring-boot-starter-websocket")

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

    // Test ============================================================================================================

    developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
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

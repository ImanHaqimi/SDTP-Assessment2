plugins {
    id("java")
    id ("org.springframework.boot") version ("3.2.4")
    id ("io.spring.dependency-management") version ("1.1.4")
}

group = "org.photogallery"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation ("org.springframework.boot:spring-boot-starter-web")
    testImplementation ("org.springframework.boot:spring-boot-starter-test")
    implementation ("org.springframework.boot:spring-boot-starter-webflux")
    implementation ("com.fasterxml.jackson.core:jackson-databind")
    testImplementation ("org.assertj:assertj-core:3.21.0")
    testImplementation ("com.squareup.okhttp3:mockwebserver:4.9.3")
}

tasks.test {
    useJUnitPlatform()
}
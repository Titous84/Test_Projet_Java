plugins {
    id("java")
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "com.ferme.bertbeach"
version = "1.0.0"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

dependencies {
    // === JUnit 5 ===
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

javafx {
    // Version JavaFX
    version = "23"

    // Modules nécessaires à ton projet
    modules = listOf(
        "javafx.controls",
        "javafx.fxml"
    )
}

application {
    // Point d’entrée de ton application JavaFX
    mainClass.set("com.ferme.bertbeach.App")
}

tasks.test {
    useJUnitPlatform()
}

tasks.named<JavaExec>("run") {
    // Permet l’utilisation de System.in dans la console IntelliJ
    standardInput = System.`in`
}

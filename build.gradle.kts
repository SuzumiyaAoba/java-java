allprojects {
  group = "com.github.suzumiyaaoba.java.java"
  version = "0.0.1-SNAPSHOT"
}

subprojects {
  apply(plugin = "java")
  apply(plugin = "application")

  repositories {
    mavenCentral()
  }

  tasks.withType(JavaExec::class.java) {
    jvmArgs = listOf("-Xms15g", "-Xmx15g")
  }
}


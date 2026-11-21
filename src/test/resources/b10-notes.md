Part A
1. Answer:
groupId: Identifies the organization, company, or project group. It usually uses reverse domain name notation (dev.danvega) to prevent name clashes
artifactId: Identifies the specific project, module, or library within that group (spring-kafka-stack-learning). It often forms the base name of the built file (like a .jar or .war)
version: Specifies the exact release or build version of the project or dependency (0.0.1-SNAPSHOT)SNAPSHOT in Maven indicates a development version that is not yet finalized. Unlike a release version, it can be updated without changing the version number.

2. Answer: Dependencies in pom.xml don't always need explicit versions because Maven can manage them through a parent POM or a BOM (Bill of Materials).
The version resolution works like this:
1. Project pom.xml inherits from spring-boot-starter-parent.
2. The parent inherits dependency management from spring-boot-dependencies.
3. spring-boot-dependencies defines the compatible Spring Kafka version, typically through the spring-kafka.version property.
4. Maven uses that version automatically when resolving spring-kafka
   The spring-kafka version is 4.0.5, while Spring Boot is 4.0.6. They don't need to match, because they are different libraries with independent versioning. Spring Boot manages compatible dependency versions through spring-boot-dependencies (BOM).
5. The compiler doesn't notice the conflict because we download library B from Maven Central as an already compiled .jar, not as source code. So the missing method in X 1.0 is only discovered when it is called at runtime.

4. Answer: <java.version>25</java.version> specifies the Java version used to compile the project

Part B
1. The Kafka and Web MVC starters use the default compile scope because their classes are needed to build and run the application. Docker Compose uses runtime because it is only needed when the application starts, not during compilation. The Kafka and Web MVC test starters use test because they provide testing utilities that are not needed in production.
The optional flag on Docker Compose prevents other projects from automatically inheriting that dependency.
2. Compilation failure.  package org.junit.jupiter.api does not exist

Part C
1.- tools.jackson.core:jackson-databind:jar:3.1.2:compile
    [INFO] |     +- com.fasterxml.jackson.core:jackson-annotations:jar:2.21:compile
    [INFO] |     \- tools.jackson.core:jackson-core:jar:3.1.2:compile

    +- com.fasterxml.jackson.core:jackson-databind:jar:2.21.2:test
    [INFO] |     |  |  \- com.fasterxml.jackson.core:jackson-core:jar:2.21.2:test
1. Jackson 3 comes into the project via spring-boot-docker-compose, among others. The jackson-databind version is 3.1.2, the groupId is tools.jackson.core. There is also spring-boot-starter-jackson via spring-boot-starter-webmvc.
If Jackson 3 were brought in only through spring-boot-docker-compose, its effective scope would be runtime. However, it is also available through a compile-scoped dependency path, which explains why Maven resolves it with compile scope.
com.fasterxml.jackson.annotation classes, such as JsonProperty and JsonIgnore, are available in production code because jackson-annotations has compile scope.
However, Jackson 2 classes such as com.fasterxml.jackson.databind.ObjectMapper are only available in tests. Production code should use tools.jackson.databind.ObjectMapper from Jackson 3.
2. Jackson 2 (com.fasterxml.jackson.core:jackson-databind:2.21.2) comes in via spring-boot-starter-kafka-test → spring-kafka-test → kafka-server with test scope.
The test in Exercise 2 compiled because Jackson 2 is available on the test classpath. Production code would not compile with this import, because test-scoped dependencies are not available when compiling src/main/java.
3. The project has roughly 105 transitive dependencies for 5 direct ones.

Part D:
./mvnw clean removes the target/ directory. ./mvnw compile compiles the production code and creates .class files in target/classes/. ./mvnw package -DskipTests creates a JAR file, compiles tests, but skips running them.

Spring Boot JAR differs from a regular library JAR because it includes the application, its dependencies, and a launcher. Running it with java -jar starts the Spring Boot application. With Docker running and Docker Compose configured, Kafka can start automatically.

./mvnw test also compiles the code because Maven follows a build lifecycle. It executes all required phases before test, including compile and test-compile.
The .jar.original file is the original JAR created by the Maven JAR Plugin. It contains the compiled application classes and resources.

The .jar file is a larger executable JAR created by the Spring Boot Maven Plugin (repackage goal). It includes the application, its dependencies, and the Spring Boot Loader, allowing it to run with java -jar.

The .jar.original file is kept as a backup of the original JAR during repackaging.
Questions to think about:
1. Why can you have two versions of a library in npm, but not in Maven?
   In npm, different versions of the same library can coexist in different node_modules folders.

In Maven, only one version of a dependency with the same groupId and artifactId is normally used within a single dependency graph. Maven picks the version using dependency mediation rules (usually the nearest one in the dependency tree).

Problems:
- Dependency conflicts: different libraries need different versions.
- Runtime errors, such as NoSuchMethodError or ClassNotFoundException.
- API incompatibility between versions.
2. Why do we need mvnw (Maven Wrapper)?
   mvnw lets you run the project with a pinned Maven version without installing Maven globally.

Benefits:
- All developers use the same Maven version.
- Fewer problems caused by different tool versions.
- Easier CI/CD setup.
  JavaScript analogy: mvnw is similar to running a local version of a tool via npx or npm exec, although the mechanisms are not exactly the same.
3. Why shouldn't you commit target/?
   target/ contains auto-generated files: .class files, JARs, test results, and other build artifacts.

Reasons:
- These files can be regenerated with ./mvnw package.
- They increase the size of the Git repository.
- They create unnecessary changes and potential merge conflicts.
- They may contain stale build output.
  That's why target/ is usually added to .gitignore:
  target/


In short: Maven handles version conflicts through dependency mediation, mvnw ensures a consistent Maven version across the team, and target/ is not committed because it contains generated files that can be reproduced.
To pin the Node.js version you can use .nvmrc or Volta, and for the package manager, Corepack with the packageManager field in package.json. This helps the whole team work with the same tool versions.

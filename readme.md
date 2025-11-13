first commit

<!-- to create maven project -->
mvn archetype:generate \
  -DgroupId=selenium.automation.framework \
  -DartifactId=selenium.automation.framework \
  -DarchetypeArtifactId=maven-archetype-quickstart \
  -DinteractiveMode=false

<!-- to run tests Using Maven (Recommended for CI/CD) -->
mvn test

<!-- to run tests Using TestNG Runner Class -->
mvn exec:java "-Dexec.mainClass=selenium.automation.framework.runners.TestNGRunner" "-Dexec.classpathScope=test"

1. What your project is:
<groupId>selenium.automation.framework</groupId>
<artifactId>selenium.automation.framework</artifactId>
<version>1.0-SNAPSHOT</version>

2. What dependencies to download:
<dependencies>
  <dependency>
    <groupId>org.testng</groupId>
    <artifactId>testng</artifactId>
    <version>7.11.0</version>
  </dependency>
</dependencies>

3. How to build/compile:
<properties>
  <maven.compiler.source>21</maven.compiler.source>
  <maven.compiler.target>21</maven.compiler.target>
</properties>

4. What plugins to run and when:
<plugin>
  <artifactId>maven-surefire-plugin</artifactId>
  <!-- Runs during 'test' phase -->
</plugin>


Maven Lifecycle:validate → compile → test-compile → test
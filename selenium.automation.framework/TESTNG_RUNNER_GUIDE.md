# TestNG Suite Runner Configuration

## Overview
Your Selenium automation framework now has a fully configured TestNG suite runner with custom listeners and organized test structure.

## Project Structure
```
selenium.automation.framework/
├── src/test/java/
│   ├── selenium/automation/framework/
│   │   ├── runners/
│   │   │   └── TestNGRunner.java          # Programmatic suite runner
│   │   ├── listeners/
│   │   │   └── TestListener.java          # Custom test listener
│   │   ├── tests/
│   │   │   ├── LoginTests.java            # Login test cases
│   │   │   └── HomePageTests.java         # Home page test cases
│   │   └── test.java                      # Sample test
│   └── resources/
│       └── testng.xml                      # TestNG suite configuration
├── pom.xml                                 # Maven configuration
└── target/
    └── surefire-reports/                   # Test reports location
```

## Running Tests

### Method 1: Using Maven (Recommended)
```bash
# Run all tests
mvn clean test

# Run only tests
mvn test

# Run with specific TestNG XML
mvn test -DsuiteXmlFile=src/test/java/resources/testng.xml
```

### Method 2: Using TestNG Runner Class
```bash
# Compile and run the TestNG Runner
mvn clean compile test-compile
java -cp "target/test-classes;target/classes;%USERPROFILE%\.m2\repository\org\testng\testng\7.11.0\*" selenium.automation.framework.runners.TestNGRunner
```

### Method 3: Right-click in IDE
- Right-click on `testng.xml` → Run As → TestNG Suite
- Right-click on test class → Run As → TestNG Test

## TestNG Configuration Features

### Current testng.xml Features:
- **Suite Name**: Selenium Automation Test Suite
- **Parallel Execution**: Disabled (can be enabled by setting parallel="tests" or "methods")
- **Test Listeners**: Custom TestListener for enhanced logging
- **Test Groups**: Organized into Login, Home Page, and Sample test suites
- **Preserve Order**: Tests run in the order specified

### Test Results Location
```
target/surefire-reports/
├── index.html                    # Main report
├── emailable-report.html         # Email-friendly summary
├── testng-results.xml            # XML results
└── TEST-TestSuite.xml            # JUnit format report
```

## Adding New Tests

### 1. Create a Test Class
```java
package selenium.automation.framework.tests;

import org.testng.Assert;
import org.testng.annotations.*;

public class NewTestClass {
    
    @BeforeClass
    public void setUp() {
        // Setup code
    }
    
    @Test(priority = 1, description = "Test description")
    public void testMethod() {
        // Test logic
        Assert.assertTrue(true);
    }
    
    @AfterClass
    public void tearDown() {
        // Cleanup code
    }
}
```

### 2. Add to testng.xml
```xml
<test name="New Test Suite">
    <classes>
        <class name="selenium.automation.framework.tests.NewTestClass"/>
    </classes>
</test>
```

## TestNG Annotations

- `@BeforeSuite` - Runs before all tests in the suite
- `@BeforeTest` - Runs before each `<test>` tag
- `@BeforeClass` - Runs before the first test method in a class
- `@BeforeMethod` - Runs before each test method
- `@Test` - Marks a method as a test method
- `@AfterMethod` - Runs after each test method
- `@AfterClass` - Runs after all test methods in a class
- `@AfterTest` - Runs after each `<test>` tag
- `@AfterSuite` - Runs after all tests in the suite

## Test Attributes

```java
@Test(
    priority = 1,                    // Execution order
    description = "Test description", // Test description
    enabled = true,                  // Enable/disable test
    dependsOnMethods = {"method1"},  // Dependency on other tests
    groups = {"smoke", "regression"}, // Test groups
    timeOut = 5000                   // Timeout in milliseconds
)
```

## Parallel Execution

To enable parallel execution, modify testng.xml:

```xml
<suite name="Suite" parallel="tests" thread-count="3">
    <!-- tests will run in parallel -->
</suite>
```

Options for `parallel`:
- `methods` - Test methods run in parallel
- `tests` - Test tags run in parallel
- `classes` - Test classes run in parallel
- `instances` - Test instances run in parallel

## Test Reports

After running tests, view reports:
1. Open `target/surefire-reports/index.html` in a browser
2. Or open `target/surefire-reports/emailable-report.html` for summary

## Custom Listener Features

The `TestListener` provides:
- Suite start/finish logging
- Test execution status (✓ Passed, ✗ Failed, ⊘ Skipped)
- Execution summary with counts
- Failure reason reporting

## Next Steps

1. Add Selenium WebDriver tests
2. Configure test data management
3. Add ExtentReports for better reporting
4. Set up CI/CD integration
5. Add Page Object Model classes

## Troubleshooting

### Tests not running?
- Verify testng.xml path in pom.xml is correct
- Ensure test classes are in the correct package
- Check that test methods have @Test annotation

### Reports not generated?
- Check `target/surefire-reports/` directory
- Ensure Maven build completes successfully
- Verify surefire plugin is configured in pom.xml

## Resources

- [TestNG Documentation](https://testng.org/doc/documentation-main.html)
- [Maven Surefire Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/)

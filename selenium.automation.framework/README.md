# Selenium Automation Framework

This is a comprehensive Selenium automation framework built with Java, Maven, and TestNG. It demonstrates best practices for web automation, including the Page Object Model (POM), multi-browser support, parallel execution, and advanced Selenium 4 features.

## 🚀 Features

- **Page Object Model (POM)**: Clean, maintainable, and reusable code.
- **TestNG**: Powerful test management with suites, parallel execution, and data-driven testing.
- **Maven**: Easy dependency management and build automation.
- **Multi-Browser Support**: Run tests on Chrome, Firefox, and Edge.
- **Parallel Execution**: Run tests in parallel to save time.
- **ExtentReports**: Beautiful, detailed, and interactive test reports with screenshots.
- **Data-Driven Testing**: Read test data from Excel files.
- **Selenium 4 Features**:
  - Relative Locators
  - Actions Class (hover, double-click, context-click)
  - Browser Console Logs Capture
- **Wait Strategies**:
  - Implicit, Explicit, and Fluent waits.
- **Retry Analyzer**: Automatically retry failed tests to handle flakiness.
- **Excel Writer Utility**: Write test results back to Excel.

## 🛠️ Tech Stack

- **Java 21**
- **Selenium WebDriver 4.38.0**
- **TestNG 7.11.0**
- **Maven 3.x**
- **WebDriverManager 6.1.0**
- **ExtentReports 5.1.2**
- **Apache POI 5.4.1**
- **SLF4J**

## 📂 Project Structure

```
.
├── pom.xml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── selenium
│   │   │       └── automation
│   │   │           └── framework
│   │   │               ├── App.java
│   │   │               ├── core
│   │   │               │   ├── ExcelReader.java
│   │   │               │   ├── ExtentManager.java
│   │   │               │   ├── ExtentTestManager.java
│   │   │               │   └── WebdriverUtil.java
│   │   │               ├── pages
│   │   │               │   ├── BasePage.java
│   │   │               │   ├── CartPage.java
│   │   │               │   ├── CheckoutPage.java
│   │   │               │   ├── HomePage.java
│   │   │               │   ├── LoginPage.java
│   │   │               │   └── ProductsPage.java
│   │   │               └── utils
│   │   │                   ├── ExcelWriterUtil.java
│   │   │                   └── RetryAnalyzer.java
│   │   └── resources
│   │       └── log4j2.xml
│   └── test
│       ├── java
│       │   └── selenium
│       │       └── automation
│       │           └── framework
│       │               ├── listeners
│       │               │   └── TestListener.java
│       │               └── tests
│       │                   ├── BaseTest.java
│       │                   ├── Cart
│       │                   │   └── TestCase13.java
│       │                   ├── Checkout
│       │                   │   ├── TestCase14.java
│       │                   │   ├── TestCase15.java
│       │                   │   ├── TestCase16.java
│       │                   │   └── TestCase23.java
│       │                   ├── Features
│       │                   │   ├── ActionsClassDemo.java
│       │                   │   ├── BrowserConsoleLogsDemo.java
│       │                   │   ├── RelativeLocatorsDemo.java
│       │                   │   └── WaitsDemo.java
│       │                   ├── Login
│       │                   │   ├── TestCase02.java
│       │                   │   ├── TestCase03.java
│       │                   │   └── TestCase04.java
│       │                   └── SignUp
│       │                       ├── TestCase01.java
│       │                       └── TestCase05.java
│       └── resources
│           ├── testdata
│           │   └── testdata.xlsx
│           └── testng.xml
├── target
└── test-output
```

## 🏁 Getting Started

### Prerequisites

- **Java 21** or higher
- **Maven 3.x**

### Installation

1. Clone the repository:
   ```bash
   git clone <repository-url>
   ```
2. Navigate to the project directory:
   ```bash
   cd selenium.automation.framework
   ```
3. Install dependencies:
   ```bash
   mvn clean install
   ```

## 🧪 Running Tests

### Run all tests:

```bash
mvn clean test
```

### Run a specific test suite:

```bash
mvn test -DsuiteXmlFile=src/test/resources/testng.xml
```

### Run tests on a specific browser:

```bash
# Run on Chrome
mvn test -Dbrowser=chrome

# Run on Firefox
mvn test -Dbrowser=firefox

# Run on Edge
mvn test -Dbrowser=edge
```

### Run a specific test class:

```bash
mvn test -Dtest=ActionsClassDemo
```

## 📊 Reports

After running the tests, you can find the Extent Report in the `test-output/reports` directory.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

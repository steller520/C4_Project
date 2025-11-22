package selenium.automation.framework.runners;

import org.testng.TestNG;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * TestNG Suite Runner
 * This class allows you to run TestNG suites programmatically
 */
/**
 * Programmatic TestNG suite runner allowing invocation without IDE/TestNG plugin.
 * Supports default testng.xml path plus overloaded method for custom parameters.
 */
@SuppressWarnings("null")
public class TestNGRunner {

    public static void main(String[] args) {
        TestNGRunner runner = new TestNGRunner();
        runner.runTestNGSuite();
    }

    /** Run default test suite from canonical testng.xml path. */
    public void runTestNGSuite() {
        TestNG testNG = new TestNG();
        
        // Create a list to hold suite files
        List<String> suites = new ArrayList<>();
        
        // Add testng.xml file path
        String testngXmlPath = "src/test/resources/testng.xml";
        File testngXml = new File(testngXmlPath);
        
        if (testngXml.exists()) {
            suites.add(testngXmlPath);
            testNG.setTestSuites(suites);
            
            // Set output directory for reports
            testNG.setOutputDirectory("test-output");
            
            // Run the test suite
            System.out.println("Running TestNG Suite: " + testngXmlPath);
            testNG.run();
            
            // Print results
            System.out.println("\n========== Test Execution Summary ==========");
            System.out.println("Total Tests Run: " + (testNG.getStatus()));
            System.out.println("Reports generated in: test-output directory");
            System.out.println("============================================\n");
        } else {
            System.err.println("TestNG XML file not found: " + testngXmlPath);
            System.err.println("Please ensure testng.xml exists in the specified location.");
        }
    }

    /**
     * Run suite pointing to provided xml and custom output directory.
     * @param xmlPath path to testng descriptor
     * @param outputDir folder for generated reports
     */
    public void runTestNGSuite(String xmlPath, String outputDir) {
        TestNG testNG = new TestNG();
        List<String> suites = new ArrayList<>();
        suites.add(xmlPath);
        testNG.setTestSuites(suites);
        testNG.setOutputDirectory(outputDir);
        testNG.run();
    }
}

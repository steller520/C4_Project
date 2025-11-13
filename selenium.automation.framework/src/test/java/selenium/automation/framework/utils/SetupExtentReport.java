package selenium.automation.framework.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class SetupExtentReport {
    protected static ExtentReports extentReports;
    protected static ExtentTest extentTest;
    protected static ExtentSparkReporter sparkReporter;
    public static void setupReport() {
        // Placeholder for Extent Report setup logic
        System.out.println("Extent Report setup completed.");
        sparkReporter = new ExtentSparkReporter("test-output/ExtentReport.html");
        extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);
        extentTest = extentReports.createTest("Automation Test Report");
    }

    public static void flushReport() {
        // Placeholder for Extent Report flush logic
        System.out.println("Extent Report flushed.");
        if (extentReports != null) {
            extentReports.flush();
        }
    }
}

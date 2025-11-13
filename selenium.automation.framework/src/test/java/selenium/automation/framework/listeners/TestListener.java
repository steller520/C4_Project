package selenium.automation.framework.listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG Listener for test execution events
 */
public class TestListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        System.out.println("========================================");
        System.out.println("Test Suite Started: " + context.getName());
        System.out.println("========================================");
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("\n========================================");
        System.out.println("Test Suite Finished: " + context.getName());
        System.out.println("Total Tests: " + context.getAllTestMethods().length);
        System.out.println("Passed: " + context.getPassedTests().size());
        System.out.println("Failed: " + context.getFailedTests().size());
        System.out.println("Skipped: " + context.getSkippedTests().size());
        System.out.println("========================================\n");
    }

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("Test Started: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("✓ Test Passed: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("✗ Test Failed: " + result.getMethod().getMethodName());
        System.out.println("Reason: " + result.getThrowable().getMessage());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("⊘ Test Skipped: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("Test Failed but within success percentage: " + result.getMethod().getMethodName());
    }
}

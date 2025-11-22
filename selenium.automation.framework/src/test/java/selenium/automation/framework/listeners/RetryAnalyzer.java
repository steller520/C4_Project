package selenium.automation.framework.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * TestNG Retry Analyzer - automatically retries failed tests to mitigate flakiness.
 * Strategy: Retry up to MAX_RETRY_COUNT times for any FAILURE status.
 * Notes:
 *  - Retries are synchronous; no backoff implemented.
 *  - Prints status to stdout for visibility (could be integrated with logger/report).
 */
public class RetryAnalyzer implements IRetryAnalyzer {
    private int retryCount = 0;
    private static final int MAX_RETRY_COUNT = 2;

    /**
     * Determines if a test should be retried based on retry threshold.
     * @param result current test result object
     * @return true if retry should occur
     */
    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            System.out.println("Retrying test '" + result.getName() + 
                             "' with status " + getResultStatusName(result.getStatus()) + 
                             " for the " + (retryCount + 1) + " time(s).");
            retryCount++;
            return true;
        }
        return false;
    }

    /**
     * Maps numeric status code to human-readable string.
     */
    private String getResultStatusName(int status) {
        switch (status) {
            case ITestResult.SUCCESS:
                return "SUCCESS";
            case ITestResult.FAILURE:
                return "FAILURE";
            case ITestResult.SKIP:
                return "SKIP";
            default:
                return "UNKNOWN";
        }
    }
}

package selenium.automation.framework.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * Utility for capturing WebDriver screenshots with timestamped filenames.
 * Returns only the filename (not full path) to simplify relative path usage in reports.
 */
public class ScreenShotUtil  {
    /**
     * Captures a PNG screenshot, creating the output directory if needed.
     * Swallows internal exceptions to avoid failing tests on ancillary reporting.
     * @param driver active WebDriver implementing TakesScreenshot
     * @param screenshotName logical base name (test or step identifier)
     * @return generated filename (timestamp appended)
     * @throws IOException propagation for file IO issues during copy
     */
    public static String takeScreenshot(WebDriver driver, String screenshotName) throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = screenshotName + "_" + timestamp + ".png";
        String screenshotDir = System.getProperty("user.dir") + "/test-output/screenshots/";
        String screenshotPath = screenshotDir + fileName;
        try {
            File dir = new File(screenshotDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            File destination = new File(screenshotPath);
            FileUtils.copyFile(source, destination);
            System.out.println("Screenshot saved: " + screenshotPath);
        } catch (Exception e) {
            System.out.println("Exception while taking screenshot: " + e.getMessage());
        }
        return fileName;
    }
}

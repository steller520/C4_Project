package selenium.automation.framework.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenShotUtil  {
    public static String takeScreenshot(WebDriver driver, String screenshotName) throws IOException {
        // Placeholder for screenshot capture logic
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = screenshotName + "_" + timestamp + ".png";
        String screenshotDir = System.getProperty("user.dir") + "/test-output/screenshots/";
        String screenshotPath = screenshotDir + fileName;
        
        try {
            // Create screenshots directory if it doesn't exist
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
        
        // Return just the filename for relative path construction
        return fileName;
    }
}

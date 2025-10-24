package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.io.IOException;

public class BaseTest {

    protected WebDriver driver;
    protected ExtentReports extent;
    protected ExtentTest test;

    @BeforeSuite
    public void setupExtent() {
        extent = reports.ExtentReporter.getExtentReport();
    }

    public void initializeDriver() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }
    
    public void closeDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    // Screenshot of full page
    public String takeFullPageScreenshot(String fileName) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File sourceFile = ts.getScreenshotAs(OutputType.FILE);
            
            // Create Screenshot directory if it doesn't exist
            String screenshotDir = System.getProperty("user.dir") + "/test-output/" + "Screenshot/" ;
            File directory = new File(screenshotDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            String filePath = screenshotDir + fileName;
            File targetFile = new File(filePath);
            FileUtils.copyFile(sourceFile, targetFile);
//            System.out.println("Screenshot saved: " + filePath);
            return filePath;

        } catch (Exception e) {
            System.err.println("Error taking full page screenshot: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    //To attach screenshots in extent report
    public void attachFileScreenshot(String filePath, String description) throws IOException {
        if (test != null && filePath != null) {
			test.log(Status.INFO, description, 
			    MediaEntityBuilder.createScreenCaptureFromPath(filePath).build());
        }
    }

    @AfterMethod
    public void tearDownTest(ITestResult result) {
        if (test != null) {
            if (result.getStatus() == ITestResult.FAILURE) {
                test.fail(result.getThrowable());
                String filePath = takeFullPageScreenshot("Failure_Screenshot.png");
                try {
					attachFileScreenshot(filePath, "Screenshot on Failure (Full Page)");
				} catch (IOException e) {
					e.printStackTrace();
				}
            } else if (result.getStatus() == ITestResult.SUCCESS) {
                test.pass("Test passed successfully.");
            } else if (result.getStatus() == ITestResult.SKIP) {
                test.skip(result.getThrowable());
            }
            closeDriver(); 
        }
    }

    @AfterSuite
    public void tearDownExtent() {
        if (extent != null) {
            extent.flush();
        }
    }
}

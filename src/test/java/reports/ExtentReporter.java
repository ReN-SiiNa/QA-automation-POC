package reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentReporter {
    
    private static ExtentReports extent;
    
    public static ExtentReports getExtentReport() {
        if (extent == null) {
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            String reportName = "Test-Report-" + timeStamp + ".html";
            
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(System.getProperty("user.dir") + "/test-output/" + reportName);
            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            
            sparkReporter.config().setDocumentTitle("Registration Form Automation Report");
            sparkReporter.config().setReportName("Registration Form Test Results");
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setTimeStampFormat("MMMM dd, yyyy, hh:mm a '('zzz')'");
            
            extent.setSystemInfo("Application", "DemoQA Practice Form");
            extent.setSystemInfo("Operating System", System.getProperty("os.name"));
            extent.setSystemInfo("User Name", System.getProperty("user.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        }
        return extent;
    }
}
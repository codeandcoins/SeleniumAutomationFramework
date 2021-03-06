package com.testClasses;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.utilities.ReadConfig;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseClass {
    public static WebDriver driver;
    public static Logger logger;
    public static ExtentTest extendReportLogger;

    ExtentSparkReporter spark;
    public static ExtentReports extent;

    ReadConfig readconfig = new ReadConfig();
    public String baseURL = readconfig.gerConfig("baseURL");
    public String username = readconfig.gerConfig("username");
    public String password = readconfig.gerConfig("password");

    @BeforeSuite
    public void suitStartSetup() {
        System.out.println("Executing beforesuit");
        logger = Logger.getLogger("LoggerName");
        PropertyConfigurator.configure("Configuration/log4j.properties");
        createExtendReport();
    }

    @Parameters("browser")
    @BeforeMethod
    public void setup(@Optional("chrome") String browser) {

        if (browser.equals("chrome")) {
            System.setProperty("webdriver.chrome.driver", readconfig.gerConfig("chromepath"));
            driver = new ChromeDriver();
            driver.manage().window().maximize();
        } else {
            throw new RuntimeException("The specified browser is not supported by the framework : " + browser);
        }

    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

    //Create Report
    public void createExtendReport()  {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());//time stamp
        String repName = "ExtentReports_" + timeStamp + ".html";
        ExtentSparkReporter spark = new ExtentSparkReporter(System.getProperty("user.dir") + "/TestResults/" + repName);
        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Host name", "localhost");
        extent.setSystemInfo("Environemnt", "QA");
        extent.setSystemInfo("user", "Anandhu");
        spark.config().setDocumentTitle("Test Execution Results"); // Tile of report
        spark.config().setReportName("Functional Test Automation Report"); // name of the report
        spark.config().setTheme(Theme.STANDARD);
    }

    //Take screenshot
    public void captureScreen(WebDriver driver, String screenshotFullPath) throws IOException {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        File target = new File(screenshotFullPath);
        FileUtils.copyFile(source, target);
        System.out.println("Screenshot taken");
    }

}

package common;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import com.aventstack.extentreports.MediaEntityBuilder;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.LogUtil;
import utils.MailUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public class BaseTest {
    protected WebDriver driver;
    protected static ExtentReports extent;
    protected static ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();
    protected static String reportPath;

    @BeforeSuite
    public void setupReport() {
        reportPath = "test-output/ExtentReport_" +
                new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss").format(new Date()) + ".html";

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setDocumentTitle("自动化测试报告");
        sparkReporter.config().setReportName("测试执行报告");

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("系统", System.getProperty("os.name"));
        extent.setSystemInfo("浏览器", "Chrome");
    }

    @BeforeMethod
    public void setupDriver(Method method) {
        System.setProperty("webdriver.chrome.driver",
                Paths.get("E:\\chens\\chromedriver-win64\\chromedriver.exe").toString());
//        driver = new ChromeDriver();
//        // ✅ 设置窗口最大化
//        driver.manage().window().maximize();

        // ✅ 设置无头模式（不弹出浏览器窗口）
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // 启用无头模式
        options.addArguments("--disable-gpu"); // Windows 推荐加上，防止图形崩溃
        options.addArguments("--window-size=1920,1080"); // 设置窗口尺寸，避免元素不可见
        options.addArguments("--remote-allow-origins=*"); // 防止跨域问题（必要时）
        driver = new ChromeDriver(options);
        ExtentTest test = extent.createTest(method.getName());
        testThread.set(test);
    }

    @AfterMethod
    public void tearDownDriver(ITestResult result) {
        ExtentTest test = testThread.get();

        if (result.getStatus() == ITestResult.FAILURE) {
            String base64 = saveAndGetScreenshotBase64(result.getName());
            if (base64 != null) {
                // 直接将HTML代码插入报告
                test.fail(result.getThrowable());
               // 使用HTML标签直接展示500px宽度的截图，并且点击图片放大
                String imageTag = "<img src='data:image/png;base64," + base64 + "' width='400px' />";
                Markup m = MarkupHelper.createLabel(imageTag, ExtentColor.WHITE);
                test.info(m);
                test.info("截图", MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());
            } else {
                test.fail(result.getThrowable());
            }

            LogUtil.error("测试失败: " + result.getThrowable().getMessage());

        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.pass("测试通过");
            LogUtil.info("测试通过: " + result.getName());

        } else {
            test.skip("测试跳过");
        }

        if (driver != null) {
            driver.quit();
        }
    }

    @AfterSuite
    public void generateReport() {
        extent.flush();
        if (new File(reportPath).exists()) {
            MailUtil.sendReportMail(reportPath);
        } else {
            LogUtil.error("报告文件未找到，无法发送邮件！");
        }
    }

    // ✅ 截图并返回Base64，同时保存本地图片
    public String saveAndGetScreenshotBase64(String methodName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String screenshotDir = "test-output/screenshots/";
        String fileName = methodName + "_" + timestamp + ".png";
        String fullPath = screenshotDir + fileName;

        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            File dest = new File(fullPath);
            dest.getParentFile().mkdirs();
            Files.copy(screenshot.toPath(), dest.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            byte[] imageBytes = Files.readAllBytes(dest.toPath());
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            LogUtil.error("截图保存或编码失败: " + e.getMessage());
            return null;
        }
    }

    public ExtentTest getTest() {
        return testThread.get();
    }
}

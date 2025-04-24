import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import utils.LogUtil;

import java.nio.file.Paths;

public class DemoTest {

    ExtentReports extent;
    ExtentTest test;
    WebDriver driver;

    @BeforeSuite
    public void setupReport() {
        // 使用 ExtentSparkReporter 替代 ExtentHtmlReporter
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("test-output/ExtentReport.html");
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setDocumentTitle("自动化测试报告");
        sparkReporter.config().setReportName("百度打开测试");

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("操作系统", System.getProperty("os.name"));
        extent.setSystemInfo("测试工具", "Selenium");
        extent.setSystemInfo("浏览器", "Chrome");

        LogUtil.info("✅ 报告初始化完成！");
    }

    @BeforeMethod
    public void setupDriver() {
        System.setProperty("webdriver.chrome.driver",
                Paths.get("E:\\chens\\chromedriver-win64\\chromedriver.exe").toString());
        driver = new ChromeDriver();

        LogUtil.info("✅ 启动浏览器！");
    }

    @Test
    public void openBaidu() {
        test = extent.createTest("打开百度首页");
        driver.get("https://www.baidu.com");
        test.pass("成功打开百度");
    }

    @Test
    public void asserEquals() {
        test=extent.createTest("打开百度首页并判断按钮字段文本");
        driver.get("https://www.baidu.com");
        //*[@id="su"]
        String buttonText  = driver.findElement(By.xpath("//*[@id=\"su\"]")).getAttribute("value");
        test.info("按钮文本为: " + buttonText);
        Assert.assertEquals(buttonText,"百度一下","按钮文本不正确");

    }


    @AfterMethod
    public void teardown() {
        driver.quit();
        test.info("关闭浏览器");
        LogUtil.info("✅ 关闭浏览器！");
    }

    @AfterSuite
    public void tearDownReport() {
        extent.flush();
        LogUtil.info("✅ 报告写入完成！");
    }
}

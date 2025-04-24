package utils;

import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class AssertUtil {
    public static void assertEqualsWithReport(WebDriver driver, ExtentTest test,
                                              String actual, String expected, String message) {
        try {
            Assert.assertEquals(actual, expected);
            test.pass("断言成功：" + message + "，实际值为：" + actual);
        } catch (AssertionError e) {
            String screenshotPath = ScreenshotUtil.capture(driver); // 自定义截图
            test.fail("断言失败：" + message + "，实际值为：" + actual + "，期望值为：" + expected)
                    .addScreenCaptureFromPath(screenshotPath);
            throw e; // 继续抛出，确保测试失败被标记
        }
    }
}

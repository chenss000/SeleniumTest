import common.BaseTest;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DemoTesttwo extends BaseTest {
    @Test
    public void testBaiduSearchButton() {
        getTest().info("打开百度首页");
        driver.get("https://www.baidu.com");

        String buttonText = driver.findElement(By.xpath("//*[@id='su']")).getAttribute("value");
        getTest().info("按钮文本为：" + buttonText);
        Assert.assertEquals(buttonText, "百度一下ss", "按钮文本不匹配！");
    }
}

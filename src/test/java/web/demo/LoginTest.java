package web.demo;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LoginTest {

    ExtentReports extent;
    ExtentTest extentTest;
    WebDriver driver;

    @Test
    public void webdriver() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver",
                Paths.get("E:\\chens\\chromedriver-win64\\chromedriver.exe")
                        .toString());
        driver = new ChromeDriver();
        driver.get("http://127.0.0.1:8080/login");
        WebElement username = driver.findElement(By.xpath("/html/body/div/form/p[2]/input"));
        WebElement password = driver.findElement(By.xpath("/html/body/div/form/p[3]/input"));
        WebElement login = driver.findElement(By.xpath("/html/body/div/form/p[4]/button"));
        username.sendKeys("zhangsan");
        password.sendKeys("zhangsan123456");
        login.click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"my-button2\"]")));
            el.click();  // 正常点击
        } catch (TimeoutException e) {
            System.out.println("元素未找到或不可见！");
            e.printStackTrace();
        }
        driver.findElement(By.xpath("//*[@id=\"my-button2\"]")).click();
        Alert alert = driver.switchTo().alert();
        System.out.println("alert.getText() = " + alert.getText());
        alert.accept();
        driver.quit();

//        Thread.sleep(10000);
//        WebDriver.Navigation navigate = driver.navigate();
//        navigate.back();
//        navigate.forward();
//        navigate.refresh();
//        WebElement motoz = driver.findElement(By.linkText("在新窗口打开moto Z页面"));
//        motoz.click();
//        String[] windows = new String[driver.getWindowHandles().size()];
//        Thread.sleep(2000);
//        driver.getWindowHandles().toArray(windows);
//        Thread.sleep(2000);
//        driver.switchTo().window(windows[0]);
//        Thread.sleep(2000);
//        driver.close();
//        Thread.sleep(2000);
//        driver.switchTo().window(windows[1]);
//        Thread.sleep(2000);
//        driver.quit();
//        Thread.sleep(5000);
//        List<WebElement> radio = driver.findElements(By.name("radio"));
//        for (int i = 0; i < radio.size(); i++) {
//            if (radio.get(i).isSelected()){
//                System.out.println("第"+(i+1)+"个单选框被选中了！");
//            }
//        }
//        WebElement apple = driver.findElement(By.xpath("/html/body/nav/ul/li[1]/a"));
//        WebElement a = driver.findElement(By.xpath("/html/body/nav/ul/li[1]/ul/li/a"));
//        Actions actions = new Actions(driver);
//        actions.moveToElement(apple);
//        actions.moveToElement(a);
//        actions.click();
//        actions.perform();

//        WebElement tx = driver.findElement(By.xpath("//*[@id=\"id1\"]/p[10]/input"));
//        tx.sendKeys("123456");
//        System.out.println("tx = " + tx.getAttribute("value"));
//        tx.sendKeys(Keys.BACK_SPACE);
//        System.out.println("tx = " + tx.getAttribute("value"));


    }
}

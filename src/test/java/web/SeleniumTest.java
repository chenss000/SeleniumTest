package web;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.nio.file.Path;
import java.nio.file.Paths;

public class SeleniumTest {
    public static void main(String[] args) throws InterruptedException {
//        System.setProperty("webdriver.chrome.driver",
//                Paths.get("E:\\chens\\chromedriver-win64\\chromedriver.exe").toString());
        System.setProperty("webdriver.chrome.driver", Paths.get("E:\\chens\\chromedriver-win64\\chromedriver.exe").toString());
        WebDriver webDriver = new ChromeDriver();
        webDriver.get("https://www.baidu.com");
        WebElement su = webDriver.findElement(By.id("su"));
        System.out.println("通过ID定位到 = " + su.getAttribute("value"));
        WebElement name = webDriver.findElement(By.name("description"));
        System.out.println("通过name定位到 = " + name.getAttribute("content"));
        WebElement element = webDriver.findElement(By.xpath("//*[@id=\"kw\"]"));
        element.sendKeys("美少女");
        su.click();

    }
}

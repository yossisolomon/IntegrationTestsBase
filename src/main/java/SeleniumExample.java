import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


public class SeleniumExample {
    private static final Logger LOGGER = Logger.getLogger(SeleniumExample.class.getName());
    // Set the path to the chromedriver.exe in your local machine
    private static final String DRIVER_PATH = "D:\\Installs\\WebDrivers\\chromedriver.exe";

    public static void main(String[] Args) throws InterruptedException {
        // Create firefox driver's instance
        System.setProperty("webdriver.chrome.driver", DRIVER_PATH);
        WebDriver driver = new ChromeDriver();

        // Set implicit wait of 10 seconds
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        // Launch sampleSiteForSelenium web page
        driver.get("http://www.artoftesting.com/sampleSiteForSelenium.html");

        // Use the id of the div to locate it and then fetch text using getText() method
        String sampleText = driver.findElement(By.id("idOfDiv")).getText();
        LOGGER.info(sampleText);

        // Using Selenium linkText locator to find the link and then using click() command to click on it
        driver.findElement(By.linkText("This is a link")).click();

        // Finding a TextBox using id locator and then using send keys to write in it
        WebElement TextBox = driver.findElement(By.id("fname"));
        TextBox.sendKeys("Some keys");

        // Clear the text written in the TextBox
        TextBox.clear();

        // Clicking on a button using click() command
        driver.findElement(By.id("idOfButton")).click();

        // Find a radio button by name and check it using click() function
        driver.findElement(By.name("gender")).click();

        // Find a CheckBox by cssSelector and check it using click() function
        driver.findElement(By.cssSelector("input.Automation")).click();

        // Using Select class, there are several ways of selecting from a dropdown
        Select dropdown = new Select(driver.findElement(By.id("testingDropdown")));
        dropdown.selectByVisibleText("Automation Testing");
        dropdown.selectByIndex(0);
        dropdown.selectByValue("Automation");

        Thread.sleep(10000);

        // Close the browser
        driver.quit();
    }
}
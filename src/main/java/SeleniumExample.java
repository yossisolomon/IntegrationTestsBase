import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


public class SeleniumExample {
    private static final String DRIVER_PATH = "D:\\Installs\\WebDrivers\\chromedriver.exe";
    private static final Logger LOGGER = Logger.getLogger(SeleniumExample.class.getName());

    public static void main(String[] Args) throws InterruptedException {
        WebDriver driver = setupDriver();

        // Launch sample web page
        driver.get("http://www.artoftesting.com/sampleSiteForSelenium.html");

        basicCommandsExample(driver);
        advancedCommandsExample(driver);
        handlingDropdownExample(driver);
        dragAndDropExample(driver);

        Thread.sleep(5000);

        driver.quit(); // Close the browser
    }

    private static WebDriver setupDriver() {
        System.setProperty("webdriver.chrome.driver", DRIVER_PATH); // Set the path to the chromedriver.exe

        ChromeOptions options = new ChromeOptions(); // here we can set specific options
        WebDriver driver = new ChromeDriver(options); // Create chrome driver instance

        // This is required for managing waits in selenium webdriver
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS); // Set implicit wait of 10 seconds
        return driver;
    }

    private static void basicCommandsExample(WebDriver driver) {
        // Use the id of the div to locate it and then fetch text using getText() method
        String sampleText = driver.findElement(By.id("idOfDiv")).getText();
        LOGGER.info(sampleText);

        // Using linkText locator to find the link and click it using click() command
        driver.findElement(By.linkText("This is a link")).click();

        // Find a radio button by name and check it using click() function
        driver.findElement(By.name("gender")).click();

        // Find a CheckBox by cssSelector and check it using click() function
        driver.findElement(By.cssSelector("input.Automation")).click();

        // Clicking on the submit button using click() command
        driver.findElement(By.id("idOfButton")).click();

        // Finding a TextBox using id locator
        WebElement textBox = driver.findElement(By.id("fname"));
        textBox.sendKeys("Some Text"); // using send keys to write in it
        textBox.clear(); // clear the text written in the TextBox
    }

    private static void handlingDropdownExample(WebDriver driver) {
        // Using Select class, there are several ways of selecting from a dropdown
        Select dropdown = new Select(driver.findElement(By.id("testingDropdown")));
        dropdown.selectByIndex(0); // selection based on index
        dropdown.selectByValue("Automation"); // selection based on the "value" attribute
        dropdown.selectByVisibleText("Automation Testing"); // selection based on the text over the option

        // Other useful methods in the Select class are:
        dropdown.getOptions(); // returns all of the options as a List of WebElement objects
        dropdown.isMultiple(); // returns a boolean value that represents whether the dropdown is multi selection
        dropdown.getAllSelectedOptions(); // returns all of the selected option (or options if multi selection)
    }

    private static void advancedCommandsExample(WebDriver driver) {
        Actions action = new Actions(driver);

        action.contextClick(driver.findElement(By.id("fname"))).perform(); // Right click in the TextBox
        action.doubleClick(driver.findElement(By.id("fname"))).perform(); // Double click in the TextBox
        action.moveToElement(driver.findElement(By.id("idOfButton"))).perform(); // Mouseover on the submit button
    }

    private static void dragAndDropExample(WebDriver driver) {
        // WebElement on which drag and drop operation needs to be performed
        WebElement fromWebElement = driver.findElement(By.id("sourceImage"));

        // WebElement to which the above object is dropped
        WebElement toWebElement = driver.findElement(By.id("targetDiv"));

        // Creating object of Actions class to build composite actions
        Actions builder = new Actions(driver);

        // Building a drag and drop action
        Action dragAndDrop = builder.clickAndHold(fromWebElement)
                .moveToElement(toWebElement)
                .release(toWebElement)
                .build();

        // Performing the drag and drop action
        dragAndDrop.perform();
    }
}
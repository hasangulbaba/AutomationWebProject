package methods;

import driver.DriverCreater;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import static java.time.Duration.ofMillis;

public class Methods {

    private static Logger logger = LoggerFactory.getLogger(Methods.class);
    private WebDriver driver = null;
    private MethodsUtil methodsUtil;
    private FluentWait wait;
    private long pollingMillis;
    public GaugeDataStores gaugeDataStores;

    public Methods() {
        this.driver = DriverCreater.WebDriver;
        wait = setFluentWait(30);
        pollingMillis = 300;
        gaugeDataStores = new GaugeDataStores();
        methodsUtil = new MethodsUtil();
    }

    public FluentWait setFluentWait(long timeout) {

        FluentWait fluentWait = new FluentWait<>(driver);
        fluentWait.withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(ofMillis(pollingMillis))
                .ignoring(NoSuchElementException.class);
        return fluentWait;
    }

    public WebElement findElement(By by) {
        logger.info("Element " + by.toString() + " by değerine sahip");
        return (WebElement) wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public List<WebElement> findElements(By by) {
        logger.info("Element " + by.toString() + " by değerine sahip");
        return (List<WebElement>) wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
    }

    public By getById(String key){
        return By.id(key);
    }

    public By getByCssSelector(String key){
        return By.cssSelector(key);
    }

    public By getByXpath(String key){
        return By.xpath(key);
    }

    public String getText(By by) {
        return findElement(by).getText();
    }

    public void clickElement(By by) {

        findElement(by).click();
        logger.info("Elemente tıklandı.");
    }

    public void holdAndRelease(WebElement element, int xOffset, int yOffset) {
        Actions actions = new Actions(driver);
        actions.clickAndHold(element)
                .moveByOffset(xOffset, yOffset)
                .release()
                .build()
                .perform();
    }

    public void waitByMilliSeconds(long milliSeconds) {

        methodsUtil.waitByMilliSeconds(milliSeconds);
    }

    public void waitBySeconds(long seconds) {

        logger.info(seconds + " saniye bekleniyor...");
        methodsUtil.waitBySeconds(seconds);
    }

    public void sendKeysToElement(By by,String text){
        findElement(by).clear();
        findElement(by).sendKeys(text);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        findElement(by).sendKeys(Keys.ENTER);
        logger.info("Elemente " + text + " texti yazıldı.");
    }


    public boolean getElementTextVisibleCheckTextControl(By by, String text) {
        waitByMilliSeconds(250);

        List<WebElement> elements = findElements(by);
        return elements.stream()
                .anyMatch(data -> data.getText().contains(text));
    }

    public void swipeDownUntilFoundText(By by, String text) {
        while (!getElementTextVisibleCheckTextControl(by, text)) {
            doSwipe();
        }
    }

    public WebElement findElementFromList(By by, String text) {
        swipeDownUntilFoundText(by, text);

        waitByMilliSeconds(250);
        List<WebElement> allList = findElements(by);
        return allList.parallelStream()
                .filter(webElement -> this.isCheckElementAttributeOrText(webElement, text))
                .findFirst()
                .orElseThrow(java.util.NoSuchElementException::new);
    }

    private boolean isCheckElementAttributeOrText(WebElement element, String value) {

        boolean exit;

        String attribute = gaugeDataStores.fetchStringFromScenarioDataStore("elementAttribute");
        String checkType = gaugeDataStores.fetchStringFromScenarioDataStore("checkType");

        switch (checkType) {

            case "text":
                exit = element.getCssValue(value).contains(value);
                break;

            case "attribute":
                exit = element.getAttribute(attribute).contains(value);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + checkType);
        }
        return exit;
    }

    public void doSwipe() {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        int width = 600;
        int height = 400;
        int durationInSeconds = 3;

        long endTime = System.currentTimeMillis() + (durationInSeconds * 1000);
        while (System.currentTimeMillis() < endTime) {
            js.executeScript("window.scrollBy(0, arguments[0]);", height);
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void putValueInGaugeDataStore(String key, Object object) {

        gaugeDataStores.storeStringToScenarioDataStore(key, object);
    }

    public void putValueInGaugeDataStoreElementGetText(String key, String parameter) {

        putValueInGaugeDataStore(parameter, getText(getById(key)));
        logger.info("gauge store değerine paramtere: " + parameter + " değeri " + getText(getById(key)) + " eklendi");
    }

    public String getValueInDataStore(String parameter) {

        return gaugeDataStores.fetchStringFromScenarioDataStore(parameter);
    }

    public void clearElement(By by){
        findElement(by).clear();
    }

}

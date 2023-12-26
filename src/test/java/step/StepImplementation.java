package step;

import com.thoughtworks.gauge.Step;
import driver.DriverCreater;
import methods.Methods;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.Log4jLoggerAdapter;
import java.time.LocalTime;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class StepImplementation {
    private static Log4jLoggerAdapter logger = (Log4jLoggerAdapter) LoggerFactory.getLogger(StepImplementation.class);
    WebDriver driver;
    private Methods methods;

    public StepImplementation() {
        this.methods = new Methods();
        this.driver = DriverCreater.WebDriver;
    }

    @Step({"<seconds> saniye bekle",
            "wait <seconds> seconds"})
    public void waitBySeconds(long seconds) {
        methods.waitBySeconds(seconds);
    }

    @Step({"<key> elementine css ile tıkla",
            "click on the <key> element"})
    public void clickElementWithCssSelector(String key) {
        methods.clickElement(methods.getByCssSelector(key));
    }

    @Step("Hold and Release İşlemi <elementId> elementi, xOffset=<xOffset>, yOffset=<yOffset> ofset ile hareket ettir")
    public void holdAndReleaseElement(String elementId, int xOffset, int yOffset) {
        WebElement element = driver.findElement(methods.getByCssSelector(elementId));
        methods.holdAndRelease(element, xOffset, yOffset);
    }

    @Step({"<key> elementinin text değeri <expectedText> değerine eşit mi",
            "Is the text value of <key> element is equal to <expectedText>"})
    public void getElementText(String key, String expectedText) {
        methods.waitByMilliSeconds(250);
        String actualText = methods.getText(methods.getByCssSelector(key)).trim()
                .replace("\r", "").replace("\n", "");
        logger.info("Beklenen text: " + expectedText);
        logger.info("Alınan text: " + actualText);
        assertEquals("text değerleri eşit değil",expectedText.replace(" ", "")
                , actualText.replace(" ", ""));
        logger.info("Text değerleri eşit");
    }

    @Step({"<key> elementine <text> değerini yaz",
            "<key> element send <text> text"})
    public void sendKeysElementWithId(String key, String text) {
        methods.sendKeysToElement(methods.getById(key), text);
    }

    @Step({"<key> elementine <text> değerini css ile yaz",
            "<key> element send <text> text"})
    public void sendKeysElementWithCss(String key, String text) {
        methods.sendKeysToElement(methods.getByCssSelector(key), text);
    }

    @Step({"<key> css'li elementinin liste içerisinde text değeri <expectedText> değerine tıkla",
            "Is the text value of <key> search in the element list is click to <expectedText>"})
    public void getElementListTextCssClick(String key, String expectedText) {
        methods.waitByMilliSeconds(250);
        methods.gaugeDataStores.storeStringToScenarioDataStore("checkType", "text");
        WebElement element = methods.findElementFromList(methods.getByCssSelector(key), expectedText);
        String actualText = element.getText()
                .replace("\r", "").replace("\n", "");
        logger.info("Beklenen text: " + expectedText);
        logger.info("Alınan text: " + actualText);
        assertEquals(expectedText.replace(" ", "")
                , actualText.replace(" ", ""), "Text değerleri eşit değil");
        element.click();
        logger.info("Text değerlerine tıklandı.");
    }

    @Step({"<key> elementinin değerini temizle",
            "clear the value of <key> element"})
    public void clearElement(String key) {
        methods.clearElement(methods.getByCssSelector(key));
    }

    @Step("<key> Parametre olarak alınan sayı kadar click yap: <clickCount>")
    public void clickMultipleTimes(String key,int clickCount) throws InterruptedException {
        WebElement clickableElement = methods.findElement(methods.getByCssSelector(key));

        for (int i = 0; i < clickCount; i++) {
            clickableElement.click();
            Thread.sleep(1000);
        }
    }
    @Step("<key> kalkış saatinin 10:00'dan geç olduğu kontrol et")
    public void checkDepartureClock(String key){
        String departureTime = null;
        LocalTime localTime;
        LocalTime localTime2;
        String TenClock = "10:00";

        departureTime = methods.findElement(methods.getByCssSelector(key)).getText();

        localTime = LocalTime.parse(departureTime);
        localTime2 = LocalTime.parse(TenClock);

        int comparisonResult = localTime.compareTo(localTime2);

        if (comparisonResult > 0){
            logger.info("ilk kalkan uçağın saati 10:00'dan geç");
        }else {
            logger.info("ilk kalkan uçağın saati 10:00'dan erken");
        }

    }

    @Step("<key> element listesindeki element değerlerinin artan şekilde sıralandığını karşılaştır")
    public void flyPriceSortAsc(String key){

        List<WebElement> allList = methods.findElements(methods.getByCssSelector(key));

        for (int i = 0; i < allList.size() - 1; i++) {

            WebElement currentElement = allList.get(i);
            WebElement nextElement = allList.get(i + 1);

            String currentText = currentElement.getText();
            String nextText = nextElement.getText();

            double currentPrice = Double.parseDouble(currentText);
            double nextPrice = Double.parseDouble(nextText);

            if (currentPrice <= nextPrice ) {
                logger.info("Thy havayolu uçak fiyatları artan şekilde sıralanmamıştır !");
            } else {
                logger.info("Thy havayolu uçak fiyatları artan şekilde sıralanmamıştır !");
            }
        }

    }

}

package driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class DriverCreater {

    public static WebDriver WebDriver = null;
    public static void beforeTest() {

        DesiredCapabilities capabilities = new DesiredCapabilities().opera();

        WebDriverManager.operadriver().setup();
        WebDriver = new OperaDriver(capabilities);
        WebDriver.manage().window().maximize();
        WebDriver.get("https://www.enuygun.com/");

    }

    public void afterPlan() {

        System.out.println("** Test tamamlandı **");
    }

}

package happyfox.helpdesk_automation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.github.bonigarcia.wdm.WebDriverManager;

public class SystemService {

	public static WebDriver driver;
	public static Properties prop;
	public static Actions actions;
	public static Action action;
	public static Random obj;
	public static ArrayList<String> tabs;

	public static WebDriver getDriver() {
		prop = new Properties();
		try {
			FileInputStream file = new FileInputStream("src\\test\\resources\\config.properties");
			prop.load(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (prop.getProperty("browser").equalsIgnoreCase("Chrome")) {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();

		} else if (prop.getProperty("browser").equalsIgnoreCase("Firefox")) {

			WebDriverManager.firefoxdriver().setup();

			FirefoxOptions fireopt = new FirefoxOptions();
			FirefoxProfile fireprof = new FirefoxProfile();

			DesiredCapabilities cap = new DesiredCapabilities();
			cap.setCapability("marionette", false);

			fireprof.setPreference("dom.webdriver.enabled", false);
			fireprof.setPreference("useAutomationExtension", false);

			fireopt.addArguments("--disable-blink-features=AutomationControlled");

			fireopt.merge(cap);
			driver = new FirefoxDriver(fireopt);

		} else if (prop.getProperty("browser").equalsIgnoreCase("Edge")) {

			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();

		}
		return driver;
	}

	public static void mouseHover(WebElement element) {
		actions = new Actions(driver);
		action = actions.moveToElement(element, 8, 0).build();
		action.perform();
	}

	public static void jsEMouseHover(WebElement element) {
		String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover', true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";

		((JavascriptExecutor) driver).executeScript(mouseOverScript, element);
	}

	public static void reload() {
		((JavascriptExecutor) driver).executeScript("history.go[0]");

	}

	public static void scrollIntoElement(WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
	}

	public static void jseClick(WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
	}

	public static void innerHTML(WebElement element, String value) {
		((JavascriptExecutor) driver).executeScript("var ele=arguments[0]; ele.innerHTML = '" + value + "';", element);
	}

	public static void clearandSendkeys(WebElement element, String value) {
		element.clear();
		element.sendKeys(value);
	}

	public static void sendValuesByJSE(WebElement element, String value) {
		((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('value','" + value + "');", element);
	}

	public static String colorCode() {
		obj = new Random();
		int rand_num = obj.nextInt(0xffffff + 1);
		return String.format("#%06x", rand_num);
	}

	public static Map<String, String> getWidowsID() {
		Map<String, String> windows = new HashMap<String, String>();
		windows.put("parent", driver.getWindowHandle());
		Set<String> w = driver.getWindowHandles();
		Iterator<String> it = w.iterator();
		while (it.hasNext()) {
			windows.put("child", it.next());
		}
		return windows;
	}

	public static void takeSS(String filename) {
		TakesScreenshot tss = ((TakesScreenshot) driver);
		File srcfile = tss.getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(srcfile, new File("./screenshots/" + filename + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void FailedSS(String methodName) {
		TakesScreenshot tss = ((TakesScreenshot) driver);
		File srcfile = tss.getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(srcfile, new File("./screenshots/" + methodName + "_Failed" + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void elementSS(WebElement element, String fileName) {
		File srcfile = element.getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(srcfile, new File("./screenshots/" + fileName + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void elementHighlighter(WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('style', 'border: 2px solid green;');",
				element);
	}

	public static String jseGetText(WebElement element) {
		return (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].innerText;", element);

	}

}

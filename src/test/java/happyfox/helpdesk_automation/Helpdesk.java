package happyfox.helpdesk_automation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import happyfox.helpdesk_automation.locators.HelpdeskLocators;

public class Helpdesk {

	public Properties prop;
	private WebDriver driver;

	private HelpdeskLocators helpdeskLoc;
	private Waits waits;

	public Helpdesk(WebDriver driver) {
		this.driver = driver;
		this.helpdeskLoc = new HelpdeskLocators();
		this.waits = new Waits(driver);
		this.prop = SystemService.prop;
		PageFactory.initElements(driver, helpdeskLoc);
	}

	public void refresh() {
		this.driver.get(this.driver.getCurrentUrl());
		this.waits.waitForLoading(60);
	}

	public void openHelpdesk() {
		SystemService.driver.get(this.prop.getProperty("base_url") + this.prop.getProperty("agent_portal"));
		this.waits.waitForLoading(30);
	}

	public By ticketPageTile() {
		return By.className("hf-ticket-list-header-title");
	}

	public void agentLogin() {
		this.helpdeskLoc.agentUsrname.sendKeys(this.prop.getProperty("user_name"));
		this.helpdeskLoc.agentPass.sendKeys(this.prop.getProperty("password"));
		this.helpdeskLoc.rememberMe.click();
		this.helpdeskLoc.agentLoginbtn.click();
		this.waits.waitForLoading(50);
		this.waits.waitForElementVisiblity(this.helpdeskLoc.ticketsTable, 35);
	}

	public void gotoHomePage() {

		if (this.driver.getCurrentUrl()
				.equalsIgnoreCase("https://interview2.supporthive.com/staff/login/?return_to=%2Fstaff%2F")) {
			this.openHelpdesk();
			this.agentLogin();
			this.waits.waitForLoading(40);
		}

		else {

			this.openHelpdesk();
			this.waits.waitForLoading(40);
			this.waits.waitForElementVisiblity(this.helpdeskLoc.ticketsTable, 35);
		}
	}

	public HashMap<String, String> drvierWindowHandles(WebDriver driver) {
		HashMap<String, String> windows = new HashMap<String, String>();
		windows.put("parent", driver.getWindowHandle());
		Set<String> wh = driver.getWindowHandles();
		Iterator<String> I1 = wh.iterator();

		while (I1.hasNext()) {
			windows.put("child", I1.next());
		}
		return windows;
	}

	public void retry(String value) {
		SystemService.mouseHover(this.helpdeskLoc.menu);
		this.waits.waitForElementToBeClickable(this.helpdeskLoc.menu(value), 6);
		this.helpdeskLoc.menu(value).click();
	}

	public void selectMenu(String value) {
		SystemService.mouseHover(this.helpdeskLoc.menu);
		this.waits.waitForElementToBeClickable(this.helpdeskLoc.menu(value), 6);
		this.helpdeskLoc.menu(value).click();
		SystemService.jseClick(this.helpdeskLoc.menu);
		this.waits.waitForLoading(50);
	}

	public void selectTicketMenu() {
		SystemService.mouseHover(this.helpdeskLoc.menu);
		this.waits.waitForElementToBeClickable(this.helpdeskLoc.ticketMenu, 10);
		this.helpdeskLoc.ticketMenu.click();
		this.waits.waitForLoading(25);
	}

	public void agentLogout() {
		this.waits.waitForElementVisiblity(this.helpdeskLoc.profileBtn, 30);
		this.helpdeskLoc.profileBtn.click();
		this.helpdeskLoc.agentLogout.click();
	}

	public String logoutConfirmation() {
		this.waits.waitForElementVisiblity(this.helpdeskLoc.logoutConfirmMsg, 25);
		return this.helpdeskLoc.logoutConfirmMsg.getText();
	}

	public String getBannerMessage() {
		this.waits.waitForElementVisiblity(this.helpdeskLoc.banner, 20);
		return this.helpdeskLoc.banner.getText().toString();
	}

	public void waitForTableLoad() {
		this.waits.waitForElementVisiblity(this.helpdeskLoc.ticketsTable, 45);
	}

}

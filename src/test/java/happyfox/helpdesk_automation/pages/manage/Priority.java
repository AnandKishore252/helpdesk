package happyfox.helpdesk_automation.pages.manage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import happyfox.helpdesk_automation.Helpdesk;
import happyfox.helpdesk_automation.SystemService;
import happyfox.helpdesk_automation.Waits;
import happyfox.helpdesk_automation.locators.manage.PriorityLocators;

public class Priority {
	private WebDriver driver;

	private PriorityLocators priorityLoc;
	private Waits waits;
	private Helpdesk helpdesk;

	public Priority(WebDriver driver) {
		this.driver = driver;
		this.priorityLoc = new PriorityLocators();
		this.waits = new Waits(driver);
		this.helpdesk = new Helpdesk(driver);
		PageFactory.initElements(driver, priorityLoc);
	}

	public void openPriorityTableView() {
		String priorityURL = SystemService.prop.getProperty("base_url") + "/staff/manage/priorities";
		if (!this.driver.getCurrentUrl().equalsIgnoreCase(priorityURL)) {
			this.driver.get(priorityURL);
			this.waits.waitForElementInvisiblity(this.priorityLoc.loader, 40);
		}
	}

	public void createPriority(HashMap<String, String> value) {
		// openPriorityTableView();
		this.waits.waitForElementToBeClickable(this.priorityLoc.createPriority, 15);
		this.priorityLoc.createPriority.click();
		this.waits.waitForElementVisiblity(this.priorityLoc.priorityName, 10);
		SystemService.clearandSendkeys(this.priorityLoc.priorityName, value.get("name"));
		SystemService.clearandSendkeys(this.priorityLoc.priorityDesc, value.get("description"));
		this.priorityLoc.addPriorityBtn.click();
		this.waits.waitForElementVisiblity(this.priorityLoc.getPriority(value.get("name")), 40);
	}

	public boolean isPriorityPresent(String name) {
		this.waits.waitForElementVisiblity(this.priorityLoc.getPriority(name), 45);
		return this.priorityLoc.getPriority(name).isDisplayed();
	}

	public WebElement getPriority(String name) {
		return this.priorityLoc.getPriority(name);
	}

	public void makeDefault(String name) {
		this.waits.waitForElementVisiblity(this.priorityLoc.getPriority(name), 13);
		SystemService.mouseHover(this.priorityLoc.getPriority(name));
		this.waits.waitForLoading(30);
		this.priorityLoc.makeDefault(name).click();
		this.waits.waitForElementInvisiblity(this.priorityLoc.loader, 45);
		this.waits.waitForElementVisiblity(this.priorityLoc.defaultPriority, 150);
	}

	public String getDefaultPriority() {
		this.waits.waitForElementVisiblity(this.priorityLoc.defaultPriority, 100);
		return this.priorityLoc.defaultPriority.getText();
	}

	public List<String> getAllPriority() {
		openPriorityTableView();
		this.waits.waitForElementVisiblity(this.priorityLoc.defaultPriority, 20);
		List<WebElement> rows = this.priorityLoc.getAllPriorityRows();
		List<String> priorities = new ArrayList<String>();
		for (WebElement element : rows) {
			priorities.add(element.findElement(By.xpath("//td[2]")).getText().toString());
		}
		return priorities;
	}
	
	public void isPriorityDisplayed(String name) {
		int count = 3;
		while (count<=3) {
			if (this.isPriorityPresent(name)) {
				break;
			} else {
				this.helpdesk.refresh();
				count ++;
			}
		}

	}

	/*
	 * public void deleteCurrentPriority(String changeDefaultTo) {
	 * this.waits.waitForElementVisiblity(this.priorityLoc.deletePriority, 7);
	 * this.priorityLoc.deletePriority.click();
	 * this.waits.waitForElementVisiblity(this.priorityLoc.confirmDelete, 13); if
	 * (this.priorityLoc.dropdown.isDisplayed()) {
	 * this.priorityLoc.dropdown.click(); List<WebElement> options =
	 * this.priorityLoc.priorityChangeDropDown(); for (WebElement option : options)
	 * { if (option.getText().equalsIgnoreCase(changeDefaultTo)) { option.click();
	 * break; } } } this.priorityLoc.confirmDelete.click();
	 * 
	 * }
	 * 
	 * public String deleteCustomPriority(String priorityName, String
	 * changeDefaultTo) { openPriorityTableView(); this.waits.waitForLoading(10);
	 * SystemService.scrollIntoElement(this.priorityLoc.getPriority(priorityName));
	 * SystemService.mouseHover(this.priorityLoc.getPriority(priorityName));
	 * this.priorityLoc.getPriority(priorityName).click();
	 * deleteCurrentPriority(changeDefaultTo);
	 * this.waits.waitForElementInvisiblity(this.priorityLoc.getPriority(
	 * priorityName), 13); String bannerMessage = this.helpdesk.getBannerMessage();
	 * return bannerMessage; }
	 */

	public void deleteDefaultPriority(String changeDefaultPriorityTo) {
		this.waits.waitForElementVisiblity(this.priorityLoc.deletePriority, 10);
		this.priorityLoc.deletePriority.click();
		this.waits.waitForElementVisiblity(this.priorityLoc.confirmDelete, 13);
		this.priorityLoc.dropdown.click();
		List<WebElement> options = this.priorityLoc.priorityChangeDropDown();
		for (WebElement option : options) {
			if (option.getText().equalsIgnoreCase(changeDefaultPriorityTo)) {
				option.click();
				this.priorityLoc.confirmDelete.click();
			}
			break;
		}

	}

	public String deleteCustomPriority(String priorityName, String changeDefaultPriorityTo) {
		this.waits.waitForLoading(20);
		SystemService.scrollIntoElement(this.priorityLoc.getPriority(priorityName));

		if (this.getDefaultPriority().equalsIgnoreCase(priorityName)) {
			SystemService.mouseHover(this.priorityLoc.getPriority(priorityName));
			this.priorityLoc.getPriority(priorityName).click();
			this.deleteDefaultPriority(changeDefaultPriorityTo);
		} else {
			SystemService.mouseHover(this.priorityLoc.getPriority(priorityName));
			this.priorityLoc.getPriority(priorityName).click();

			this.waits.waitForElementVisiblity(this.priorityLoc.deletePriority, 10);
			this.priorityLoc.deletePriority.click();
			this.waits.waitForElementVisiblity(this.priorityLoc.confirmDelete, 20);
			this.priorityLoc.confirmDelete.click();
		}

		String bannerMessage = this.helpdesk.getBannerMessage();
		return bannerMessage;
	}

}

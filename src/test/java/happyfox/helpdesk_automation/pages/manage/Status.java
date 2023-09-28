package happyfox.helpdesk_automation.pages.manage;

import java.util.HashMap;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import happyfox.helpdesk_automation.Helpdesk;
import happyfox.helpdesk_automation.SystemService;
import happyfox.helpdesk_automation.Waits;
import happyfox.helpdesk_automation.locators.manage.StatusLocators;

public class Status {

	private WebDriver driver;
	private StatusLocators statusLoc;
	private Helpdesk helpdesk;
	private Waits waits;

	public Status(WebDriver driver) {
		this.driver = driver;
		this.statusLoc = new StatusLocators();
		this.helpdesk = new Helpdesk(driver);
		this.waits = new Waits(driver);
		PageFactory.initElements(driver, statusLoc);
	}

	public void openStatusTableView() {
		String statusURL = SystemService.prop.getProperty("base_url") + "/staff/manage/statuses";
		if (!this.driver.getCurrentUrl().equalsIgnoreCase(statusURL)) {
			this.driver.get(statusURL);
			this.waits.waitForElementInvisiblity(this.statusLoc.loader, 40);
		}
	}

	public void createStatus(HashMap<String, String> value) {
		this.waits.waitForElementToBeClickable(this.statusLoc.create, 15);
		this.statusLoc.create.click();

		this.waits.waitForElementVisiblity(this.statusLoc.statusName, 10);
		SystemService.clearandSendkeys(this.statusLoc.statusName, value.get("name"));

		this.statusLoc.statusColor.click();
		this.setColorValue(value.get("color"));

		this.statusLoc.statusBehaviour.click();
		this.selectStatusBehaviour(value.get("behaviour"));

		SystemService.clearandSendkeys(this.statusLoc.desc, value.get("description"));

		this.statusLoc.addBtn.click();
		this.waits.waitForElementVisiblity(this.statusLoc.getStatus(value.get("name")), 20);
	}

	public void setColorValue(String color) {
		String activepath = "//div[@class='sp-replacer sp-light sp-active']";
		this.statusLoc.colorValue.click();

		this.waits.waitForElementToBeClickable(this.statusLoc.sendColorValue, 5);
		this.statusLoc.sendColorValue.click();
		SystemService.clearandSendkeys(this.statusLoc.sendColorValue, color);

		this.statusLoc.statusColor = this.driver.findElement(By.xpath(activepath));
		this.waits.waitForElementToBeClickable(this.statusLoc.statusColor, 5);
		this.statusLoc.statusColor.click();
	}

	public void selectStatusBehaviour(String behaviour) {
		List<WebElement> options = this.statusLoc.behaviourDropdown();
		for (WebElement option : options) {
			if (option.getText().equalsIgnoreCase(behaviour)) {
				option.click();
				break;
			}
		}
	}

	public boolean isStatusPresent(String name) {
		this.waits.waitForElementVisiblity(this.statusLoc.getStatus(name), 20);
		return this.statusLoc.getStatus(name).isDisplayed();
	}

	public void makeDefault(String name) {
		this.waits.waitForElementVisiblity(this.statusLoc.getStatus(name), 13);
		SystemService.mouseHover(this.statusLoc.getStatus(name));
		this.waits.waitForLoading(10);
		this.statusLoc.statusMakeDefault.click();
		this.waits.waitForElementInvisiblity(this.statusLoc.loader, 25);
		this.waits.waitForElementVisiblity(this.statusLoc.defaultStatusName, 150);
	}

	public String getDefaultStatus() {
		this.waits.waitForElementVisiblity(this.statusLoc.defaultStatusName, 150);
		return this.statusLoc.defaultStatusName.getText();
	}

	public WebElement getStatus(String name) {
		return this.statusLoc.getStatus(name);
	}

	public void isStatusDisplayed(String name) {
		int count = 3;
		while (count <= 3) {
			if (this.isStatusPresent(name)) {
				break;
			} else {
				this.helpdesk.refresh();
				count ++;
			}
		}

	}

	/*
	 * public void deleteCurrentStatus(String defaultostatus) {
	 * this.waits.waitForElementVisiblity(this.statusLoc.delete, 7);
	 * this.statusLoc.delete.click();
	 * this.waits.waitForElementVisiblity(this.statusLoc.confirmDelete, 13); if
	 * (this.statusLoc.dropdown.isDisplayed()) { this.statusLoc.dropdown.click();
	 * List<WebElement> options = this.statusLoc.statusChangeDropDown(); for
	 * (WebElement option : options) { if
	 * (option.getText().equalsIgnoreCase(defaultostatus)) { option.click(); break;
	 * } } } this.statusLoc.confirmDelete.click(); }
	 * 
	 * public String deleteCustomStatus(String statusName, String defaultostatus) {
	 * openStatusTableView(); this.waits.waitForLoading(10);
	 * SystemService.scrollIntoElement(this.statusLoc.getStatus(statusName));
	 * SystemService.mouseHover(this.statusLoc.getStatus(statusName));
	 * this.statusLoc.getStatus(statusName).click();
	 * deleteCurrentStatus(defaultostatus); String bannerMessage =
	 * this.helpdesk.getBannerMessage();
	 * this.waits.waitForElementInvisiblity(this.statusLoc.getStatus(statusName),
	 * 13); return bannerMessage; }
	 */

	public void deleteDefaultStatus(String changeDefaultStatusTo) {
		this.waits.waitForElementVisiblity(this.statusLoc.delete, 15);
		this.statusLoc.delete.click();
		this.statusLoc.dropdown.click();
		List<WebElement> options = this.statusLoc.statusChangeDropDown();
		for (WebElement option : options) {
			if (option.getText().equalsIgnoreCase(changeDefaultStatusTo)) {
				option.click();
				this.statusLoc.confirmDelete.click();

			}
			break;
		}
	}

	public String deleteCustomStatus(String statusName, String changeDefaultStatusTo) {
		SystemService.scrollIntoElement(this.statusLoc.getStatus(statusName));

		if (this.getDefaultStatus().equalsIgnoreCase(statusName)) {
			SystemService.mouseHover(this.statusLoc.getStatus(statusName));
			this.statusLoc.getStatus(statusName).click();
			this.deleteDefaultStatus(changeDefaultStatusTo);
		} else {
			SystemService.mouseHover(this.statusLoc.getStatus(statusName));
			this.statusLoc.getStatus(statusName).click();
			this.waits.waitForElementVisiblity(this.statusLoc.delete, 15);
			this.statusLoc.delete.click();
			this.waits.waitForElementVisiblity(this.statusLoc.confirmDelete, 20);
			this.statusLoc.confirmDelete.click();
		}

		String bannerMessage = this.helpdesk.getBannerMessage();
		return bannerMessage;
	}

}

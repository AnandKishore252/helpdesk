package happyfox.helpdesk_automation.locators.manage;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import happyfox.helpdesk_automation.SystemService;

public class PriorityLocators {

	@FindBy(xpath = "//div[@data-test-id='module-switcher_manage-title']/following::ul//li[@data-test-id='module-switcher_manage-priorities']")
	public WebElement priorityViaMenu;

	@FindBy(xpath = "//a[@title='Priorities']")
	public WebElement priorityViaSidebar;

	@FindBy(xpath = "//div[@data-test-id='priorities-table']")
	public WebElement priorityTable;

	@FindBy(xpath = "//button[@data-test-id='new-priority']")
	public WebElement createPriority;

	@FindBy(xpath = "//input[@aria-label='Priority Name']")
	public WebElement priorityName;

	@FindBy(xpath = "//textarea[@aria-label='Description']")
	public WebElement priorityDesc;

	@FindBy(xpath = "//textarea[@data-test-id = 'form-field-helpText']")
	public WebElement priorityHelpText;

	@FindBy(xpath = "//button[@data-test-id = 'add-priority']")
	public WebElement addPriorityBtn;

	@FindBy(xpath = "//a[@data-test-id = 'cancel-add-priority']")
	public WebElement priorityCancel;

	@FindBy(xpath = "//a[@data-test-id = 'reset-add-priority']")
	public WebElement priorityReset;

	@FindBy(xpath = "//div[@class = 'hf-form-header_close']")
	public WebElement closeSlidePanel;

	@FindBy(linkText = "Make Default")
	public WebElement priorityMakeDefault;

	@FindBy(xpath = "//div[@data-test-id='default-priority']//ancestor::tr/td[2]")
	public WebElement defaultPriority;

	@FindBy(xpath = "//header[@data-test-id='view-priority-header']/a[contains(text(),'Edit')]")
	public WebElement editPriority;

	@FindBy(xpath = "//header[@data-test-id='view-priority-header']/a[contains(text(),'Delete')]")
	public WebElement deletePriority;

	@FindBy(xpath = "//div[@data-test-id='form-field-alternateEntity']")
	public WebElement dropdown;

	@FindBy(xpath = "//li[@data-option-index='0']")
	public WebElement lowOption;

	@FindBy(xpath = "//button[contains(text(),'Delete')]")
	public WebElement confirmDelete;

	@FindBy(xpath = "//div[@class='hf-toast-message']")
	public WebElement bannerMsg;

	@FindBy(xpath = "//div[@data-test-id='priorities-table']//table//tbody//td/span[@title='']")
	public WebElement priority;

	@FindBy(xpath = "//*[@data-test-id='priorities-table']//tbody//tr[last()]")
	public WebElement lastRow;

	@FindBy(xpath = "//tr//td[5]//*[local-name()='svg']")
	public WebElement defaulted;

	@FindBy(xpath = "//div[@class='hf-ticket-action_loader']")
	public WebElement loader;

	public WebElement sideMenu(String name) {
		return SystemService.driver.findElement(By.linkText(name));
	}

	public List<WebElement> priorityChangeDropDown() {
		return SystemService.driver
				.findElements(By.xpath("//div[@data-test-id='form-field-alternateEntity']//following::ul//li"));
	}

	public WebElement getPriority(String name) {
		return SystemService.driver.findElement(By.xpath("//span[@title='" + name + "']"));
	}

	public List<WebElement> getAllPriorityRows() {
		return SystemService.driver.findElements(By.xpath("//div[@data-test-id='priorities-table']//tbody//tr"));
	}

	public WebElement makeDefault(String priorityName) {
		return SystemService.driver.findElement(
				By.xpath("//span[@title='" + priorityName + "']//ancestor::tr//td[5]//div[@class='hf-make-default']"));
	}

	public String defaultStatusName() {
		return SystemService.driver.findElement(By.xpath("//div[@data-test-id='default-status']//ancestor::tr/td[2]"))
				.getText();
	}

	public WebElement priorityDefaulted(String priorityName) {
		return SystemService.driver.findElement(
				By.xpath("//span[@title='" + priorityName + "']//ancestor::tr//td[5]//*[local-name()='svg']"));
	}

}

package happyfox.helpdesk_automation.pages.support.newTicket;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import happyfox.helpdesk_automation.Helpdesk;
import happyfox.helpdesk_automation.SystemService;
import happyfox.helpdesk_automation.Waits;
import happyfox.helpdesk_automation.support.newTicket.locators.RaiseNewTicketLocators;

public class RaiseNewTicket {
	private WebDriver driver;
	private Waits waits;
	private RaiseNewTicketLocators newTicketLoc;
	private Helpdesk helpdesk;

	public HashMap<Integer, String> fileList = new HashMap<Integer, String>();

	public RaiseNewTicket(WebDriver driver) {
		this.driver = driver;
		this.waits = new Waits(driver);
		this.newTicketLoc = new RaiseNewTicketLocators();
		this.helpdesk = new Helpdesk(driver);
		PageFactory.initElements(driver, newTicketLoc);
	}

	public void openCreateTicketPage() {

		this.driver.get(this.helpdesk.prop.getProperty("base_url") + this.helpdesk.prop.getProperty("raise_ticket"));
		this.waits.waitForLoading(20);
	}

	public void addCC(String value) {
		this.newTicketLoc.addCCBtn.click();
		SystemService.clearandSendkeys(this.newTicketLoc.ccField, value);
	}

	public void addBCC(String value) {
		this.newTicketLoc.addCCBtn.click();
		this.waits.waitForElementVisiblity(this.newTicketLoc.bccField, 10);
		SystemService.clearandSendkeys(this.newTicketLoc.bccField, value);
	}

	// uploading file by file upload window
	public void addAttachment(String fileWithPath) throws InterruptedException {
		this.waits.waitForElementVisiblity(this.newTicketLoc.attachFile, 10);
		this.newTicketLoc.attachFile.click();
		StringSelection selection = new StringSelection(fileWithPath);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
		try {
			Robot robo = new Robot();
			robo.keyPress(KeyEvent.VK_CONTROL);
			robo.keyPress(KeyEvent.VK_V);
			robo.keyRelease(KeyEvent.VK_V);
			robo.keyRelease(KeyEvent.VK_CONTROL);
			Thread.sleep(20);
			robo.keyPress(KeyEvent.VK_TAB);
			robo.keyRelease(KeyEvent.VK_TAB);
			Thread.sleep(20);
			robo.keyPress(KeyEvent.VK_ENTER);
			robo.keyRelease(KeyEvent.VK_ENTER);
		} catch (AWTException e) {
			e.printStackTrace();
		}
		this.waits.waitForElementVisiblity(this.newTicketLoc.attachedFile, 10);
	}

	public String createTicket(HashMap<String, String> values) {
		this.waits.waitForElementVisiblity(this.newTicketLoc.msgField, 30);
		SystemService.takeSS("screenshot");
		SystemService.clearandSendkeys(this.newTicketLoc.subjectField, values.get("subject"));
		WebElement element = driver.findElement(this.newTicketLoc.msgBox());
		((JavascriptExecutor) this.driver)
				.executeScript("var ele=arguments[0]; ele.innerHTML = '" + values.get("message") + "';", element);
		this.newTicketLoc.fileInput.sendKeys(values.get("file"));
		this.waits.waitForElementVisiblity(this.newTicketLoc.attachedFile, 10);
		this.getFileNames();
		SystemService.clearandSendkeys(this.newTicketLoc.nameField, values.get("name"));
		SystemService.clearandSendkeys(this.newTicketLoc.emailField, values.get("email"));
		SystemService.clearandSendkeys(this.newTicketLoc.phNumber, values.get("phone"));
		this.newTicketLoc.createTicketBtn.click();

		return this.ticketCreatedMsg();
	}

	public String ticketCreatedMsg() {
		this.waits.waitForElementVisiblity(this.newTicketLoc.customMsg, 20);
		return this.newTicketLoc.customMsg.getText().toString();
	}

	public WebElement MsgElement() {
		return this.newTicketLoc.customMsg;
	}

	public boolean Msg() {
		return this.newTicketLoc.customMsg.isDisplayed();
	}

	public boolean isattached() {
		return this.newTicketLoc.attachedFile.isDisplayed();
	}

	public void getFileNames() {

		List<WebElement> files = this.driver.findElements(By.className("hf-attach-file_content"));
		for (int i = 0; i < files.size(); i++) {
			String name = this.driver.findElement(By.xpath("//div[@data-file-id='" + i + "']/span")).getText();
			fileList.put(i, name);

		}
	}

}

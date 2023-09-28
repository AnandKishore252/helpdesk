package happyfox.helpdesk_automation.pages.manage;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import happyfox.helpdesk_automation.SystemService;
import happyfox.helpdesk_automation.Waits;
import happyfox.helpdesk_automation.locators.TicketPageLocators;
import happyfox.helpdesk_automation.locators.TicketTableLocators;

public class Tickets {

	public WebDriver driver;
	private Waits waits;
	private TicketPageLocators ticketPageLoc;
	private TicketTableLocators ticketTableLoc;
	public String ticketId;

	public Tickets(WebDriver driver) {
		this.driver = driver;
		waits = new Waits(driver);
		ticketPageLoc = new TicketPageLocators();
		ticketTableLoc = new TicketTableLocators();

		PageFactory.initElements(driver, ticketPageLoc);
		PageFactory.initElements(driver, ticketTableLoc);
	}

	public void ticketsTableViewFilter(List<String> value) {
		this.waits.waitForElementToBeClickable(this.ticketTableLoc.tableViewFilterBtn, 15);
		this.ticketTableLoc.tableViewFilterBtn.click();
		List<WebElement> checkboxes = this.ticketTableLoc.filterListCheckBox();
		for (WebElement checkbox : checkboxes) {
			if (checkbox.isSelected()) {
				checkbox.click();
			}
		}
		for (int i = 0; i < value.size(); i++) {
			this.ticketTableLoc.filterListLabel(value.get(i)).click();
		}
		this.ticketTableLoc.tableViewFilterBtn.click();
	}

	public boolean isTicketPresent(String ticketSubject) {
		return this.ticketTableLoc.ticketSubject(ticketSubject).isDisplayed();
	}

	public boolean isTicketAttachmentVisibleOnTable(String ticketSubject) {
		return this.ticketTableLoc.ticketAttachmentVisibleOnTable(ticketSubject).isDisplayed();
	}

	public boolean isTicketAttachmentVisibleOnTicketPage(String name) {
		return this.ticketPageLoc.ticketAttachmentVisibleOnPage(name).isDisplayed();
	}

	public String attachmentFileName() {
		return this.ticketPageLoc.attachment.getText();
	}

	public String getTicketId(String ticketSubject) {
		String ticketUrl = this.ticketSubject(ticketSubject).getAttribute("href");
		String id = "";
		try {
			URL parsedUrl = new URL(ticketUrl);
			id = parsedUrl.getPath().substring(14);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return id;
	}

	public HashMap<String, String> getTicketDetailPageAttributes() {

		HashMap<String, String> attributes = new HashMap<String, String>();
		attributes.put("status", this.ticketPageLoc.ticketBoxStaus.getText());
		attributes.put("priority", this.ticketPageLoc.ticketBoxPriority.getText());
		attributes.put("assignee", this.ticketPageLoc.ticketBoxasignee.getText());
		attributes.put("owner", this.ticketPageLoc.ticketBoxOwner.getText());
		attributes.put("duedate", this.ticketPageLoc.ticketBoxOwner.getText());
		attributes.put("category", this.ticketPageLoc.ticketCategory.getText());
		return attributes;
	}

	public WebElement ticketSubject(String ticketSubject) {
		return this.ticketTableLoc.ticketSubject(ticketSubject);
	}

	public String getTicketStatus(String ticketSubject) {
		return this.ticketTableLoc.getStatus(ticketSubject).getText();
	}

	public String getTicketPriority(String ticketSubject) {
		return this.ticketTableLoc.getPriority(ticketSubject).getText();
	}

	public WebElement ticketRow() {
		return this.ticketTableLoc.ticketRow(ticketId);
	}

	public void openTicket(String ticketSubject) {
		this.waits.waitForElementToBeClickable(this.ticketTableLoc.ticketSubject(ticketSubject), 10);
		this.driver.get(this.ticketTableLoc.ticketSubject(ticketSubject).getAttribute("href"));
		this.waits.waitForLoading(20);
	}

	public WebElement id() {
		this.waits.waitForElementVisiblity(this.ticketPageLoc.ticketBoxId, 7);
		return this.ticketPageLoc.ticketBoxId;
	}

	public WebElement ticketBoxStatus() {
		return this.ticketPageLoc.ticketBoxStaus;
	}

	public WebElement ticketBoxPriority() {
		return this.ticketPageLoc.ticketBoxPriority;
	}

	public WebElement ticketBox() {
		return this.ticketPageLoc.ticketBox;
	}

	public HashMap<String, String> replyWithCannedAction(String value) {
		this.waits.waitForElementVisiblity(this.ticketPageLoc.taskBox, 20);
		this.ticketPageLoc.reply.click();
		// this.waits.waitForElementVisiblity(this.ticketPageLoc.draftMsg, 35);
		this.ticketPageLoc.cannedAction.click();
		this.waits.waitForElementVisiblity(this.ticketPageLoc.cannedActionSearch, 10);
		this.ticketPageLoc.cannedActionSearch.sendKeys(value);
		this.ticketPageLoc.cannedActionQuery(value).click();
		SystemService.mouseHover(this.ticketPageLoc.useThisBtn);
		this.ticketPageLoc.useThisBtn.click();
		this.waits.waitForElementVisiblity(this.ticketPageLoc.cannedStatus, 20);

		HashMap<String, String> cannedAttributes = new HashMap<String, String>();
		cannedAttributes.put("status", this.ticketPageLoc.cannedStatus.getText());
		cannedAttributes.put("priority", this.ticketPageLoc.cannedPriority.getText());
		this.ticketPageLoc.cannedActionEditorTagBox.click();
		cannedAttributes.put("tags", this.tagCount());
		this.ticketPageLoc.cannedActionEditorTagBox.click();
		cannedAttributes.put("assignee", this.ticketPageLoc.asignee.getText());
		cannedAttributes.put("dueDate", this.ticketPageLoc.dueDate.getText());
		cannedAttributes.put("timeSpent", this.ticketPageLoc.timeSpent.getText());
		this.ticketPageLoc.addReplyBtn.click();
		this.waits.waitForLoading(25);
		return cannedAttributes;
	}

	public String tagCount() {
		int size = this.ticketPageLoc.cannedActionEditorTagList().size();
		String count = Integer.toString(size);
		return count;
	}

	public List<String> getTags() {

		List<String> tagList = new ArrayList<String>();
		List<WebElement> tags = this.ticketPageLoc.tagList();
		for (WebElement tag : tags) {
			tagList.add(tag.getText());
		}
		return tagList;
	}

}

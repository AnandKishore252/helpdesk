package happyfox.helpdesk_automation.suites;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import happyfox.helpdesk_automation.Helpdesk;
import happyfox.helpdesk_automation.SystemService;
import happyfox.helpdesk_automation.Waits;
import happyfox.helpdesk_automation.listener.TestListener;
import happyfox.helpdesk_automation.pages.manage.Priority;
import happyfox.helpdesk_automation.pages.manage.Status;
import happyfox.helpdesk_automation.pages.manage.Tickets;
import happyfox.helpdesk_automation.pages.support.newTicket.RaiseNewTicket;

@Listeners(TestListener.class)
public class HelpdeskTest {

	public WebDriver driver;
	public WebDriver driver2;
	public Helpdesk helpdesk;
	public Status status;
	public Priority priority;
	public Waits wait;
	public Tickets tickets;
	public RaiseNewTicket newticket;

	public String timeStamp;
	public HashMap<String, String> statusValue;
	public HashMap<String, String> priorityValue;
	public HashMap<String, String> ticketValues;
	public HashMap<String, String> cannedActionValue;
	public HashMap<String, String> cannedActionAttributes;
	public HashMap<String, String> windows;
	public List<String> tableFilterlist;
	public List<String> cannedActionTaglist;

	@SuppressWarnings("serial")
	@BeforeClass(groups = { "scenario one", "scenario two", "scenario three" })
	public void setUp() {

		this.driver = SystemService.getDriver();
		this.driver.manage().window().maximize();
		this.driver.manage().deleteAllCookies();

		this.helpdesk = new Helpdesk(SystemService.driver);
		this.status = new Status(SystemService.driver);
		this.priority = new Priority(SystemService.driver);
		this.wait = new Waits(SystemService.driver);
		this.tickets = new Tickets(SystemService.driver);

		this.helpdesk.openHelpdesk();
		this.helpdesk.agentLogin();
		this.helpdesk.waitForTableLoad();

		this.timeStamp = String.valueOf(new Date().getTime());

		this.statusValue = new HashMap<String, String>() {

			{
				put("name", "Issue Created " + timeStamp);
				put("color", SystemService.colorCode());
				put("behaviour", "Pending");
				put("description", "Status when a new ticket is created in HappyFox");
				put("isdefault", "Yes");
			}
		};

		this.priorityValue = new HashMap<String, String>() {

			{
				put("name", "Assisstance Required " + timeStamp);

				put("description", "Low");
				// put("helpText", "Low Priority");
				put("isdefault", "Yes");
			}
		};
		this.ticketValues = new HashMap<String, String>() {

			{
				put("subject", "Test Ticket " + timeStamp);
				put("message", "For Testing Purpose" + timeStamp);
				put("name", "AK" + timeStamp);
				put("email", "aK" + timeStamp + "@yopmail.com");
				put("phone", "9988776655");
				put("cc", "aK" + timeStamp + "@yopmail.com");
				put("bcc", "aK" + timeStamp + "@yopmail.com");
				put("file", System.getProperty("user.dir") + "\\screenshots\\screenshot.png");
				put("filename", "");

			}
		};

		this.cannedActionValue = new HashMap<String, String>() {
			{
				put("name", "Reply to customer query");

			}
		};

		this.tableFilterlist = new ArrayList<String>();
		this.tableFilterlist.add("Status");
		this.tableFilterlist.add("Priority");
		this.tableFilterlist.add("Ticket ID");

		this.windows = new HashMap<String, String>();
	}

	@AfterClass(groups = { "scenario one", "scenario two", "scenario three" })
	public void tearDown() {
		this.helpdesk.agentLogout();
		String loggedOutMsg = this.helpdesk.logoutConfirmation();
		String expectedConfirmationMsg = "You have logged out successfully.";
		Assert.assertEquals(loggedOutMsg, expectedConfirmationMsg);
		this.driver.quit();
	}

	@BeforeMethod
	public void beforeMethod() {
		this.timeStamp = String.valueOf(new Date().getTime());
	}

	@AfterMethod
	public void afetrMethod() {

	}

	@Test(description = " verify status and priority is created", enabled = true, priority = 1, groups = {
			"scenario one", "scenario two" })
	public void createStatuAndPriority() {
		this.helpdesk.refresh();
		this.helpdesk.waitForTableLoad();

		this.helpdesk.selectMenu("Statuses");
		this.status.createStatus(this.statusValue);
		this.status.isStatusDisplayed(this.statusValue.get("name"));
		Assert.assertEquals(this.status.isStatusPresent(this.statusValue.get("name")), true);

		this.helpdesk.refresh();

		this.helpdesk.selectMenu("Priorities");
		this.priority.createPriority(this.priorityValue);
		this.priority.isPriorityDisplayed(this.priorityValue.get("name"));
		Assert.assertEquals(this.priority.isPriorityPresent(this.priorityValue.get("name")), true);

		this.helpdesk.gotoHomePage();
		this.helpdesk.refresh();
		this.helpdesk.waitForTableLoad();
	}

	@Test(description = "verify that created status and Priority is default", enabled = true, priority = 2, dependsOnMethods = "createStatuAndPriority", groups = {
			"scenario two" })
	public void makeAsDefault() {
		this.helpdesk.selectMenu("Statuses");
		this.status.openStatusTableView();
		this.status.makeDefault(this.statusValue.get("name"));
		this.status.isStatusDisplayed(this.statusValue.get("name"));
		Assert.assertTrue(this.status.getDefaultStatus().equalsIgnoreCase(this.statusValue.get("name")));

		this.helpdesk.refresh();

		this.helpdesk.selectMenu("Priorities");
		this.priority.openPriorityTableView();
		this.priority.makeDefault(this.priorityValue.get("name"));
		this.priority.isPriorityDisplayed(this.priorityValue.get("name"));
		Assert.assertTrue(this.priority.getDefaultPriority().equalsIgnoreCase(this.priorityValue.get("name")));

		this.helpdesk.gotoHomePage();
		this.helpdesk.waitForTableLoad();

	}

	@Test(description = "Create new ticket from support--> new ticket page", enabled = true, priority = 3, dependsOnMethods = {
			"createStatuAndPriority", "makeAsDefault" }, groups = { "scenario two" })
	public void createNewTicket() {

		this.driver2 = new ChromeDriver();
		this.newticket = new RaiseNewTicket(this.driver2);
		this.driver2.manage().window().maximize();
		this.newticket.openCreateTicketPage();
		String responseMsg = this.newticket.createTicket(this.ticketValues);
		String expectedMsg = "Your ticket has been created";
		Assert.assertTrue(responseMsg.contains(expectedMsg));

		this.driver2.manage().window().minimize();
		this.driver2.quit();

		this.helpdesk.gotoHomePage();

	}

	@Test(description = "verify that the created ticket is in default status and default priority", enabled = true, priority = 4, dependsOnMethods = "createNewTicket", groups = {
			"scenario two" })
	public void ticketCreatedWithDefaultStatusAndPriority() {
		this.helpdesk.refresh();

		Assert.assertTrue(this.tickets.isTicketPresent(this.ticketValues.get("subject")));
		Assert.assertTrue(this.tickets.isTicketAttachmentVisibleOnTable(this.ticketValues.get("subject")));

		// this.helpdesk.refresh();

		this.tickets.ticketId = this.tickets.getTicketId(this.ticketValues.get("subject"));
		this.tickets.openTicket(this.ticketValues.get("subject"));

		// this.helpdesk.refresh();

		Assert.assertTrue(this.tickets.ticketBoxStatus().getText().equalsIgnoreCase(this.statusValue.get("name")));
		Assert.assertTrue(this.tickets.ticketBoxPriority().getText().equalsIgnoreCase(this.priorityValue.get("name")));
		Assert.assertTrue(this.tickets.isTicketAttachmentVisibleOnTicketPage(this.tickets.attachmentFileName()));

		this.helpdesk.gotoHomePage();
	}

	@Test(description = "replying using canned action and verifying the property changes", enabled = true, priority = 5, dependsOnMethods = "ticketCreatedWithDefaultStatusAndPriority", groups = {
			"scenario two" })
	public void replyUsingCannedAction() {
		this.helpdesk.refresh();
		this.helpdesk.waitForTableLoad();
		this.tickets.openTicket(this.ticketValues.get("subject"));
		// this.helpdesk.refresh();
		this.cannedActionAttributes = this.tickets.replyWithCannedAction(this.cannedActionValue.get("name"));
		String expectedResponseMsg = "Ticket has been updated successfully";
		Assert.assertEquals(this.helpdesk.getBannerMessage(), expectedResponseMsg);

		this.helpdesk.refresh();

		this.cannedActionTaglist = tickets.getTags();
		String tagListCount = Integer.toString(cannedActionTaglist.size());

		Assert.assertTrue(cannedActionAttributes.get("status").equalsIgnoreCase("shipment"));
		Assert.assertTrue(cannedActionAttributes.get("priority").equalsIgnoreCase("critical"));
		Assert.assertTrue(cannedActionAttributes.get("tags").equalsIgnoreCase(tagListCount));

	}

	@Test(description = "verify that the ticket is updated to canned action attributes", enabled = true, priority = 6, dependsOnMethods = "replyUsingCannedAction", groups = {
			"scenario two" })
	public void ticketCondtionUpdatedAfterCannedReply() {
		this.helpdesk.gotoHomePage();
		this.helpdesk.refresh();
		this.helpdesk.waitForTableLoad();

		Assert.assertTrue(this.tickets.isTicketPresent(this.ticketValues.get("subject")));
		Assert.assertTrue(this.tickets.getTicketStatus(this.ticketValues.get("subject")).equalsIgnoreCase("shipment"));
		Assert.assertTrue(
				this.tickets.getTicketPriority(this.ticketValues.get("subject")).equalsIgnoreCase("critical"));

		this.helpdesk.refresh();
		this.helpdesk.waitForTableLoad();
	}

	@Test(description = "delete the created status and priority", enabled = true, priority = 7, dependsOnMethods = "createStatuAndPriority", groups = {
			"scenario three" })
	public void deleteStatusAndPriority() {

		this.helpdesk.selectMenu("Statuses");
		Assert.assertTrue(this.status.isStatusPresent(this.statusValue.get("name")));
		this.helpdesk.refresh();

		String statusBannerMsg = this.status.deleteCustomStatus(this.statusValue.get("name"), "TEST_STATUS");
		String expectedstatsusBannerMsg = "Status \"" + statusValue.get("name") + "\" is deleted successfully.";
		Assert.assertEquals(statusBannerMsg, expectedstatsusBannerMsg);

		this.helpdesk.refresh();

		this.helpdesk.selectMenu("Priorities");
		Assert.assertTrue(this.priority.isPriorityPresent(this.priorityValue.get("name")));
		this.helpdesk.refresh();

		String priorityBannerMsg = this.priority.deleteCustomPriority(this.priorityValue.get("name"), "Low");
		String expectedpriorityBannerMsg = "Priority \"" + this.priorityValue.get("name")
				+ "\" is deleted successfully.";
		Assert.assertEquals(priorityBannerMsg, expectedpriorityBannerMsg);

		this.helpdesk.gotoHomePage();
		this.helpdesk.refresh();
		this.helpdesk.waitForTableLoad();

	}

}

package happyfox.helpdesk_automation.listener;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

	int retryCounter = 0;

	int maxLimit = 2;

	public boolean retry(ITestResult result) {
		if (!result.isSuccess()) {
			if (retryCounter < maxLimit) {
				retryCounter++;
				return true;
			}
		}
		return false;
	}

}

package my.silly.filebrowser;

import java.io.PrintWriter;

import org.junit.platform.engine.discovery.ClassSelector;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

public class RunJUnit5TestsFromJava {
	SummaryGeneratingListener listener = new SummaryGeneratingListener();

	public void runOne() {

		ClassSelector selector1 = DiscoverySelectors.selectClass(TestTableViewUI.class);
		ClassSelector selector2 = DiscoverySelectors.selectClass(TestTreeViewUI.class);
		ClassSelector selector3 = DiscoverySelectors.selectClass(TestBreadcrumbLabel.class);
		LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
				.selectors(selector1, selector2, selector3).build();
		Launcher launcher = LauncherFactory.create();
		TestPlan testPlan = launcher.discover(request);
		launcher.registerTestExecutionListeners(listener);
		launcher.execute(request);
	}

	public static void main(String[] args) {
		RunJUnit5TestsFromJava testRunner = new RunJUnit5TestsFromJava();
		testRunner.runOne();

		TestExecutionSummary summary = testRunner.listener.getSummary();
		summary.printTo(new PrintWriter(System.out));
	}
}
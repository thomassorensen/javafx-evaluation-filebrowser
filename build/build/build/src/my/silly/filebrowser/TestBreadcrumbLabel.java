package my.silly.filebrowser;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;

import javafx.scene.control.Label;
import my.silly.filebrowser.data.Item;
import my.silly.filebrowser.data.ItemType;
import my.silly.filebrowser.ui.BreadcrumbLabel;

class TestBreadcrumbLabel {

	private Label label;
	private BreadcrumbLabel bread;
	private String sep;

	@BeforeEach
	void before() throws TimeoutException {
		FxToolkit.registerPrimaryStage();
		label = new Label("");
		bread = new BreadcrumbLabel(label);
		sep = File.separator;
	}
	
	@AfterEach
	void after() throws TimeoutException {
		FxToolkit.cleanupStages();
	}
	
	@Test
	void set_breadcrumb() throws TimeoutException {
		
		Item root = new Item("root", null, ItemType.Root);
		Item item1 = new Item("crumb1", root, ItemType.Folder);
		Item item2 = new Item("crumb2", item1, ItemType.Folder);
		
		bread.set(item2);
		assertEquals("root"+sep+"crumb1"+sep+"crumb2"+sep, label.getText());
		
	}

	@Test
	void add_breadcrumb() throws TimeoutException {
		Item root = new Item("root", null, ItemType.Root);
		Item item1 = new Item("crumb1", root, ItemType.Folder);
		Item item2 = new Item("crumb2", item1, ItemType.Folder);
		bread.addBreadcrumb(root).addBreadcrumb(item1).addBreadcrumb(item2);
		assertEquals("root"+sep+"crumb1"+sep+"crumb2"+sep, label.getText());
	}
	
	@Test
	void empty_breadcrumb() throws TimeoutException {
		assertEquals("", label.getText());
	}
	
}

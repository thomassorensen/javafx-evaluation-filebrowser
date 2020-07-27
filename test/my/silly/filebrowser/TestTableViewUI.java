package my.silly.filebrowser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Set;

import org.junit.internal.TextListener;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.JUnitCore;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.assertions.api.TableViewAssert;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.service.query.NodeQuery;

import com.sun.javafx.scene.control.skin.ContextMenuContent;
import com.sun.javafx.scene.control.skin.ContextMenuContent.MenuItemContainer;

import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import my.silly.filebrowser.data.Item;

@ExtendWith(ApplicationExtension.class)
public class TestTableViewUI {

	@Start
	public void start(Stage primaryStage) throws IOException {
		new Main().start(primaryStage);
	}

	@Test
	public void initial_state(FxRobot robot) {
		FxAssert.verifyThat(CSS.INFO_LABEL, LabeledMatchers.hasText("dc"));
		FxAssert.verifyThat(CSS.BREADCRUMB_LABEL, LabeledMatchers.hasText("\\"));
		FxAssert.verifyThat(CSS.BTN_MOVE_UP_ONE_LEVEL, LabeledMatchers.hasText("↑"));

		FxAssert.verifyThat(CSS.FILE_MENU_ITEM, LabeledMatchers.hasText("File"));
		FxAssert.verifyThat(CSS.EDIT_MENU, LabeledMatchers.hasText("Edit"));
		FxAssert.verifyThat(CSS.VIEW_MENU_ITEM, LabeledMatchers.hasText("View"));
	}

	@Test
	void open_file_menu(FxRobot robot) throws InterruptedException {

		robot.clickOn(CSS.FILE_MENU_ITEM).moveTo(CSS.NEW_MENU);
		MenuItem newMenuItem = robot.lookup(CSS.FILE_NEW_MENU).queryAs(ContextMenuContent.MenuItemContainer.class)
				.getItem();
		Assertions.assertThat(newMenuItem).hasText("New");

		robot.moveTo(CSS.NEW_MENU);

		MenuItem newFileItem = robot.lookup(CSS.FILE_NEW_MENU_ITEMS).nth(0)
				.queryAs(ContextMenuContent.MenuItemContainer.class).getItem();
		Assertions.assertThat(newFileItem).hasText("File");

		MenuItem newFolderItem = robot.lookup(CSS.FILE_NEW_MENU_ITEMS).nth(1)
				.queryAs(ContextMenuContent.MenuItemContainer.class).getItem();
		Assertions.assertThat(newFolderItem).hasText("Folder");

	}

	@Test
	void open_edit_menu(FxRobot robot) {
		robot.clickOn(CSS.EDIT_MENU).moveTo(CSS.EDIT_RENAME_MENU_ITEM);
		MenuItem renameMenuItem = robot.lookup(CSS.EDIT_MENU + " .menu-item").nth(0)
				.queryAs(ContextMenuContent.MenuItemContainer.class).getItem();
		MenuItem deleteMenuItem = robot.lookup(CSS.EDIT_MENU + " .menu-item").nth(1)
				.queryAs(ContextMenuContent.MenuItemContainer.class).getItem();
		Assertions.assertThat(renameMenuItem).hasText("Rename");
		Assertions.assertThat(deleteMenuItem).hasText("Delete");
	}

	@Test
	void open_view_menu(FxRobot robot) {
		robot.clickOn(CSS.VIEW_MENU_ITEM);
		robot.moveTo("#viewTreeRadioitem");

		Set<MenuItemContainer> editMenuItems = robot.lookup("#viewMenuitem .menu-item")
				.queryAllAs(ContextMenuContent.MenuItemContainer.class);
		assertEquals(editMenuItems.size(), 2);

		RadioMenuItem viewTableMenuItem = (RadioMenuItem) robot.lookup("#viewMenuitem #viewTableRadioitem")
				.queryAs(ContextMenuContent.MenuItemContainer.class).getItem();
		RadioMenuItem viewTreeMenuItem = (RadioMenuItem) robot.lookup("#viewMenuitem #viewTreeRadioitem")
				.queryAs(ContextMenuContent.MenuItemContainer.class).getItem();
		;

		Assertions.assertThat(viewTableMenuItem.isSelected()).isTrue();
		Assertions.assertThat(viewTableMenuItem).hasText("Table");
		Assertions.assertThat(viewTreeMenuItem).hasText("Tree");
	}

	@Test
	void browse_table_view(FxRobot robot) {
		TableView<Item> tableView = robot.lookup(CSS.TABLEVIEW).queryTableView();
		Node nBack = robot.lookup(CSS.BTN_MOVE_UP_ONE_LEVEL).query();

		openFolder(tableView, 0, robot);
		assertRow(robot, tableView, 0, "aquaman", "\\dc\\aquaman", "Folder");
		assertRow(robot, tableView, 1, "character_list.txt", "\\dc\\character_list.txt", "File");
		openFolder(tableView, 0, robot);
		assertRow(robot, tableView, 0, "mmmmmomoa.png", "\\dc\\aquaman\\mmmmmomoa.png", "File");
		assertRow(robot, tableView, 1, "movie-review-collection.txt", "\\dc\\aquaman\\movie-review-collection.txt",
				"File");

		robot.clickOn(nBack);
		assertRow(robot, tableView, 0, "aquaman", "\\dc\\aquaman", "Folder");
		assertRow(robot, tableView, 1, "character_list.txt", "\\dc\\character_list.txt", "File");
		robot.clickOn(nBack);
		assertRow(robot, tableView, 0, "dc", "\\dc", "Folder");
		assertRow(robot, tableView, 1, "marvel", "\\marvel", "Folder");
		assertRow(robot, tableView, 2, "fact_marvel_beats_dc.txt", "\\fact_marvel_beats_dc.txt", "File");
	}

	private TableViewAssert<?> assertRow(FxRobot robot, TableView<?> tableView, int row, Object... cells) {
		return Assertions.assertThat(tableView).as(String.format("Checking the content of row %s is %s", row, cells))
				.containsRowAtIndex(row, cells);
	}

	private void openFolder(TableView<Item> tableView, int row, FxRobot robot) {
		Node nCell = robot.lookup(CSS.TABLEVIEW_CELL).nth(row).query();
		robot.moveTo(tableView).moveTo(nCell).doubleClickOn();
	}

	@Test
	void delete_item(FxRobot robot) {
		NodeQuery cellQuery = robot.lookup(CSS.TABLEVIEW_CELL).nth(0);
		Node q = cellQuery.query();
		robot.clickOn(q).clickOn(CSS.EDIT_MENU).clickOn(CSS.EDIT_DELETE_MENU_ITEM);
		TableView<Item> tableView = robot.lookup(CSS.TABLEVIEW).queryTableView();
		Assertions.assertThat(tableView).as("Check that dc is no longer in the tableview").doesNotContainRow("dc",
				"\\dc", "Folder");
		assertRow(robot, tableView, 0, "marvel", "\\marvel", "Folder");
		assertRow(robot, tableView, 1, "fact_marvel_beats_dc.txt", "\\fact_marvel_beats_dc.txt", "File");

	}

	@Test
	void add_file(FxRobot robot) {
		robot.clickOn(CSS.FILE_MENU_ITEM).moveTo(CSS.NEW_MENU).clickOn(CSS.NEW_FILE_MENU_ITEM);
		TableView<Item> tableView = robot.lookup(CSS.TABLEVIEW).queryTableView();
		assertRow(robot, tableView, 3, "New File", "\\New File", "File");
	}

	@Test
	void add_folder(FxRobot robot) {
		robot.clickOn(CSS.FILE_MENU_ITEM).moveTo(CSS.NEW_MENU).moveTo(CSS.NEW_FILE_MENU_ITEM).clickOn(CSS.NEW_FOLDER_MENU_ITEM).write("hello").type(KeyCode.ENTER).sleep(1000);
		TableView<Item> tableView = robot.lookup(CSS.TABLEVIEW).queryTableView();
		assertRow(robot, tableView, 3, "helloNew Folder", "\\helloNew Folder", "Folder");
	}

	@Test
	void rename_folder(FxRobot robot) {
		Node qFirstCell = robot.lookup(CSS.TABLEVIEW_CELL).nth(0).query();
		robot.clickOn(qFirstCell).clickOn(CSS.EDIT_MENU).clickOn(CSS.EDIT_RENAME_MENU_ITEM).write("Hello").type(KeyCode.ENTER);
		TableView<Item> tableView = robot.lookup(CSS.TABLEVIEW).queryTableView();
		Assertions.assertThat(tableView).containsRowAtIndex(0, "Hello", "\\Hello", "Folder");
	}

	@Test
	void rename_file(FxRobot robot) {
		Node qFirstCell = robot.lookup(CSS.TABLEVIEW_CELL).nth(2).query();
		robot.clickOn(qFirstCell).clickOn(CSS.EDIT_MENU).clickOn(CSS.EDIT_RENAME_MENU_ITEM).write("Hello").type(KeyCode.ENTER);
		TableView<Item> tableView = robot.lookup(CSS.TABLEVIEW).queryTableView();
		Assertions.assertThat(tableView).containsRowAtIndex(2, "Hello", "\\Hello", "File");
	}

	@Test
	void selection(FxRobot robot) {
		Node qFirstCell = robot.lookup(CSS.TABLEVIEW_CELL).nth(0).query();
		Node qSecondCell = robot.lookup(CSS.TABLEVIEW_CELL).nth(1).query();
		Node qThirdCell = robot.lookup(CSS.TABLEVIEW_CELL).nth(2).query();

		robot.clickOn(qFirstCell);

		assertState(robot, "dc", "\\");

		robot.clickOn(qSecondCell);

		assertState(robot, "marvel", "\\");

		robot.clickOn(qThirdCell);
		assertState(robot, "fact_marvel_beats_dc.txt", "\\");

		openFolder(robot.lookup(CSS.TABLEVIEW).queryTableView(), 0, robot);
		qFirstCell = robot.lookup(CSS.TABLEVIEW_CELL).nth(0).query();
		qSecondCell = robot.lookup(CSS.TABLEVIEW_CELL).nth(1).query();

		robot.clickOn(qFirstCell);

		assertState(robot, "aquaman", "\\dc\\");

		robot.clickOn(qSecondCell);

		assertState(robot, "character_list.txt", "\\dc\\");

	}

	private void assertState(FxRobot robot, String info, String breadcrb) {
		FxAssert.verifyThat(CSS.INFO_LABEL, LabeledMatchers.hasText(info));
		FxAssert.verifyThat(CSS.BREADCRUMB_LABEL, LabeledMatchers.hasText(breadcrb));
	}

	public static void main(String[] args) {
		JUnitCore junit = new JUnitCore();
		junit.addListener(new TextListener(System.out));
		junit.run(TestTableViewUI.class);
	}
	
}

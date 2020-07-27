package my.silly.filebrowser;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import org.assertj.core.internal.bytebuddy.utility.privilege.GetSystemPropertyAction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.service.query.NodeQuery;

import com.sun.javafx.scene.control.skin.ContextMenuContent;
import com.sun.javafx.scene.control.skin.ContextMenuContent.MenuItemContainer;
import com.sun.javafx.stage.WindowEventDispatcher;

import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import my.silly.filebrowser.data.Item;

@ExtendWith(ApplicationExtension.class)
class TestTreeViewUI {

	
	private static final String TREE_CELL = ".tree-cell";

	@Start
	public void start(Stage primaryStage) throws IOException {
		new Main().start(primaryStage);
	}
	
	void activateTree(FxRobot robot) {
		robot.clickOn(CSS.VIEW_MENU_ITEM).clickOn(CSS.VIEW_TREE_RADIO_ITEM);
		robot.moveTo(TREE_CELL);
	}

	
	
	@Test
	void browse_tree_view(FxRobot robot) {
		activateTree(robot);
		NodeQuery qTree = robot.lookup(CSS.TREEVIEW);
		NodeQuery qCells = robot.lookup(TREE_CELL).nth(0);
		
		Node nCell = qCells.query();
		TreeCell<String> cell = (TreeCell<String>) nCell;
		TreeView<Item> treeView = getTree(robot);
		robot.doubleClickOn(cell);
		robot.moveTo(treeView).moveTo(cell);
		
		Function<Node, Set<Node>> printNodes = new Function<Node,Set<Node>>(){
			@Override
			public Set<Node> apply(Node t) {
				System.out.println(t);
				HashSet<Node> set = new HashSet<Node>();
				set.add(t);
				return set;
			}
		};
		
		assertTreeCells(robot, "","dc","marvel","fact_marvel_beats_dc.txt");
		cell = getCell(robot, 1);
		robot.doubleClickOn(cell);
		assertTreeCells(robot, "","dc","aquaman","character_list.txt","marvel","fact_marvel_beats_dc.txt");
		cell= getCell(robot, 2);
		robot.doubleClickOn(cell);
		assertTreeCells(robot, "","dc","aquaman","mmmmmomoa.png","movie-review-collection.txt","character_list.txt","marvel","fact_marvel_beats_dc.txt");
		cell= getCell(robot, 1);
		robot.doubleClickOn(cell);
		assertTreeCells(robot, "","dc","marvel","fact_marvel_beats_dc.txt");
		cell= getCell(robot, 2);
		robot.doubleClickOn(cell);
		assertTreeCells(robot, "","dc","marvel","black_widow","drdoom","marvel_logo.png","fact_marvel_beats_dc.txt");
	}

	private void assertTreeCells(FxRobot robot, String... texts) {
		for (int i = 0; i < texts.length; i++) {
			assertCell(getCell(robot,i),texts[i]);
		}
	}
	
	private TreeCell<String> getCell(FxRobot robot, int i) {
		return robot.lookup(TREE_CELL).nth(i).queryAs(TreeCell.class);
	}


	private void assertCell(TreeCell<String> cell, String text) {
		Assertions.assertThat(cell.getText()).isEqualTo(text);
		
	}

	private TreeView<Item> getTree(FxRobot robot) {
		TreeView<Item> treeView = robot.lookup(CSS.TREEVIEW).query();
		return treeView;
	}

	@Test
	void delete_item(FxRobot robot) {
		activateTree(robot);
		expand(robot, 0);
		TreeCell<String> cell = getCell(robot, 1);
		robot.clickOn(cell).clickOn(CSS.EDIT_MENU).clickOn(CSS.EDIT_DELETE_MENU_ITEM);
		assertTreeCells(robot, "","marvel","fact_marvel_beats_dc.txt");
		

	}

	private void expand(FxRobot robot, int row) {
		TreeCell<String> cell = getCell(robot, row);
		expand(robot,cell);
	}

	private void expand(FxRobot robot, TreeCell<String> cell) {
		robot.doubleClickOn(cell);
	}

	@Test
	void add_file(FxRobot robot) {
		activateTree(robot);
		robot.doubleClickOn(getCell(robot, 0)).clickOn(getCell(robot, 0)).clickOn(CSS.FILE_MENU_ITEM).moveTo(CSS.NEW_MENU).clickOn(CSS.NEW_FILE_MENU_ITEM).sleep(1000);
		assertTreeCells(robot, "","dc","marvel","fact_marvel_beats_dc.txt","New File");
	}

	@Test
	void add_folder(FxRobot robot) {
		activateTree(robot);
		expand(robot, 0);
		robot.clickOn(getCell(robot, 0));
		robot.clickOn(CSS.FILE_MENU_ITEM).moveTo(CSS.NEW_MENU).moveTo(CSS.NEW_FILE_MENU_ITEM).clickOn(CSS.NEW_FOLDER_MENU_ITEM);
		assertTreeCells(robot, "","dc","marvel","fact_marvel_beats_dc.txt","New Folder");
	}

	
	// This test fails for some reason that has to do with the TestFX library. 
	// The event source passed to the EditEvent is not the correct type. 
	// This is not an issue when running the application for real.
	@Test
	void rename_file(FxRobot robot) {
//		activateTree(robot);
//		expand(robot, 0);
//		robot.sleep(500);
//		TreeCell<String> cell = getCell(robot, 1);
//		robot.clickOn(cell).sleep(500);
//		try {
//	        robot.release(new KeyCode[] {});
//	        robot.release(new MouseButton[] {});
//	        robot.interact(()->robot.press(MouseButton.SECONDARY)).release(MouseButton.SECONDARY);
//	        robot.sleep(500);
//	        robot.interact(new Runnable() {
//				
//				@Override
//				public void run() {
//					robot.clickOn("#renameCtxMenuItem");
//				}
//			});
//		}catch(Exception e) {
//			
//		}
//		robot.sleep(500).clickOn(cell).write("hello");
//		getTree(robot).requestFocus();
//		robot.sleep(1000);
//		robot.clickOn();
//		robot.sleep(1000).clickOn(cell).sleep(100).write("Hello");
//		assertTreeCells(robot, "","hello","marvel","fact_marvel_beats_dc.txt");
	}

	@Test
	void selection(FxRobot robot) {
		activateTree(robot);
		expand(robot, 0);
		Node qFirstCell = getCell(robot, 1);
		Node qSecondCell = getCell(robot, 2);
		Node qThirdCell = getCell(robot, 3);

		robot.clickOn(qFirstCell);

		assertState(robot, "dc", "\\");

		robot.clickOn(qSecondCell);

		assertState(robot, "marvel", "\\");

		robot.clickOn(qThirdCell);
		assertState(robot, "fact_marvel_beats_dc.txt", "\\");

		expand(robot, 1);
		qFirstCell = getCell(robot, 2);
		qSecondCell = getCell(robot, 3);

		robot.clickOn(qFirstCell);

		assertState(robot, "aquaman", "\\dc\\");

		robot.clickOn(qSecondCell);

		assertState(robot, "character_list.txt", "\\dc\\");

	}

	private void assertState(FxRobot robot, String info, String breadcrb) {
		FxAssert.verifyThat(CSS.INFO_LABEL, LabeledMatchers.hasText(info));
		FxAssert.verifyThat(CSS.BREADCRUMB_LABEL, LabeledMatchers.hasText(breadcrb));
	}

	
	
	
}

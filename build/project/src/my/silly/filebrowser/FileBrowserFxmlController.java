package my.silly.filebrowser;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import my.silly.filebrowser.data.Item;
import my.silly.filebrowser.data.ItemType;
import my.silly.filebrowser.ui.DataView;
import my.silly.filebrowser.ui.NotSupportedViewException;

public class FileBrowserFxmlController { 


	@FXML
	private MenuBar mainMenubar;

	@FXML
	private Menu fileMenuitem;

	@FXML
	private Menu newMenu;

	@FXML
	private MenuItem newFileMenuItem;

	@FXML
	private MenuItem newFolderMenuitem;

	@FXML
	private MenuItem quitMenuitem;

	@FXML
	private Menu editMenuitem;

	@FXML
	private MenuItem renameMenuitem1;

	@FXML
	private MenuItem deleteMenuitem;

	@FXML
	private Menu viewMenuitem;

	@FXML
	private RadioMenuItem viewTableRadioitem;

	@FXML
	private ToggleGroup dataviewToggleGroup;

	@FXML
	private RadioMenuItem viewTreeRadioitem;

	@FXML
	private HBox infoLayout;

	@FXML
	private Label infoLabel;

	@FXML
	private VBox mainContainerLayout;

	@FXML
	private Button btnMoveUpOneLevel;

	@FXML
	private Label breadcrumbLabel;

	@FXML
	private TableView<Item> tableview;

	@FXML
	private TableColumn<Item, String> nameColumn;

	@FXML
	private TableColumn<Item, String> pathColumn;

	@FXML
	private TableColumn<Item, String> typeColumn;

	@FXML
	private ContextMenu ctxMenuTableview;

	@FXML
	private Menu ctxMenu;

	@FXML
	private MenuItem newFileCtxMenuItem;
	@FXML
	private MenuItem newFolderCtxMenuItem;

	@FXML
	private MenuItem renameCtxMenuItem;

	@FXML
	private MenuItem deleteItemCtxMenuItem;

	@FXML
	private TreeView<String> treeview;

	private DataView dataview;


	@FXML
	void newFolder(ActionEvent event) {
		newItem(ItemType.Folder);
	}

	@FXML
	void newFile(ActionEvent event) {
		newItem(ItemType.File);
	}

	private void newItem(ItemType type) {
		dataview.newItem(type);
	}

	@FXML
	void deleteItem(ActionEvent event) {
		dataview.deleteItem();
	}

	@FXML
	void handleMouseClickedView(MouseEvent event) {
		dataview.handleDoubleclick(event);
	}

	@FXML
	void moveUpOneLevel(ActionEvent event) {
		dataview.moveUpOneLevel(event);
	}

	@FXML
	void quit(ActionEvent event) {
		Platform.exit();
	}

	@FXML
	void renameItem(ActionEvent event) {
		dataview.renameItem();
	}

	@FXML
	void showTable(ActionEvent event) {
		dataview.showTable();
	}

	@FXML
	void showTree(ActionEvent event) {
		dataview.showTree();
	}

	@FXML
	void initialize() throws NotSupportedViewException {
		// Dataview contains all the logic. This is so I can easily move to non FXML solution.
		dataview = new DataView(tableview, treeview, nameColumn, pathColumn, typeColumn, breadcrumbLabel, infoLabel);
	}

}

package my.silly.filebrowser.ui;

import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import my.silly.filebrowser.data.HardCodedItemProvider;
import my.silly.filebrowser.data.Item;
import my.silly.filebrowser.data.ItemType;

public class DataView {

	private ViewAdaper<? extends Control> currentView;
	private ViewAdaper<TableView<Item>> tableviewAdapter;
	private ViewAdaper<TreeView<String>> treeviewAdapter;

	
	public DataView(TableView<Item> tableview, TreeView<String> treeview, TableColumn<Item, String> nameColumn,
			TableColumn<Item, String> pathColumn, TableColumn<Item, String> typeColumn, Label breadcrumbLabel,
			Label infoLabel) throws NotSupportedViewException {
		super();
		
		// Adapters so that dataview can use both tableview and tree view with the same set of methods.  
		this.tableviewAdapter = ViewAdaper.createTableAdapter(tableview, nameColumn,pathColumn,typeColumn);
		this.treeviewAdapter = ViewAdaper.createTreeAdapter(treeview);
		
		// Wrap the label with BreadcrumbLabel that contians logic for manipulating the label text.
		BreadcrumbLabel breadcrumb = new BreadcrumbLabel(breadcrumbLabel);
		
		this.tableviewAdapter.setBreadcrumb(breadcrumb);
		this.tableviewAdapter.setInfoLabel(infoLabel);
		this.treeviewAdapter.setBreadcrumb(breadcrumb);
		this.treeviewAdapter.setInfoLabel(infoLabel);
		
		// create and set the itemprovider. Currently uses a dumb provider that uses hardcoded item creation.
		HardCodedItemProvider itemProvider = new HardCodedItemProvider();
		tableviewAdapter.setItemProvider(itemProvider);
		treeviewAdapter.setItemProvider(itemProvider);
		
		showTable();
	}

	/**
	 * Shows the table and hides the tree. The table view becomes the current view.
	 */
	public void showTable() {
		treeviewAdapter.hide();
		tableviewAdapter.show();
		currentView = tableviewAdapter;
		
	}

	/**
	 * Shows the tree and hides the table. The tree view becomes the current view.
	 */
	public void showTree() {
		treeviewAdapter.show();
		tableviewAdapter.hide();
		
		currentView = treeviewAdapter;
	}

	/**
	 * Delete the selected item in the current view.
	 */
	public void deleteItem() {
		currentView.deleteItem();
	}

	/**
	 * Activate editing for the selected cell in the current view. 
	 */
	public void renameItem() {
		currentView.renameItem();
	}

	/**
	 * Moves up to the parent of the selected item. Behaviour depends on what is the current view.
	 * @param event
	 */
	public void moveUpOneLevel(ActionEvent event) {
		currentView.moveUpOneLevel(event);
	}

	/**
	 * Adds a new item to the view. 
	 * @param type {@link ItemType} the item type being created.
	 */
	public void newItem(ItemType type) {
		currentView.newItem(type);
		
	}

	/**
	 * Handles the doubleclick event. Behaviour depends on the current view.
	 * @param event {@link MouseEvent}
	 */
	public void handleDoubleclick(MouseEvent event) {
		currentView.handleDoubleclick(event);
		
	}

}

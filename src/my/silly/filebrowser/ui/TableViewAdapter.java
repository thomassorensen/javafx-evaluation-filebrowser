package my.silly.filebrowser.ui;


import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import my.silly.filebrowser.data.Item;
import my.silly.filebrowser.data.ItemType;
import my.silly.filebrowser.valuefactory.ItemTypeValueFactory;

/**
 * TableViewAdapter implements ViewAdapter. 
 * This allows for a consistent api without having to worry about 
 * what type of view control is being used. 
 * @author ThomasSörensen
 *
 */
public class TableViewAdapter extends ViewAdaper<TableView<Item>> {

	private TableColumn<Item, String> nameColumn;
	private TableColumn<Item, String> pathColumn;
	private TableColumn<Item, String> typeColumn;

	/**
	 * Create a TableViewAdapter
	 * 
	 * @param view The tablview to use.
	 * @param nameColumn The name column.
	 * @param pathColumn The path column
	 * @param typeColumn The item type column.
	 */
	public TableViewAdapter(TableView<Item> view, TableColumn<Item, String> nameColumn,
			TableColumn<Item, String> pathColumn, TableColumn<Item, String> typeColumn) {
		super(view);
		this.nameColumn = nameColumn;
		this.pathColumn = pathColumn;
		this.typeColumn = typeColumn;
		
		setupNameColumn(view, nameColumn);
		setValueFactories();
		setupSelection();
		
		// When the current parent property 
		// changes update the table view so it shows the new parents children.
		getCurrentParentProperty().addListener(new ChangeListener<Item>() {
			@Override
			public void changed(ObservableValue<? extends Item> observable, Item oldValue, Item newValue) {
				if (newValue !=null) {
					view.getItems().clear();
					view.getItems().addAll(newValue.getChildren());
				}
			}
		});
	}

	/**
	 * Setup selection listener. The user selects an item it will update the info label.
	 */
	private void setupSelection() {
		getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Item>() {

			@Override
			public void changed(ObservableValue<? extends Item> observable, Item oldValue, Item newValue) {
				if (newValue != null && !newValue.equals(oldValue)) {
					setInfoLabel(newValue);
				}
			}
		});
	}

	/**
	 * Get the views selection model.
	 * @return
	 */
	protected TableViewSelectionModel<Item> getSelectionModel() {
		return view.getSelectionModel();
	}

	/**
	 * Setup the table name column of this table view so that editing works.
	 * @param view
	 * @param nameColumn
	 */
	private void setupNameColumn(TableView<Item> view, TableColumn<Item, String> nameColumn) {
		nameColumn.setEditable(false);
		nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		nameColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Item, String>>() {
			@Override
			public void handle(CellEditEvent<Item, String> t) {
				Item item = (Item) t.getTableView().getItems().get(t.getTablePosition().getRow());
				item.setName(t.getNewValue());
				nameColumn.setEditable(false);
				Platform.runLater(() -> {
					// This is so that java fx updates the view.
					view.getColumns().get(1).setVisible(false);
					view.getColumns().get(1).setVisible(true);

				});
			}
		});
	}

	/**
	 * Setup the cell value factories for this table views columns. 
	 */
	private void setValueFactories() {
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		pathColumn.setCellValueFactory(new PropertyValueFactory<>("path"));
		typeColumn.setCellValueFactory(new ItemTypeValueFactory());
	}

	/**
	 * Init items
	 */
	@Override
	protected void initItems() {
		view.getItems().clear();
		Item root = getItemProvider().getRoot();
		setCurrentParent(root);
	}

	/**
	 * Add a new item. When a new item is added it will get the name 'New File|Folder'
	 * After the the item is added the cell is set to edit so the user can change the name.
	 */
	@Override
	protected void newItem(ItemType type) {
		Item currentParent = getCurrentParent();
		Item item = new Item("New " + type.name(), currentParent, type);
		view.getItems().add(item);
		getSelectionModel().select(item);
		renameItem();

	}

	/**
	 * Activate edit mode on the selected cell.
	 */
	@Override
	protected void renameItem() {
		view.setEditable(true);
		nameColumn.setEditable(true);
		int row = getSelectionModel().getSelectedIndex();
		view.edit(row, nameColumn);
	}

	/**
	 * Delete the selected item.
	 */
	@Override
	protected void deleteItem() {
		ObservableList<Item> selectedItems = getSelectionModel().getSelectedItems();
		deleteItems(selectedItems);
	}

	/**
	 * Delete a list of items.
	 * @param selectedItems The items to be removed.
	 */
	private void deleteItems(ObservableList<Item> selectedItems) {
		selectedItems.forEach(i -> i.delete());
		view.getItems().removeAll(selectedItems);
	}

	/**
	 * Handle the doubleclick event. Doubleclicking will open a folder and show its children.
	 */
	@Override
	public void handleDoubleclick(MouseEvent event) {
		if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
			Item item = getSelectionModel().getSelectedItem();
			if (item != null && item.isFolder()) {
				super.setCurrentParent(item);
			}
		}
		event.consume();
	}

	/**
	 * Move up one level. This will show the children of the current parent. 
	 * If current parent is null then nothing happens.
	 */
	@Override
	public void moveUpOneLevel(ActionEvent event) {
		super.moveUpOneLevel(event);
	}

	/**
	 * Clear the table view items and re-add the current parent children.
	 */
	@Override
	protected void refreshItems() {
		view.getItems().clear();
		view.getItems().addAll(getCurrentParent().getChildren());
		getSelectionModel().clearAndSelect(0);
	}
}

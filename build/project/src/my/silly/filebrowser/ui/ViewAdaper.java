package my.silly.filebrowser.ui;

import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import my.silly.filebrowser.data.Item;
import my.silly.filebrowser.data.ItemProvider;
import my.silly.filebrowser.data.ItemType;

/**
 * The ViewAdapter is used by the table and tree view so that we can work with the different
 * views using a consistent api.
 * @author ThomasSörensen
 *
 * @param <T>
 */
public abstract class ViewAdaper<T extends Control> {
	private BreadcrumbLabel breadcrumb;
	private Label infoLabel;
	protected T view;
	private ItemProvider itemProvider;
	private SimpleObjectProperty<Item> currentParentItem;
	
	protected ViewAdaper(T view) {
		this.view= view;
		currentParentItem = new SimpleObjectProperty<Item>();
		currentParentItem.addListener((obs,oldItem,newItem)->{
			setBreadcrumb(newItem);
			refreshItems();
		});
	}

	/**
	 * Refresh the items.
	 */
	protected void refreshItems() {
		//Implement this method in classes that extend this class if required.
	};

	/**
	 * Create a ViewAdapter that uses a {@link TableView}s.
	 * @param tableview The table view.
	 * @param nameColumn The name column
	 * @param pathColumn The path column
	 * @param typeColumn The type column
	 * @return The TableViewAdapter.
	 */
	public static ViewAdaper<TableView<Item>> createTableAdapter(TableView<Item> tableview, TableColumn<Item, String> nameColumn, TableColumn<Item, String> pathColumn, TableColumn<Item, String> typeColumn) {
		return new TableViewAdapter(tableview, nameColumn,pathColumn,typeColumn);
	}
	
	/**
	 * Create a ViewAdapter for a {@link TreeView}
	 * @param treeview The TreeView
	 * @return The TreeViewAdapter
	 */
	public static ViewAdaper<TreeView<String>> createTreeAdapter(TreeView<String> treeview) {
		return new TreeViewAdapter(treeview);
	}
	
	/**
	 * The BreadcrumbLabel to be used.
	 * @param breadcrumbLabel
	 */
	public void setBreadcrumb(BreadcrumbLabel breadcrumbLabel) {
		this.breadcrumb = breadcrumbLabel;
		
	}

	/**
	 * Set the InfoLabel. 
	 * @param infoLabel
	 */
	public void setInfoLabel(Label infoLabel) {
		this.infoLabel = infoLabel;
	}

	/**
	 * Get the item provider
	 * @return
	 */
	protected ItemProvider getItemProvider() {
		return itemProvider;
	}

	/**
	 * Set the item provider.
	 * @param itemProvider
	 */
	public void setItemProvider(ItemProvider itemProvider) {
		this.itemProvider = itemProvider;
		initItems();
	}
	
	/**
	 * Show this ViewAdapters view control.
	 */
	public void show() {
		show(true);
	}
	
	/**
	 * Hide this ViewAdapters view control
	 */
	public void hide() {
		show(false);
	}

	/**
	 * Set the breadcrumb to the provided Item.
	 * @param leaf
	 */
	protected void setBreadcrumb(Item leaf) {
		breadcrumb.set(leaf);
	};
	
	/**
	 * Set so that the InfoLabel shows the provided items text.
	 * @param item
	 */
	protected void setInfoLabel(Item item) {
		infoLabel.setText(item.getName());
	}
	
	/**
	 * Set if this ViewAdapters view should be shown.
	 * @param isShow
	 */
	protected void show(boolean isShow) {
		view.setManaged(isShow);
		view.setVisible(isShow);
	}

	/**
	 * Get the current parent. 
	 * @return
	 */
	protected Item getCurrentParent() {
		return currentParentItem.get();
	}
	
	/**
	 * Set the current parent of this ViewAdapter to the provided items parent.
	 * @param item
	 */
	protected void select(Item item) {
		if (isNewCurrentParent(item)) {
			currentParentItem.set(item.getParent());
		}
		setInfoLabel(item);
	}
	
	/**
	 * If the provided item is not the same as the current parent.
	 * @param newValue 
	 * @return True if the current parent is different than the provided Item.
	 */
	private boolean isNewCurrentParent(Item newValue) {
		return newValue.getParent() != null && newValue.getParent().getItemType()==ItemType.Folder && !currentParentItem.getValue().equals(newValue.getParent());
	}
	
	/**
	 * Init the items.
	 */
	protected abstract void initItems();

	/**
	 * Create and add a new Item to this view.
	 * @param type The type of Item to create. {@link ItemType}
	 */
	protected abstract void newItem(ItemType type);

	/**
	 * Activate edit mode on the currently selected item.
	 */
	protected abstract void renameItem();

	/**
	 * Delete the selected Item.
	 */
	protected abstract void deleteItem();

	/**
	 * Set the current parent.
	 * @param item
	 */
	protected void setCurrentParent(Item item) {
		currentParentItem.set(item);
	}

	/**
	 * Handle doubleclick event. 
	 * @param event
	 */
	protected void handleDoubleclick(MouseEvent event) {
		//do nothing
	}

	/**
	 * Move up one level. Sets the current parent to the parent of the current parent.
	 * @param event
	 */
	public void moveUpOneLevel(ActionEvent event) {
		Item parent = currentParentItem.get();
		if (parent!=null && parent.getParent() != null && !parent.isRoot()) {
			setCurrentParent(parent.getParent());		
		}
	}

	
	/**
	 * Get the current parent item property.
	 * @return
	 */
	protected SimpleObjectProperty<Item> getCurrentParentProperty(){
		return currentParentItem;
	}
	
}

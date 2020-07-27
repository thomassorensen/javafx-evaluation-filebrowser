package my.silly.filebrowser.ui;

import java.util.List;

import com.sun.javafx.scene.SceneUtils;
import com.sun.media.jfxmedia.events.NewFrameEvent;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeItem.TreeModificationEvent;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import my.silly.filebrowser.data.Item;
import my.silly.filebrowser.data.ItemType;

/**
 * TreeViewAdapter implements ViewAdapter. 
 * This allows for a consistent api without having to worry about 
 * what type of view control is being used. 
 * @author ThomasSörensen
 *
 */
public class TreeViewAdapter extends ViewAdaper<TreeView<String>> {

	/**
	 * Create a TreeViewAdapter with the provided TreeView.
	 * @param view
	 */
	public TreeViewAdapter(TreeView<String> view) {
		super(view);
		this.view.setEditable(false);
		setCellFactory(view);
		setupSelectionListener();
	}

	/**
	 * Setup the selection listener. Sets the info label and current parent.
	 */
	private void setupSelectionListener() {
		getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {

			@Override
			public void changed(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldItem,
					TreeItem<String> newItem) {
				if (newItem != null && !newItem.equals(oldItem)) {
					if (newItem instanceof TreeFileItem) {
						TreeFileItem<String> newFileitem = (TreeFileItem<String>) newItem;
						Item item = newFileitem.getItem();
						if (item.getParent() != null && !item.getParent().equals(getCurrentParent())) {
							setCurrentParent(item.getParent());
						}
						setInfoLabel(item);
					}
				}
			}
		});
	}

	/**
	 * Setup the cell factory. 
	 * @param view
	 */
	private void setCellFactory(TreeView<String> view) {
		view.setCellFactory(new Callback<TreeView<String>, TreeCell<String>>() {
			@Override
			public TreeCell<String> call(TreeView<String> p) {
				return new TextFieldTreeCellImpl();
			}
		});
	}

	/**
	 * Set the root of the tree view.
	 */
	@Override
	protected void initItems() {
		view.setRoot(new TreeFileItem<Item>(getItemProvider().getRoot()));
		view.setShowRoot(true);
	}

	/**
	 * Get the selection model of this tree view.
	 * @return
	 */
	protected MultipleSelectionModel<TreeItem<String>> getSelectionModel() {
		return view.getSelectionModel();

	}

	/**
	 * Create and add a new item to this treeview. 
	 */
	@Override
	protected void newItem(ItemType type) {
		TreeFileItem<String> parentTreeItem = (TreeFileItem<String>) getSelectionModel().getSelectedItem();
		Item selectedItem = parentTreeItem.getItem();
		if (selectedItem.isFolder() || selectedItem.isRoot()) {
			Item newItem = new Item("New " + type.name(), parentTreeItem.getItem(), type);
			TreeFileItem<String> newTreeItem = new TreeFileItem<String>(newItem);
			parentTreeItem.getChildren().add(newTreeItem);
			getSelectionModel().select(newTreeItem);
		}

	}

	/**
	 * Activate editing mode on the selected cell.
	 */
	@Override
	protected void renameItem() {
		view.setEditable(true);
		TreeItem<String> selectedItem = getSelectionModel().getSelectedItem();
		view.edit(selectedItem);
		view.setEditable(false);

	}

	/**
	 * Delete the selected item.
	 */
	@Override
	protected void deleteItem() {
		ObservableList<TreeItem<String>> selecteditems = getSelectionModel().getSelectedItems();
		for (TreeItem<String> treeItem : selecteditems) {
			TreeFileItem<String> treeFileItem = (TreeFileItem<String>) treeItem;
			treeFileItem.delete();
		}

	}

	/**
	 * Collapses the parent of the selected item.
	 */
	@Override
	public void moveUpOneLevel(ActionEvent event) {
		TreeItem<String> selectedItem = getSelectionModel().getSelectedItem();
		if (selectedItem != null) {
			TreeItem<String> parent = selectedItem.getParent();
			if (parent == null) {
				parent = view.getRoot();
			}
			if (parent.getParent() == null) {
				parent.getChildren().forEach(i -> i.setExpanded(false));
				getSelectionModel().select(0);
			} else {
				parent.setExpanded(false);
				getSelectionModel().select(parent);
			}
		}
	}

}

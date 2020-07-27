package my.silly.filebrowser.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import my.silly.filebrowser.data.Item;

/**
 * Contains logic to handle the breadcrumb. 
 * Wraps a {@link Label}
 * @author ThomasSörensen
 *
 */
public class BreadcrumbLabel {

	private Label label;
	private ObservableList<Item> breadcrumbList;

	/**
	 * Creates a BreadcrumbLable. Provide a {@link Label} that will be updated by this BreadcrumbLabel.
	 * @param breadcrumbLabel The Label to use for showing the breadcrumb.
	 */
	public BreadcrumbLabel(Label breadcrumbLabel) {
		this.label = breadcrumbLabel;
		this.breadcrumbList = FXCollections.observableArrayList();
		
		// Add a change listener to the ObservableList. 
		// When the list is changed it will update the label
		breadcrumbList.addListener(new ListChangeListener<Item>() {
			@Override
			public void onChanged(Change<? extends Item> c) {
				updateLabel();				
			}
		});
		
	}
	
	/**
	 * Add an item to the breadcrumb and update the label.
	 * @param item 
	 * @return
	 */
	public BreadcrumbLabel addBreadcrumb(Item item) {
		breadcrumbList.add(item);
		return this;
	}
	
	/**
	 * Remove the laste item from the breadcrumb.
	 */
	public void removeLast() {
		if (!breadcrumbList.isEmpty()) {
			breadcrumbList.remove(breadcrumbList.size()-1);
		}
	}

	/**
	 * Get the last item in the breadcrumb.
	 * @return The last item.
	 */
	public Item getLast() {
		return breadcrumbList.get(breadcrumbList.size()-1);
	}
	
	/**
	 * Update the label text. 
	 * This will enumerate over breadcrumb list and create a new string 
	 * that is used when setting the test of the label.
	 */
	private void updateLabel() {
		StringBuilder bui = new StringBuilder();
		for (Item item : breadcrumbList) {
			bui.append(item.getName()).append(File.separator);
		}
		label.setText(bui.toString());
	}
	
	/**
	 * Clear the breadcrumb.
	 */
	public void clear() {
		breadcrumbList.clear();
	}
	
	/**
	 * Set the item that is being whose breadcrumb is represented. 
	 * It will traverse the provided items parents and add all the ancestors to the list.
	 * @param item The item whose breadcrumb will be represented.
	 */
	public void set(Item item) {
		List<Item> newBeadcrumb = new ArrayList<Item>();
		traverseUpAndAddItems(item,newBeadcrumb);
		breadcrumbList.clear();
		breadcrumbList.addAll(newBeadcrumb);
		
	}

	private void traverseUpAndAddItems(Item item, List<Item> newBeadcrumb) {
		if (item.getParent() != null) {
			traverseUpAndAddItems(item.getParent(), newBeadcrumb);
		}		
		newBeadcrumb.add(item);
	}
	
}

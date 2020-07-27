package my.silly.filebrowser.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;

/**
 * Represents a data item in. It uses SimpleStringProperty for the string fields
 * so that they can be bound to other properties and add listeners.
 * 
 * @See SimpleStringProperty
 * 
 * @author ThomasSörensen
 *
 */
public class Item {

	private final SimpleStringProperty name;
	private SimpleStringProperty path;
	private Item parent;
	private ItemType itemType;
	private List<Item> children;

	/**
	 * Creates a new item.
	 * 
	 * @param name The name of the item.
	 * @param parent The items parent
	 * @param itemType The item type. {@link ItemType}
	 */
	public Item(String name, Item parent, ItemType itemType) {
		this.name = new SimpleStringProperty(name);
		this.parent = parent;
		this.itemType = itemType;
		this.children = new ArrayList<Item>();
		if (parent != null) {
			parent.addChild(this);
		}
		this.path = new SimpleStringProperty();
	}

	/**
	 * Sets the children of this item.
	 * Replaces the existing children.
	 * @param children List of items.
	 */
	public void setChildren(List<Item> children) {
		this.children = children;
	}

	/**
	 * Add child to this item.
	 * @param child Item to be a child to this item.
	 */
	public void addChild(Item child) {
		this.children.add(child);
	}

	/**
	 * Remove an item from the list of this items children
	 * @param child Item to be removed
	 */
	public void removeChild(Item child) {
		this.children.remove(child);
	}

	/**
	 * Get the parent of this item. Null if it has no parent.
	 * @return This items parent item.
	 */
	public Item getParent() {
		return parent;
	}

	/**
	 * Get the name.
	 * @return The name
	 */
	public String getName() {
		return name.get();
	}

	/**
	 * Sets the name. Settings this value will rebuild the path.
	 * @param newValue The new value to replace the name with.
	 */
	public void setName(String newValue) {
		this.name.set(newValue);
		this.getPath();
	}

	/**
	 * Return the item type of this item.
	 * @return {@link ItemType} the item type of this item.
	 */
	public ItemType getItemType() {
		return itemType;

	}

	/**
	 * Gets the list of children for this item.
	 * @return List of items that are children.
	 */
	public List<Item> getChildren() {
		return children;
	}

	/**
	 * Removes the item from the list of children in this items parent.
	 */
	public void delete() {
		getParent().removeChild(this);
	}

	/**
	 * If the the items @{link ItemType}==ItemType.File
	 * @return True if the item type is File
	 */
	public boolean isFile() {
		return itemType == ItemType.File;
	}

	/**
	 * If the the items @{link ItemType}==ItemType.Folder
	 * @return True if the item type is Folder
	 */
	public boolean isFolder() {
		return itemType == ItemType.Folder;
	}
	
	/**
	 * If the the items @{link ItemType}==ItemType.Root
	 * @return True if the item type is Root
	 */
	public boolean isRoot() {
		return getItemType() == ItemType.Root;
	}
	/**
	 * Get this items path. It traverses the ancestors to build the path.
	 * @return This items path.
	 */
	public String getPath() {
		path.set(buildPath(this));
		return path.get();
	}

	/**
	 * Traverses the ancestors of this item to build this items path.
	 * @param item
	 * @return
	 */
	private String buildPath(Item item) {
		StringBuilder bui = new StringBuilder();
		buildPath(item, bui);
		return bui.toString();
	}

	
	private void buildPath(Item item, StringBuilder bui) {
		if (item.getParent() != null && item.getParent().isFolder()) {
			buildPath(item.getParent(), bui);
		}
		bui.append(File.separator).append(item.getName());
	}
	

	@Override
	public String toString() {
		return "Item [name=" + name + ", parent=" + (parent == null ? "none" : parent.getName()) + ", itemType="
				+ itemType + "]";
	}
	
}

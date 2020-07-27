package my.silly.filebrowser.ui;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import my.silly.filebrowser.data.Item;
import my.silly.filebrowser.data.ItemType;

/**
 * Custom TreeItem that contains the Item instance it represents.
 * Enables lazy loading of the tree.
 * @author ThomasSörensen
 *
 * @param <T>
 */
public class TreeFileItem<T> extends TreeItem<String> {

	private Item item;
	private boolean isLoaded;

	private static final Image folder = new Image(TreeFileItem.class.getResourceAsStream("folder.png"));
	private static final Image file = new Image(TreeFileItem.class.getResourceAsStream("file.png"));

	public TreeFileItem(Item item) {
//			icon = new ImageView(new Image(getClass().getResourceAsStream(item.getItemType() == ItemType.File?"file.png" : "folder.png")));
		this.item = item;
		super.setGraphic(new ImageView(item.getItemType() == ItemType.File ? file : folder));
		super.setValue(item.getName());
	}

	@Override
	public boolean isLeaf() {
		return item.isFile();
	}

	/**
	 * Get the this TreeFileItems children. Uses lazy loading.
	 */
	@Override
	public ObservableList<TreeItem<String>> getChildren() {
		if (!isLoaded) {
			isLoaded = true;
			ArrayList<TreeFileItem<String>> children = new ArrayList<>();
			List<Item> items = item.getChildren();
			if (items != null && !items.isEmpty()) {
				for (Item item : items) {
					children.add(new TreeFileItem<String>(item));
				}
				super.getChildren().addAll(children);
			}
		}
		return super.getChildren();
	}

	public Item getItem() {
		return item;

	}

	public void delete() {
		this.getParent().getChildren().remove(this);
		item.delete();
	}

}

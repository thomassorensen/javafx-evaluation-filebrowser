package my.silly.filebrowser.data;

public class HardCodedItemProvider implements ItemProvider{

	
	/**
	 * Creates a simple file structure with files and folders.
	 */
	@Override
	public Item getRoot() {
		Item root = new Item("",null,ItemType.Root);
		Item item1 = new Item("dc",root,ItemType.Folder);
		Item item2 = new Item("marvel",root,ItemType.Folder);
		new Item("fact_marvel_beats_dc.txt",root,ItemType.File);
		
		Item item1_1 = new Item("aquaman",item1,ItemType.Folder);
		new Item("character_list.txt",item1,ItemType.File);

		Item item2_1 = new Item("black_widow",item2,ItemType.Folder);
		Item item2_2 = new Item("drdoom",item2,ItemType.Folder);
		new Item("marvel_logo.png",item2,ItemType.File);

		new Item("mmmmmomoa.png",item1_1,ItemType.File);
		new Item("movie-review-collection.txt",item1_1,ItemType.File);
		
		new Item("bw.png",item2_1,ItemType.File);
		new Item("why-the-widow-is-awesome.txt",item2_1,ItemType.File);
		
		new Item("the-doctor.png",item2_2,ItemType.File);
		
		return root;
		
	}
	
	
	
}

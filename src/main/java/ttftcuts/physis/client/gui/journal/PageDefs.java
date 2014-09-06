package ttftcuts.physis.client.gui.journal;

import com.google.common.collect.HashMultimap;

public class PageDefs {
	public static JournalArticle introduction;
	public static JournalArticle index;
	
	public static JournalArticle test;
	
	public static void init() {
		articleMap = HashMultimap.create();
		
		introduction = new JournalArticle(null,
			new JournalPageText("Adventurer...\n\nI am [name].\n\nI am the essence of the book you now hold. It has been... a great time, since I was last awakened.\n\nPerhaps you seek the secrets held within me? I shall aid you, but first, let me tell you what I can of my creation.\n\nMy race, the ones who forged and bound me, were known as the Physians. A people of harmony, peace, and strength of spirit. Masters of art and science, they strode across this land, all challenges falling to their singular vision."),
			new JournalPageText("Through the [ars artificium/ars biotica/ars technologia], they constructed a world upon world; a new heaven upon earth.\n\nWhere is this heaven now?\n\nI will tell you. In time.\n\nFirst, I shall teach you of the [ars whatever].")
		);
		index = new JournalArticle(null,
			new JournalPageText("Some index junk"),
			new JournalPageIndex(0)
		);
		
		test = new JournalArticle(Category.ITEM, new JournalPageTitle("YAY", "IT WORKED?"));
	}
	
	//##### ------------------------------------------------------- #####
	
	public static HashMultimap<Category, JournalArticle> articleMap;
	public enum Category {
		ITEM("Items", "Description"),
		EFFECT("Effects", "Description"),
		DEVICE("Devices", "Description"),
		;
		
		public String name;
		public String description;
		
		Category(String entryname, String desc) {
			this.name = entryname;
			this.description = desc;
		}
	}

}





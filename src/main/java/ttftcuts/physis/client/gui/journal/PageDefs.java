package ttftcuts.physis.client.gui.journal;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.google.common.collect.HashMultimap;

public class PageDefs {	
	//##### ------------------------------------------------------- #####
	public static JournalArticle introduction;

	public static JournalArticle test;
	//##### ------------------------------------------------------- #####
	public static JournalArticle index;
	
	public static void init() {
		articleMap = HashMultimap.create();
		
		//Physis.logger.info("Intro pages");
		
		introduction = new JournalArticle("intro", null,
			new JournalPageText("intro1"),
			new JournalPageText("intro2")
		);
		
		//Physis.logger.info("Pages");
		
		/*test = new JournalArticle("testitem", Category.ITEM, 
			new JournalPageTitle("testitem", "testitem"),
			new JournalPageCraftingRecipe(new ItemStack(Items.iron_pickaxe))
		).setStack(new ItemStack(Items.apple))
			.addRequirement("test", 1);*/
		
		test = new JournalArticleTrowels("testitem", Category.ITEM, 
			new JournalPageTitle("testitem", "testitem"),
			new JournalPageCraftingRecipe(new ItemStack(Items.iron_pickaxe))
		).setStack(new ItemStack(Items.apple))
			.addRequirement("test", 1);
		
		for (int i=0; i<100; i++) {
			new JournalArticle("testitem"+(i+2), Category.ITEM, 
				new JournalPageTitle("testitem", "testitem")
			).setStack(new ItemStack(Blocks.bookshelf));
			//.addRequirement("test", 1);
		}
		
		//Physis.logger.info("Index next");
		
		// these should be last - any articles after here won't be indexed!
		index = new JournalArticle("index", null,
			new JournalPageText("index"),
			new JournalPageIndex(0)
		);
		
		//Physis.logger.info("Categories");
		
		// category pages
		for(Category category: Category.values()) {
			JournalArticle cat = new JournalArticle(category.name, null,
				new JournalPageTitle(category.name, category.description)
			);
			int p = (int)Math.ceil(articleMap.get(category).size() / (double)JournalPageSubIndex.articlesPerPage);
			for (int i=0; i<p; i++) {
				cat.pages.add(new JournalPageSubIndex(category, i));
			}
			category.sectionMenu = cat;
		}
		
		//Physis.logger.info("Done");
	}
	
	//##### ------------------------------------------------------- #####
	
	public static HashMultimap<Category, JournalArticle> articleMap;
	public enum Category {
		ITEM("item", "item"),
		EFFECT("effect", "effect"),
		DEVICE("device", "device"),
		;
		
		public String name;
		public String description;
		public JournalArticle sectionMenu;
		
		Category(String entryname, String desc) {
			this.name = entryname;
			this.description = desc;
		}
	}

}





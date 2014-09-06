package ttftcuts.physis.client.gui.journal;

import java.util.ArrayList;
import java.util.List;

import ttftcuts.physis.Physis;
import ttftcuts.physis.client.gui.journal.PageDefs.Category;

public class JournalArticle {
	public Category category;
	public List<JournalPage> pages = new ArrayList<JournalPage>();
	
	public JournalArticle(Category cat, JournalPage... args) {
		category = cat;
		for(JournalPage page : args) {
			pages.add(page);
		}
		if (cat != null) {
			Physis.logger.info("cat: "+cat+", this: "+this+", articleMap: "+PageDefs.articleMap);
			PageDefs.articleMap.put(cat, this);
		}
	}
}

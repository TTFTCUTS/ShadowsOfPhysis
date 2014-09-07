package ttftcuts.physis.common.helper;

import net.minecraft.util.StatCollector;

public class LocalizationHelper {
	public final String titlePrefix = "physis.journal.title.";
	public final String articlePrefix = "physis.journal.text.";	
	public final String categoryPrefix = "physis.journal.category.";
	
	public LocalizationHelper() {
		
	}
	
	public String translate(String input) {
		String output = StatCollector.translateToLocal(input);
		
		// recursive junk;
		
		output = output.replace("\\n", "\n").replace("@r", "@r@0").replace('@', '\u00a7');
		
		return output;
	}
}

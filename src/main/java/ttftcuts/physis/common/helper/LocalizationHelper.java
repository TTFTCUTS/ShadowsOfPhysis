package ttftcuts.physis.common.helper;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
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
	
	@SuppressWarnings("unchecked")
	public List<String> wrapText(String input, int width) {
		FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
		
		List<String> output = fr.listFormattedStringToWidth(input, width);

		return output;
	}
	
	public List<String> translateAndWrap(String input, int width) {
		return wrapText(translate(input), width);
	}
	
	public String ticksToSeconds2dp(int ticks) {
		double seconds = ticks / 20.0;
		double rounded = Math.round(seconds*100.0)/100.0;
		if (rounded % 1.0 == 0.0) {
			return String.valueOf((int)rounded);
		}
		return String.valueOf(rounded);
	}
}

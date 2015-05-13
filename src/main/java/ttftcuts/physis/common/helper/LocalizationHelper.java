package ttftcuts.physis.common.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

import ttftcuts.physis.Physis;
import ttftcuts.physis.api.artifact.IArtifactEffect;
import ttftcuts.physis.api.artifact.IArtifactTrigger;
import ttftcuts.physis.common.artifact.PhysisArtifacts;
import ttftcuts.physis.common.story.StoryEngine;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

public class LocalizationHelper {
	public final String journalPrefix = "physis.journal.";
	public final String titlePrefix = journalPrefix +"title.";
	public final String articlePrefix = journalPrefix +"text.";	
	public final String categoryPrefix = journalPrefix +"category.";
	
	private static Pattern deepFormatPattern = Pattern.compile("%\\d*\\$s");
	private static Pattern translatePattern = Pattern.compile("\\$([^\\$]*?)\\$");
	private static Pattern storyVarPattern = Pattern.compile("\\#([^\\#]*?)\\#");
	
	public LocalizationHelper() {
		
	}
	
	public String translate(String input) {
		String output = this.translateInternal(input);
		
		return replaceColourStrings(output);
	}
	
	private String translateInternal(String input) {
		String output = StatCollector.translateToLocal(replaceStoryVariables(input));
		Matcher m = translatePattern.matcher(output);
		
		while(m.find()) {
			String s = m.group(1);
			String translated = this.translateInternal(s);
			
			output = output.replace("$"+m.group(1)+"$", translated);
		}
		return output;
	}
	
	public String replaceColourStrings(String input) {
		return input.replace("\\n", "\n").replace("@r", "@r@0").replace('@', '\u00a7');
	}
	
	public String replaceStoryVariables(String input) {
		Matcher m = storyVarPattern.matcher(input);
		
		while(m.find()) {
			String s = m.group(1);

			Side side = FMLCommonHandler.instance().getEffectiveSide();
			
			int storyval = StoryEngine.get(s, side == Side.CLIENT);
			
			String value = storyval == -1 ? "[Missing story var \""+s+"\"]" : String.valueOf(storyval);
			
			input = input.replace("#"+m.group(1)+"#", value);
		}
		
		return input;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> wrapText(String input, int width) {
		FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
		if (input == null) {
			return new ArrayList<String>();
		}
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

	public String deepFormat(String input, String... args) {
		String output = Physis.text.translate(input);
		
		while (deepFormatPattern.matcher(output).find()) {
			try {
				output = String.format(output, (Object[])args);
			} catch (Exception e) {
				Physis.logger.warn("Formatting error: "+e.getLocalizedMessage());
				break;
			}
		}
		
		return output;
	}
	
	public String formatArtifactNames(String input, ItemStack stack) {
		String output = Physis.text.translate(input);
		
		NBTTagCompound tag = stack.stackTagCompound.getCompoundTag(PhysisArtifacts.ARTIFACTTAG);
		if (tag.hasKey(PhysisArtifacts.TRIGGERTAG) && tag.hasKey(PhysisArtifacts.EFFECTTAG)) {
			IArtifactTrigger trigger = PhysisArtifacts.getTriggerFromSocketable(stack);
			IArtifactEffect effect = PhysisArtifacts.getEffectFromSocketable(stack);
			
			if (trigger != null && effect != null) {
				
				output = this.deepFormat(output, 
					Physis.text.translate(effect.getUnlocalizedEffectString()), 
					Physis.text.translate(trigger.getUnlocalizedTargetString()), 
					Physis.text.ticksToSeconds2dp(effect.getCooldown(trigger.getCooldownCategory())), 
					Physis.text.ticksToSeconds2dp(effect.getDuration(trigger.getCooldownCategory())),
					trigger.getTooltipInfo(),
					effect.getTooltipInfo()
				);
			}
		}
		
		return output;
	}
}

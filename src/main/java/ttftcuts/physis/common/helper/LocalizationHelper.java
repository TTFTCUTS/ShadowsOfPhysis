package ttftcuts.physis.common.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import ttftcuts.physis.Physis;
import ttftcuts.physis.api.artifact.IArtifactEffect;
import ttftcuts.physis.api.artifact.IArtifactTrigger;
import ttftcuts.physis.common.artifact.PhysisArtifacts;

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
	
	public LocalizationHelper() {
		
	}
	
	public String translate(String input) {
		String output = StatCollector.translateToLocal(input);
		
		// recursive junk;
		
		if (output != null) {
			output = output.replace("\\n", "\n").replace("@r", "@r@0").replace('@', '\u00a7');
		}
		
		return output;
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
	
	private static Pattern replaceable = Pattern.compile("%\\d*\\$s");
	public String recursiveFormat(String input, String... args) {
		String output = Physis.text.translate(input);
		
		while (replaceable.matcher(output).find()) {
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
				
				output = this.recursiveFormat(output, 
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

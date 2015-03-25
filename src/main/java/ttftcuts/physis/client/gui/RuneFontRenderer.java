package ttftcuts.physis.client.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ttftcuts.physis.Physis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RuneFontRenderer extends FontRenderer {

	private static ResourceLocation glyphSheet = new ResourceLocation(Physis.MOD_ID, "textures/font/runes.png");
	private static ResourceLocation unicodeGlyphSheet = new ResourceLocation(Physis.MOD_ID, "textures/font/runesunicode.png");
	
	private Map<Integer, List<Integer>> unicodeCharsByWidth;
	private Map<Integer, List<Integer>> asciiCharsByWidth;
	
	private int remixoffset = 0;
	
	public RuneFontRenderer() {
		super(Minecraft.getMinecraft().gameSettings, glyphSheet, Minecraft.getMinecraft().renderEngine, false);
		
		this.calculateUnicodeWidths();
		this.calculateAsciiWidths();
	}
	
	private static List<Character> forbidden = ImmutableList.of((char)25, (char)26, (char)27, (char)28, (char)29, (char)30, (char)31, (char)32, (char)33, (char)34, (char)39, (char)40, (char)41, (char)42, (char)43, (char)44, (char)45, (char)46, (char)47, (char)58, (char)59, (char)60, (char)61, (char)62, (char)63, (char)64, (char)91, (char)92, (char)93, (char)94, (char)95, (char)96, (char)123, (char)124, (char)125, (char)255);
	private boolean isCharAllowed(char c, boolean unicode) {
		if (!unicode) {
			return !forbidden.contains(c);
		}
		return Character.isAlphabetic(c) || Character.isDigit(c);
	}
	
	private void calculateUnicodeWidths() {
		this.unicodeFlag = true;
		unicodeCharsByWidth = new HashMap<Integer, List<Integer>>();
		for (int i=0; i<256; i++) {
			if (!isCharAllowed((char)i, true)) {continue;}
			int w = this.getCharWidth((char)i);
			List<Integer> category;
			if (!unicodeCharsByWidth.containsKey(w)) {
				category = new ArrayList<Integer>();
				unicodeCharsByWidth.put(w, category);
			}else {
				category = unicodeCharsByWidth.get(w);
			}
			category.add(i);
		}
		this.unicodeFlag = false;
	}
	
	private void calculateAsciiWidths() {
		asciiCharsByWidth = new HashMap<Integer, List<Integer>>();
		for (int i=0; i<256; i++) {
			if (!isCharAllowed((char)i, false)) {continue;}
			int w = this.charWidth[i];
			List<Integer> category;
			if (!asciiCharsByWidth.containsKey(w)) {
				category = new ArrayList<Integer>();
				asciiCharsByWidth.put(w, category);
			}else {
				category = asciiCharsByWidth.get(w);
			}
			category.add(i);
		}
	}

	private char replaceChar(char c, boolean unicode) {
		boolean u = this.unicodeFlag;
		this.unicodeFlag = unicode;
		int width = this.getCharWidth(c);
		this.unicodeFlag = u;
		
		Map<Integer, List<Integer>> charmap = unicode ? this.unicodeCharsByWidth : this.asciiCharsByWidth;
		
		char n = c;
		List<Integer> chars = charmap.get(width);
		if (chars != null && isCharAllowed(c, unicode)) {
			int cnum = (c * (remixoffset+c) + remixoffset + c) % chars.size();
			n = (char)(chars.get(cnum).intValue());
			remixoffset++;
		}
		return n;
	}
	
	@Override
	protected ResourceLocation getUnicodePageLocation(int page)
    {
        return unicodeGlyphSheet;
    }
	
	@Override
	protected float renderDefaultChar(int c, boolean italic) {
		char n = replaceChar((char)c, false);
		return super.renderDefaultChar(n, italic);
	}
	
	@Override
	protected float renderUnicodeChar(char c, boolean italic)
    {
        if (this.glyphWidth[c] == 0)
        {
            return 0.0F;
        }
        else
        {
    		char n = replaceChar(c, true);
    		return super.renderUnicodeChar(n, italic);
        }
    }
	
	@Override
	protected void renderStringAtPos(String string, boolean shadow) {
		super.renderStringAtPos(string, shadow);
		this.remixoffset = 0;
	}
	
	@Override
	public void onResourceManagerReload(IResourceManager manager) {
		super.onResourceManagerReload(manager);
		this.calculateAsciiWidths();
	}
}

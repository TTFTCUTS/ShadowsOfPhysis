package ttftcuts.physis.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

public class IconFontRenderer extends FontRenderer {

	@SuppressWarnings("unused")
	private static FontRenderer old;
	
	public static void setIconFont() {
		Minecraft mc = Minecraft.getMinecraft();
		
		FontRenderer icon = new IconFontRenderer();
		
		FontRenderer basic = mc.fontRenderer;
		
		icon.FONT_HEIGHT = basic.FONT_HEIGHT;
		icon.fontRandom = basic.fontRandom;
		icon.setBidiFlag(basic.getBidiFlag());
		icon.setUnicodeFlag(basic.getUnicodeFlag());
		
		old = mc.fontRenderer;
		mc.fontRenderer = icon;
	}
	
	public IconFontRenderer() {
		super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().renderEngine, false);
		
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.gameSettings.language != null) {
			this.setUnicodeFlag(mc.func_152349_b());
            this.setBidiFlag(mc.getLanguageManager().isCurrentLanguageBidirectional());
		}
	}

}

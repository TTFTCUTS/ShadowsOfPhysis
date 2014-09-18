package ttftcuts.physis.common.handler;

import java.util.Map.Entry;

import ttftcuts.physis.Physis;
import ttftcuts.physis.common.helper.TextureHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.client.event.TextureStitchEvent;

@SideOnly(Side.CLIENT)
public class TextureMapHandler {
	
	@SubscribeEvent
	public void onStitch(TextureStitchEvent.Pre event) {
		for (Entry<String, TextureAtlasSprite> entry : TextureHelper.addedIcons.entrySet()) {
			Physis.logger.info("Adding "+entry.getKey()+" to the map");
			event.map.setTextureEntry(entry.getKey(), entry.getValue());
		}
	}
}

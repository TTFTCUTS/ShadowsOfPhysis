package ttftcuts.physis.common.handler;

import ttftcuts.physis.common.helper.TextureHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class HudHandler {
	
	@SubscribeEvent
	public void onDrawScreen(RenderGameOverlayEvent.Post event) {
		TextureHelper.stackBuffer.framebufferRender(16, 16);
	}
}

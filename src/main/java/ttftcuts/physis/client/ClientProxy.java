package ttftcuts.physis.client;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import ttftcuts.physis.client.render.RenderDigSite;
import ttftcuts.physis.common.CommonProxy;
import ttftcuts.physis.common.handler.HudHandler;
import ttftcuts.physis.common.handler.TextureMapHandler;
import ttftcuts.physis.common.item.ItemTrowel;

public class ClientProxy extends CommonProxy {

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		
		MinecraftForge.EVENT_BUS.register(new TextureMapHandler());
		//MinecraftForge.EVENT_BUS.register(new HudHandler());
		
		if (OpenGlHelper.isFramebufferEnabled()) {
			ItemTrowel.buildDynamicTextures();
		}
		
		RenderingRegistry.registerBlockHandler(new RenderDigSite(RenderingRegistry.getNextAvailableRenderId()));
	}
}

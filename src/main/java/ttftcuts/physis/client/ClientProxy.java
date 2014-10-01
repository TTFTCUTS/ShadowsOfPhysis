package ttftcuts.physis.client;

import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import ttftcuts.physis.client.render.RenderDigSite;
import ttftcuts.physis.common.CommonProxy;
import ttftcuts.physis.common.handler.TextureMapHandler;
import ttftcuts.physis.common.handler.TooltipHandler;
import ttftcuts.physis.common.item.material.PhysisToolMaterial;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);

		MinecraftForge.EVENT_BUS.register(new TextureMapHandler());
		//MinecraftForge.EVENT_BUS.register(new HudHandler());
		MinecraftForge.EVENT_BUS.register(new TooltipHandler());
		
		RenderingRegistry.registerBlockHandler(new RenderDigSite(RenderingRegistry.getNextAvailableRenderId()));
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
		
		PhysisToolMaterial.buildTintData(10);
	}
}

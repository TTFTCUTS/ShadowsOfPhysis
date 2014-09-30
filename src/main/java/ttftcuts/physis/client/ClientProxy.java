package ttftcuts.physis.client;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import ttftcuts.physis.Physis;
import ttftcuts.physis.client.render.RenderDigSite;
import ttftcuts.physis.common.CommonProxy;
import ttftcuts.physis.common.handler.TextureMapHandler;
import ttftcuts.physis.common.handler.TooltipHandler;
import ttftcuts.physis.common.item.material.PhysisToolMaterial;

public class ClientProxy extends CommonProxy {

	public ClientProxy() {
		Physis.logger.info("CLIENT PROXY");
	}
	
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

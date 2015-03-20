package ttftcuts.physis.client;

import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import ttftcuts.physis.client.render.RenderDigSite;
import ttftcuts.physis.client.render.RenderSocketTable;
import ttftcuts.physis.client.render.item.RenderItemAlpha;
import ttftcuts.physis.client.render.item.RenderSocketable;
import ttftcuts.physis.client.render.tile.RenderTileDigSite;
import ttftcuts.physis.client.render.tile.RenderTileSocketTable;
import ttftcuts.physis.common.CommonProxy;
import ttftcuts.physis.common.PhysisItems;
import ttftcuts.physis.common.artifact.PhysisArtifacts;
import ttftcuts.physis.common.block.tile.TileEntityDigSite;
import ttftcuts.physis.common.block.tile.TileEntitySocketTable;
import ttftcuts.physis.common.compat.PhysisIntegration;
import ttftcuts.physis.common.handler.ClientTickHandler;
import ttftcuts.physis.common.handler.TextureMapHandler;
import ttftcuts.physis.common.handler.TooltipHandler;
import ttftcuts.physis.common.helper.ShaderHelper;
import ttftcuts.physis.common.story.StoryEngine;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		PhysisIntegration.preInitStart(event, true);
		PhysisIntegration.preInitEnd(event, true);
	}
	
	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		PhysisIntegration.initStart(event, true);
		
		FMLCommonHandler.instance().bus().register(new ClientTickHandler());
		MinecraftForge.EVENT_BUS.register(new TextureMapHandler());
		MinecraftForge.EVENT_BUS.register(new TooltipHandler());
		//MinecraftForge.EVENT_BUS.register(new BlockHighlightHandler());
		
		RenderingRegistry.registerBlockHandler(new RenderDigSite(RenderingRegistry.getNextAvailableRenderId()));
		RenderingRegistry.registerBlockHandler(new RenderSocketTable(RenderingRegistry.getNextAvailableRenderId()));
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDigSite.class, new RenderTileDigSite());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySocketTable.class, new RenderTileSocketTable());
		
		MinecraftForgeClient.registerItemRenderer(PhysisItems.socketable, new RenderSocketable());
		
		ShaderHelper.initShaders();
		PhysisArtifacts.clientInit();
		
		// make sure it doesn't come out null in that little while before loading.
		StoryEngine.reload(-1, true);
		
		PhysisIntegration.initEnd(event, true);
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
		PhysisIntegration.postInitStart(event, true);
		
		//RenderSocketed.injectRenderer();
		
		PhysisIntegration.postInitEnd(event, true);
	}
	
	@Override
	public void loadFinished(FMLLoadCompleteEvent event) {
		super.loadFinished(event);
		
		PhysisIntegration.loadFinished(event, true);
	}
}

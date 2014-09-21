package ttftcuts.physis.common;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import ttftcuts.physis.Physis;
import ttftcuts.physis.common.block.BlockDigSite;
import ttftcuts.physis.common.block.tile.TileEntityDigSite;
import ttftcuts.physis.common.item.block.ItemBlockPhysis;
//import ttftcuts.physis.common.item.block.ItemBlockPhysisWithMetadata;
import cpw.mods.fml.common.registry.GameRegistry;

public final class PhysisBlocks {
	public static Block digSiteDirt;
	public static Block digSiteSand;
	public static Block digSiteClay;
	
	public static void init() {
		digSiteDirt = registerBlock(new BlockDigSite("dirt"));
		digSiteSand = registerBlock(new BlockDigSite("sand"));
		digSiteClay = registerBlock(new BlockDigSite("clay"));
		registerTile(TileEntityDigSite.class, "digsite");
	}
	
	private static Block registerBlock(Block block, Class<? extends ItemBlock> itemBlock) {
		GameRegistry.registerBlock(block, itemBlock, block.getUnlocalizedName().replace("block.", ""));
		return block;
	}
	
	private static Block registerBlock(Block block) {
		return registerBlock(block, ItemBlockPhysis.class);
	}
	
	private static void registerTile(Class<? extends TileEntity> clazz, String id) {
		GameRegistry.registerTileEntity(clazz, Physis.MOD_ID + ":" + id);
	}
}

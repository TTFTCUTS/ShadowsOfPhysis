package ttftcuts.physis.common.handler;

import ttftcuts.physis.Physis;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockJukebox;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;

public class BlockHighlightHandler {

	@SubscribeEvent
	public void onDrawBlockHighlight(DrawBlockHighlightEvent event) {
		int x = event.target.blockX;
		int y = event.target.blockY;
		int z = event.target.blockZ;
		
		World world = event.player.worldObj;
		
		if (world != null) {
			Block block = world.getBlock(x, y, z);
			if (block == Blocks.jukebox) {
				TileEntity te = world.getTileEntity(x, y, z);
				if (te instanceof BlockJukebox.TileEntityJukebox) {
					//BlockJukebox.TileEntityJukebox jb = (BlockJukebox.TileEntityJukebox) te;
					
					Physis.logger.info("test");
				}
			}
		}
	}
}

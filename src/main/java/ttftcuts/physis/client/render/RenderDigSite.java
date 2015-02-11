package ttftcuts.physis.client.render;

import org.lwjgl.opengl.GL11;

import ttftcuts.physis.common.block.tile.TileEntityDigSite;
import ttftcuts.physis.common.helper.PhysisRenderHelper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderDigSite implements ISimpleBlockRenderingHandler {

	public static int renderid;
	
	public RenderDigSite(int id) {
		renderid = id;
	}
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		
		PhysisRenderHelper.renderStandardBlockAsItem(block, metadata, renderer);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {

		renderer.renderStandardBlock(block, x, y, z);
		
		TileEntity btile = world.getTileEntity(x, y, z);
		if (btile != null && btile instanceof TileEntityDigSite) {
			TileEntityDigSite tile = (TileEntityDigSite)btile;
			
			// base textures
			tile.renderlayer = 0;
			renderer.renderStandardBlock(block, x, y, z);
			
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			
			// symbol layers
			/*int extralayers = tile.renderdata.size();
			for (int i=0; i<extralayers; i++) {
				tile.renderlayer = i+1;
				renderer.renderStandardBlock(block, x, y, z);
			}*/

			// reset
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			tile.renderlayer = 0;
			
		} else {
			renderer.renderStandardBlock(block, x, y, z);
		}
		
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return renderid;
	}
}

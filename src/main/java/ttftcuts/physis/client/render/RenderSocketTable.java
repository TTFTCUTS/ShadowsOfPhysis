package ttftcuts.physis.client.render;

import org.lwjgl.opengl.GL11;

import ttftcuts.physis.common.block.tile.TileEntitySocketTable;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderSocketTable implements ISimpleBlockRenderingHandler {

	public static int renderid;
	
	public RenderSocketTable(int id) {
		renderid = id;
	}
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		GL11.glPushMatrix();
		GL11.glTranslatef(-0.5F, -0.65F, -0.5F);
		GL11.glScalef(0.7f, 0.7f, 0.7f);
		TileEntityRendererDispatcher.instance.renderTileEntityAt(new TileEntitySocketTable(), 0.0D, 0.0D, 0.0D, 0.0F);
		GL11.glPopMatrix();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		return false;
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

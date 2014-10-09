package ttftcuts.physis.client.render.tile;

import org.lwjgl.opengl.GL11;

import ttftcuts.physis.Physis;
import ttftcuts.physis.client.model.ModelSocketTable;
import ttftcuts.physis.common.block.tile.TileEntitySocketTable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class RenderTileSocketTable extends TileEntitySpecialRenderer {

	private static final ResourceLocation texture = new ResourceLocation(Physis.MOD_ID+":textures/blocks/socketTable.png");
	
	private static final ModelSocketTable model = new ModelSocketTable();
	
	// forward face to number of 90 degree rotations conversion
	private static final int[] rot = {-1,-1,2,0,1,3};
	
	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float tick) {
		TileEntitySocketTable table = (TileEntitySocketTable)tile;
		
		GL11.glPushMatrix();
		GL11.glColor4f(1f, 1f, 1f, 1f);
		GL11.glTranslated(x, y, z);
		
		Minecraft mc = Minecraft.getMinecraft();
		
		mc.renderEngine.bindTexture(texture);
		
		GL11.glTranslatef(0.5f, 1.5f, 0.5f);
		GL11.glScalef(1F, -1F, -1F);
		
		GL11.glRotatef(90F * rot[table.facing], 0F, 1F, 0F);
		
		model.render();
		
		GL11.glPopMatrix();
	}

}

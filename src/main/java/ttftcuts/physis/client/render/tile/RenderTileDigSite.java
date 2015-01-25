package ttftcuts.physis.client.render.tile;

import org.lwjgl.opengl.GL11;

import ttftcuts.physis.Physis;
import ttftcuts.physis.client.gui.GuiJournal;
import ttftcuts.physis.common.block.tile.TileEntityDigSite;
import ttftcuts.physis.common.helper.PhysisRenderHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class RenderTileDigSite extends TileEntitySpecialRenderer {

	private static final ResourceLocation texture = new ResourceLocation(Physis.MOD_ID+":textures/blocks/socketTable.png");
	
	private TileEntityDigSite digsite;
	
	public RenderTileDigSite() {}
	
	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float tick) {
		digsite = (TileEntityDigSite)tile;
		
		GL11.glPushMatrix();
		GL11.glColor4f(1f, 1f, 1f, 1f);
		GL11.glTranslated(x, y, z);
		
		Minecraft mc = Minecraft.getMinecraft();
		
		mc.renderEngine.bindTexture(texture);		
		//mc.renderEngine.bindTexture(GuiJournal.bookTextureLeft);		
		
		GL11.glTranslatef(0, 0, -.2f);
		
		//GL11.glRotatef(90F, 1F, 0F, 0F);
		//GL11.glTranslatef(-2F, -2F, -0.001F);
		
		PhysisRenderHelper.renderDoubleUVQuad(0, 0, 1, 1, 0, 1, 0, 1);
		
		//Physis.logger.info("rendering: "+digsite);
		//Physis.logger.info("render at "+x+","+y+","+z);
		
		GL11.glPopMatrix();
	}


}

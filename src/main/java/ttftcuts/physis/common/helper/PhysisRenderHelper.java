package ttftcuts.physis.common.helper;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class PhysisRenderHelper {
	private static Minecraft mc = Minecraft.getMinecraft();
	
	public static void renderStandardBlockAsItem(Block block, int metadata, RenderBlocks renderer) {
		Tessellator tess =  Tessellator.instance;
		block.setBlockBoundsForItemRender();
		renderer.setRenderBoundsFromBlock(block);
		
		GL11.glPushMatrix();

		GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		
		// bottom
		tess.startDrawingQuads();
		tess.setNormal(0.0F, -1.0F, 0.0F);
		renderer.renderFaceYNeg(block, 0.0F, 0.0F, 0.0F, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
		tess.draw();
		
		// top
		tess.startDrawingQuads();
		tess.setNormal(0.0F, 1.0F, 0.0F);
		renderer.renderFaceYPos(block, 0.0F, 0.0F, 0.0F, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
		tess.draw();
		
		// east
		tess.startDrawingQuads();
		tess.setNormal(0.0F, 0.0F, -1.0F);
		renderer.renderFaceZNeg(block, 0.0F, 0.0F, 0.0F, renderer.getBlockIconFromSideAndMetadata(block, 2, metadata));
		tess.draw();
		
		// west
		tess.startDrawingQuads();
		tess.setNormal(0.0F, 0.0F, 1.0F);
		renderer.renderFaceZPos(block, 0.0F, 0.0F, 0.0F, renderer.getBlockIconFromSideAndMetadata(block, 3, metadata));
		tess.draw();
		
		// north
		tess.startDrawingQuads();
		tess.setNormal(-1.0F, 0.0F, 0.0F);
		renderer.renderFaceXNeg(block, 0.0F, 0.0F, 0.0F, renderer.getBlockIconFromSideAndMetadata(block, 4, metadata));
		tess.draw();
		
		// south
		tess.startDrawingQuads();
		tess.setNormal(1.0F, 0.0F, 0.0F);
		renderer.renderFaceXPos(block, 0.0F, 0.0F, 0.0F, renderer.getBlockIconFromSideAndMetadata(block, 5, metadata));
		tess.draw();
		
		GL11.glPopMatrix();
	}
	
	public static void renderItemStack(ItemStack stack, int x, int y) {
		RenderItem render = new RenderItem();
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		render.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), stack, x, y);
		render.renderItemOverlayIntoGUI(mc.fontRenderer, mc.getTextureManager(), stack, x, y);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}
	
	public static void renderUVQuad(double x, double y, double w, double h, double u1, double u2, double v1, double v2) {
		Tessellator t = Tessellator.instance;
		t.startDrawingQuads();
		t.setBrightness(255);
		t.addVertexWithUV(x, y + h, 0, u2, v1);
		t.addVertexWithUV(x + w, y + h, 0, u1, v1);
		t.addVertexWithUV(x + w, y, 0, u1, v2);
		t.addVertexWithUV(x, y, 0, u2, v2);
		t.draw();
	}
	
	public static void renderDoubleUVQuad(double x, double y, double w, double h, double u1, double u2, double v1, double v2) {
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, 0);
		renderUVQuad(0,0,w,h,u1,u2,v1,v2);
		GL11.glRotatef(180, 0, 1, 0);
		renderUVQuad(-w,0,w,h,u2,u1,v1,v2);
		GL11.glPopMatrix();
	}
}

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
	
	//private static RenderItem renderItem = new RenderItem();
	public static void renderItemStack(ItemStack stack, int x, int y) {
		renderItemStack(stack,x,y,true, true, true);
	}
	public static void renderItemStack(ItemStack stack, int x, int y, boolean useCustomRenderers, boolean overlay) {
		renderItemStack(stack,x,y, useCustomRenderers, overlay, true);
	}
	
	private static RenderItem renderItem = new RenderItem();
	public static void renderItemStack(ItemStack stack, int x, int y, boolean useCustomRenderers, boolean overlay, boolean colour) {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		if (!colour) {
			renderItem.renderWithColor = false;
		}
		if (useCustomRenderers) {
			renderItem.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), stack, x, y);
		} else {
			renderItem.renderItemIntoGUI(mc.fontRenderer, mc.getTextureManager(), stack, x, y, true);
		} 
		if (overlay) {
			renderItem.renderItemOverlayIntoGUI(mc.fontRenderer, mc.getTextureManager(), stack, x, y);
		}
		renderItem.renderWithColor = true;
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}
	
	public static void drawColouredTexturedModalRect(int x, int y, float z, int u, int v, int w, int h, int colour)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(colour);
        tessellator.addVertexWithUV((double)(x + 0), (double)(y + h), (double)z, (double)((float)(u + 0) * f), (double)((float)(v + h) * f1));
        tessellator.addVertexWithUV((double)(x + w), (double)(y + h), (double)z, (double)((float)(u + w) * f), (double)((float)(v + h) * f1));
        tessellator.addVertexWithUV((double)(x + w), (double)(y + 0), (double)z, (double)((float)(u + w) * f), (double)((float)(v + 0) * f1));
        tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)z, (double)((float)(u + 0) * f), (double)((float)(v + 0) * f1));
        tessellator.draw();
    }
}

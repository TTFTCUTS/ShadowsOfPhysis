package ttftcuts.physis.common.helper;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import ttftcuts.physis.Physis;
import ttftcuts.physis.common.helper.recipe.ItemStackWrapper;

public class PhysisRenderHelper {
	public static final ResourceLocation largeGlyphs = new ResourceLocation(Physis.MOD_ID, "textures/gui/glyphs_large.png");
	public static final int largeGlyphCount = 64;
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
		renderItemStack(stack,x,y,true, true, false);
	}
	public static void renderItemStack(ItemStack stack, int x, int y, boolean useCustomRenderers, boolean overlay) {
		renderItemStack(stack,x,y, useCustomRenderers, overlay, false);
	}
	
	private static RenderItem renderItem = new RenderItem();
	public static void renderItemStack(ItemStack stack, int x, int y, boolean useCustomRenderers, boolean overlay, boolean encrypt) {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		FontRenderer font = encrypt ? Physis.runeFontRenderer : mc.fontRenderer;
		if (encrypt) {
			int glyph = Math.abs(stack.getDisplayName().hashCode()) % largeGlyphCount;
			int gx = glyph % 8;
			int gy = (int)Math.floor(glyph / 8);
			mc.renderEngine.bindTexture(largeGlyphs);
			drawTexturedModalRect(x,y, renderItem.zLevel+50, gx*16, gy*16, 16, 16, 0.5f);
		} else {
			if (useCustomRenderers) {
				renderItem.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), stack, x, y);
			} else {
				renderItem.renderItemIntoGUI(mc.fontRenderer, mc.getTextureManager(), stack, x, y, true);
			} 
		}
		if (overlay) {
			renderItem.renderItemOverlayIntoGUI(font, mc.getTextureManager(), stack, x, y);
		}
		renderItem.renderWithColor = true;
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}
	
	public static void drawColouredTexturedModalRect(int x, int y, float z, int u, int v, int w, int h, int colour, float scale)
    {
		if (scale <= 0f) { return; }
        float f = 0.00390625F / scale;
        float f1 = 0.00390625F / scale;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        if (colour >= 0) {
        	tessellator.setColorOpaque_I(colour);
        }
        tessellator.addVertexWithUV((double)(x + 0), (double)(y + h), (double)z, (double)((float)(u + 0) * f), (double)((float)(v + h) * f1));
        tessellator.addVertexWithUV((double)(x + w), (double)(y + h), (double)z, (double)((float)(u + w) * f), (double)((float)(v + h) * f1));
        tessellator.addVertexWithUV((double)(x + w), (double)(y + 0), (double)z, (double)((float)(u + w) * f), (double)((float)(v + 0) * f1));
        tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)z, (double)((float)(u + 0) * f), (double)((float)(v + 0) * f1));
        tessellator.draw();
    }
	public static void drawColouredTexturedModalRect(int x, int y, float z, int u, int v, int w, int h, int colour) {
		drawColouredTexturedModalRect(x,y,z,u,v,w,h,colour, 1f);
	}
	
	public static void drawTexturedModalRect(int x, int y, float z, int u, int v, int w, int h, float scale)
    {
		drawColouredTexturedModalRect(x,y,z,u,v,w,h,-1, scale);
    }
	public static void drawTexturedModalRect(int x, int y, float z, int u, int v, int w, int h)
    {
		drawColouredTexturedModalRect(x,y,z,u,v,w,h,-1, 1f);
    }
}

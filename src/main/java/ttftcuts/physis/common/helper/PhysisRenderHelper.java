package ttftcuts.physis.common.helper;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

public class PhysisRenderHelper {
	
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
}

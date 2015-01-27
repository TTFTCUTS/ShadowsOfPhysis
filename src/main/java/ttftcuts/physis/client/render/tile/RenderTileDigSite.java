package ttftcuts.physis.client.render.tile;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;

import ttftcuts.physis.Physis;
import ttftcuts.physis.api.item.ITrowel;
import ttftcuts.physis.common.PhysisBlocks;
import ttftcuts.physis.common.block.tile.TileEntityDigSite;
import ttftcuts.physis.puzzle.oddoneout.OddOneOutPuzzle;
import ttftcuts.physis.utils.TPair;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class RenderTileDigSite extends TileEntitySpecialRenderer {

	private static final ResourceLocation texture = new ResourceLocation(Physis.MOD_ID+":textures/blocks/digsite/shapes/overlay.png");
	
	// coord[number of symbols][symbol][pos][x or y]
	private static final double[][][][] coord = {
		{
			{{6/16.0,10/16.0},{10/16.0,10/16.0},{10/16.0,6/16.0},{6/16.0,6/16.0}}
		},
		{
			{{3/16.0,10/16.0},{7/16.0,10/16.0},{7/16.0,6/16.0},{3/16.0,6/16.0}},
			{{9/16.0,10/16.0},{13/16.0,10/16.0},{13/16.0,6/16.0},{9/16.0,6/16.0}}
		},
		{
			{{1/16.0,10/16.0},{5/16.0,10/16.0},{5/16.0,6/16.0},{1/16.0,6/16.0}},
			{{6/16.0,10/16.0},{10/16.0,10/16.0},{10/16.0,6/16.0},{6/16.0,6/16.0}},
			{{11/16.0,10/16.0},{15/16.0,10/16.0},{15/16.0,6/16.0},{11/16.0,6/16.0}},
		}
	};
	
	private static final double[][][] shapeuvs = {
		{{0,0.5},{0,0.5}},
		{{0.5,1},{0,0.5}},
		{{0,0.5},{0.5,1}}
	};
	
	private static final int[] shapecolours = {
		0xFF3333,
		0x33FF33,
		0x3333FF
	};
	
	private TileEntityDigSite digsite;
	
	public RenderTileDigSite() {}
	
	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float tick) {
		digsite = (TileEntityDigSite)tile;
		if (digsite.getWorldObj() == null) { return; }
		World world = digsite.getWorldObj();
		Block block = PhysisBlocks.digSiteDirt;
		
		Minecraft mc = Minecraft.getMinecraft();
		Tessellator t = Tessellator.instance;
		
		OddOneOutPuzzle puzzle = digsite.layerlist.get(digsite.currentlayer).puzzle;
		if (puzzle == null) { return; }
		
		if (mc.thePlayer == null) { return; }
		
		EntityPlayer player = mc.thePlayer;
		if (player.getHeldItem() == null || !(player.getHeldItem().getItem() instanceof ITrowel)) { return; }
		
		MovingObjectPosition hit = mc.objectMouseOver;
		if (!(hit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK 
				&& hit.blockX == digsite.xCoord
				&& hit.blockY == digsite.yCoord
				&& hit.blockZ == digsite.zCoord)) 
		{ return;}
		
		GL11.glPushMatrix();
		GL11.glColor4f(1f, 1f, 1f, 1f);
		GL11.glTranslated(x, y, z);
		
		mc.renderEngine.bindTexture(texture);

		GL11.glDisable(GL11.GL_CULL_FACE);
		
		// brightness
		int b = 0;
		// number of symbols
		int n = 0;
		// symbol to use
		int s = 0;
		// list of symbols in the current face
		List<TPair<Integer>> sym;
		
		// positive x
		if (block.shouldSideBeRendered(world, digsite.xCoord+1, digsite.yCoord, digsite.zCoord, 5))
		{
			b = block.getMixedBrightnessForBlock(world, digsite.xCoord+1, digsite.yCoord, digsite.zCoord);
			sym = puzzle.options.get(5).symbols;
			n = sym.size();
			
			for(int p = 0; p<n; p++) {
				s = sym.get(p).val1;
				t.startDrawingQuads();
				t.setColorOpaque_I(shapecolours[sym.get(p).val2]);
				t.setNormal(1, 0, 0);
				t.setBrightness(b);
				t.addVertexWithUV(1.1, coord[n-1][p][3][1], coord[n-1][p][3][0], shapeuvs[s][0][1], shapeuvs[s][1][1]);
				t.addVertexWithUV(1.1, coord[n-1][p][0][1], coord[n-1][p][0][0], shapeuvs[s][0][1], shapeuvs[s][1][0]);
				t.addVertexWithUV(1.1, coord[n-1][p][1][1], coord[n-1][p][1][0], shapeuvs[s][0][0], shapeuvs[s][1][0]);
				t.addVertexWithUV(1.1, coord[n-1][p][2][1], coord[n-1][p][2][0], shapeuvs[s][0][0], shapeuvs[s][1][1]);
				t.draw();
			}
		}
		
		// negetive x
		if (block.shouldSideBeRendered(world, digsite.xCoord-1, digsite.yCoord, digsite.zCoord, 4))
		{
			b = block.getMixedBrightnessForBlock(world, digsite.xCoord-1, digsite.yCoord, digsite.zCoord);
			sym = puzzle.options.get(4).symbols;
			n = sym.size();
			
			for(int p = 0; p<n; p++) {
				s = sym.get(p).val1;
				t.startDrawingQuads();
				t.setColorOpaque_I(shapecolours[sym.get(p).val2]);
				t.setNormal(-1, 0, 0);
				t.setBrightness(b);
				t.addVertexWithUV(-.1, coord[n-1][p][3][1], coord[n-1][p][1][0], shapeuvs[s][0][1], shapeuvs[s][1][1]);
				t.addVertexWithUV(-.1, coord[n-1][p][0][1], coord[n-1][p][2][0], shapeuvs[s][0][1], shapeuvs[s][1][0]);
				t.addVertexWithUV(-.1, coord[n-1][p][1][1], coord[n-1][p][3][0], shapeuvs[s][0][0], shapeuvs[s][1][0]);
				t.addVertexWithUV(-.1, coord[n-1][p][2][1], coord[n-1][p][0][0], shapeuvs[s][0][0], shapeuvs[s][1][1]);
				t.draw();
			}
		}
		
		// positive z
		if (block.shouldSideBeRendered(world, digsite.xCoord, digsite.yCoord, digsite.zCoord+1, 3))
		{
			b = block.getMixedBrightnessForBlock(world, digsite.xCoord, digsite.yCoord, digsite.zCoord+1);
			sym = puzzle.options.get(3).symbols;
			n = sym.size();
			
			for(int p = 0; p<n; p++) {
				s = sym.get(p).val1;
				t.startDrawingQuads();
				t.setColorOpaque_I(shapecolours[sym.get(p).val2]);
				t.setNormal(0, 0, 1);
				t.setBrightness(b);
				t.addVertexWithUV(coord[n-1][p][0][1], coord[n-1][p][0][0], 1.1, shapeuvs[s][0][1], shapeuvs[s][1][1]);
				t.addVertexWithUV(coord[n-1][p][1][1], coord[n-1][p][1][0], 1.1, shapeuvs[s][0][1], shapeuvs[s][1][0]);
				t.addVertexWithUV(coord[n-1][p][2][1], coord[n-1][p][2][0], 1.1, shapeuvs[s][0][0], shapeuvs[s][1][0]);
				t.addVertexWithUV(coord[n-1][p][3][1], coord[n-1][p][3][0], 1.1, shapeuvs[s][0][0], shapeuvs[s][1][1]);
				t.draw();
			}
		}
		
		// negetive z
		if (block.shouldSideBeRendered(world, digsite.xCoord, digsite.yCoord, digsite.zCoord-1, 2))
		{
			b = block.getMixedBrightnessForBlock(world, digsite.xCoord, digsite.yCoord, digsite.zCoord-1);
			sym = puzzle.options.get(2).symbols;
			n = sym.size();
			
			for(int p = 0; p<n; p++) {
				s = sym.get(p).val1;
				t.startDrawingQuads();
				t.setColorOpaque_I(shapecolours[sym.get(p).val2]);
				t.setNormal(0, 0, -1);
				t.setBrightness(b);
				t.addVertexWithUV(coord[n-1][p][2][1], coord[n-1][p][0][0], -.1, shapeuvs[s][0][1], shapeuvs[s][1][1]);
				t.addVertexWithUV(coord[n-1][p][3][1], coord[n-1][p][1][0], -.1, shapeuvs[s][0][1], shapeuvs[s][1][0]);
				t.addVertexWithUV(coord[n-1][p][0][1], coord[n-1][p][2][0], -.1, shapeuvs[s][0][0], shapeuvs[s][1][0]);
				t.addVertexWithUV(coord[n-1][p][1][1], coord[n-1][p][3][0], -.1, shapeuvs[s][0][0], shapeuvs[s][1][1]);
				t.draw();
			}
		}
		
		// y stuff is a little different because rotation
		int facing = 0;
		if (mc.thePlayer != null) {
			facing = 3 - (MathHelper.floor_double((double)(mc.thePlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3);
		}
		int[] pos = {0,1,2,3};
		
		pos = new int[]{pos[(facing+0)%4], pos[(facing+1)%4], pos[(facing+2)%4], pos[(facing+3)%4]};
		
		// positive y
		if (block.shouldSideBeRendered(world, digsite.xCoord, digsite.yCoord+1, digsite.zCoord, 1))
		{
			b = block.getMixedBrightnessForBlock(digsite.getWorldObj(), digsite.xCoord, digsite.yCoord+1, digsite.zCoord);
			sym = puzzle.options.get(1).symbols;
			n = sym.size();
			
			for(int p = 0; p<n; p++) {
				s = sym.get(p).val1;
				t.startDrawingQuads();
				t.setColorOpaque_I(shapecolours[sym.get(p).val2]);
				t.setNormal(0, 1, 0);
				t.setBrightness(b);
				t.addVertexWithUV(coord[n-1][p][pos[0]][0], 1.1, coord[n-1][p][pos[0]][1], shapeuvs[s][0][1], shapeuvs[s][1][1]);
				t.addVertexWithUV(coord[n-1][p][pos[1]][0], 1.1, coord[n-1][p][pos[1]][1], shapeuvs[s][0][1], shapeuvs[s][1][0]);
				t.addVertexWithUV(coord[n-1][p][pos[2]][0], 1.1, coord[n-1][p][pos[2]][1], shapeuvs[s][0][0], shapeuvs[s][1][0]);
				t.addVertexWithUV(coord[n-1][p][pos[3]][0], 1.1, coord[n-1][p][pos[3]][1], shapeuvs[s][0][0], shapeuvs[s][1][1]);
				t.draw();
			}
		}
		
		// negetive y
		if (block.shouldSideBeRendered(world, digsite.xCoord, digsite.yCoord-1, digsite.zCoord, 0))
		{
			b = block.getMixedBrightnessForBlock(digsite.getWorldObj(), digsite.xCoord, digsite.yCoord-1, digsite.zCoord);
			sym = puzzle.options.get(0).symbols;
			n = sym.size();
			
			for(int p = 0; p<n; p++) {
				s = sym.get(p).val1;
				t.startDrawingQuads();
				t.setColorOpaque_I(shapecolours[sym.get(p).val2]);
				t.setNormal(0, -1, 0);
				t.setBrightness(b);
				t.addVertexWithUV(coord[n-1][p][pos[3]][0], -.1, coord[n-1][p][pos[3]][1], shapeuvs[s][0][0], shapeuvs[s][1][0]);
				t.addVertexWithUV(coord[n-1][p][pos[2]][0], -.1, coord[n-1][p][pos[2]][1], shapeuvs[s][0][0], shapeuvs[s][1][1]);
				t.addVertexWithUV(coord[n-1][p][pos[1]][0], -.1, coord[n-1][p][pos[1]][1], shapeuvs[s][0][1], shapeuvs[s][1][1]);
				t.addVertexWithUV(coord[n-1][p][pos[0]][0], -.1, coord[n-1][p][pos[0]][1], shapeuvs[s][0][1], shapeuvs[s][1][0]);
				t.draw();
			}
		}
		
		GL11.glEnable(GL11.GL_CULL_FACE);
		
		//Physis.logger.info("rendering: "+digsite);
		//Physis.logger.info("render at "+x+","+y+","+z);
		
		GL11.glPopMatrix();
	}


}

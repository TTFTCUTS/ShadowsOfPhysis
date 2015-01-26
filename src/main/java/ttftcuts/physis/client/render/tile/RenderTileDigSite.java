package ttftcuts.physis.client.render.tile;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;

import ttftcuts.physis.Physis;
import ttftcuts.physis.api.item.ITrowel;
import ttftcuts.physis.common.PhysisBlocks;
import ttftcuts.physis.common.block.tile.TileEntityDigSite;

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

	private static final ResourceLocation texture = new ResourceLocation(Physis.MOD_ID+":textures/items/journal.png");
	
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
		
		int b = 0;
		int s = 0;
		
		// positive x
		if (block.shouldSideBeRendered(world, digsite.xCoord+1, digsite.yCoord, digsite.zCoord, 5))
		{
			b = block.getMixedBrightnessForBlock(world, digsite.xCoord+1, digsite.yCoord, digsite.zCoord);
			s = 2;
			
			for(int p = 0; p<s; p++) {
				t.startDrawingQuads();
				t.setNormal(1, 0, 0);
				t.setBrightness(b);
				t.addVertexWithUV(1.1, coord[s-1][p][3][1], coord[s-1][p][3][0], 1, 1);
				t.addVertexWithUV(1.1, coord[s-1][p][0][1], coord[s-1][p][0][0], 1, 0);
				t.addVertexWithUV(1.1, coord[s-1][p][1][1], coord[s-1][p][1][0], 0, 0);
				t.addVertexWithUV(1.1, coord[s-1][p][2][1], coord[s-1][p][2][0], 0, 1);
				t.draw();
			}
		}
		
		// negetive x
		if (block.shouldSideBeRendered(world, digsite.xCoord-1, digsite.yCoord, digsite.zCoord, 4))
		{
			b = block.getMixedBrightnessForBlock(world, digsite.xCoord-1, digsite.yCoord, digsite.zCoord);
			s = 3;
			
			for(int p = 0; p<s; p++) {
				t.startDrawingQuads();
				t.setNormal(-1, 0, 0);
				t.setBrightness(b);
				t.addVertexWithUV(-.1, coord[s-1][p][3][1], coord[s-1][p][1][0], 1, 1);
				t.addVertexWithUV(-.1, coord[s-1][p][0][1], coord[s-1][p][2][0], 1, 0);
				t.addVertexWithUV(-.1, coord[s-1][p][1][1], coord[s-1][p][3][0], 0, 0);
				t.addVertexWithUV(-.1, coord[s-1][p][2][1], coord[s-1][p][0][0], 0, 1);
				t.draw();
			}
		}
		
		// positive z
		if (block.shouldSideBeRendered(world, digsite.xCoord, digsite.yCoord, digsite.zCoord+1, 3))
		{
			b = block.getMixedBrightnessForBlock(world, digsite.xCoord, digsite.yCoord, digsite.zCoord+1);
			s = 2;
			
			for(int p = 0; p<s; p++) {
				t.startDrawingQuads();
				t.setNormal(0, 0, 1);
				t.setBrightness(b);
				t.addVertexWithUV(coord[s-1][p][0][1], coord[s-1][p][0][0], 1.1, 1, 1);
				t.addVertexWithUV(coord[s-1][p][1][1], coord[s-1][p][1][0], 1.1, 1, 0);
				t.addVertexWithUV(coord[s-1][p][2][1], coord[s-1][p][2][0], 1.1, 0, 0);
				t.addVertexWithUV(coord[s-1][p][3][1], coord[s-1][p][3][0], 1.1, 0, 1);
				t.draw();
			}
		}
		
		// negetive z
		if (block.shouldSideBeRendered(world, digsite.xCoord, digsite.yCoord, digsite.zCoord-1, 2))
		{
			b = block.getMixedBrightnessForBlock(world, digsite.xCoord, digsite.yCoord, digsite.zCoord-1);
			s = 3;
			
			for(int p = 0; p<s; p++) {
				t.startDrawingQuads();
				t.setNormal(0, 0, -1);
				t.setBrightness(b);
				t.addVertexWithUV(coord[s-1][p][2][1], coord[s-1][p][0][0], -.1, 1, 1);
				t.addVertexWithUV(coord[s-1][p][3][1], coord[s-1][p][1][0], -.1, 1, 0);
				t.addVertexWithUV(coord[s-1][p][0][1], coord[s-1][p][2][0], -.1, 0, 0);
				t.addVertexWithUV(coord[s-1][p][1][1], coord[s-1][p][3][0], -.1, 0, 1);
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
			s = 3;
			
			for(int p = 0; p<s; p++) {
				t.startDrawingQuads();
				t.setNormal(0, 1, 0);
				t.setBrightness(b);
				
				t.addVertexWithUV(coord[s-1][p][pos[0]][0], 1.1, coord[s-1][p][pos[0]][1], 1, 1);
				t.addVertexWithUV(coord[s-1][p][pos[1]][0], 1.1, coord[s-1][p][pos[1]][1], 1, 0);
				t.addVertexWithUV(coord[s-1][p][pos[2]][0], 1.1, coord[s-1][p][pos[2]][1], 0, 0);
				t.addVertexWithUV(coord[s-1][p][pos[3]][0], 1.1, coord[s-1][p][pos[3]][1], 0, 1);
				t.draw();
			}
		}
		
		// negetive y
		if (block.shouldSideBeRendered(world, digsite.xCoord, digsite.yCoord-1, digsite.zCoord, 0))
		{
			b = block.getMixedBrightnessForBlock(digsite.getWorldObj(), digsite.xCoord, digsite.yCoord-1, digsite.zCoord);
			s = 3;
			
			for(int p = 0; p<s; p++) {
				t.startDrawingQuads();
				t.setNormal(0, -1, 0);
				t.setBrightness(b);
		
				t.addVertexWithUV(coord[s-1][p][pos[3]][0], -.1, coord[s-1][p][pos[3]][1], 0, 0);
				t.addVertexWithUV(coord[s-1][p][pos[2]][0], -.1, coord[s-1][p][pos[2]][1], 0, 1);
				t.addVertexWithUV(coord[s-1][p][pos[1]][0], -.1, coord[s-1][p][pos[1]][1], 1, 1);
				t.addVertexWithUV(coord[s-1][p][pos[0]][0], -.1, coord[s-1][p][pos[0]][1], 1, 0);
				t.draw();
			}
		}
		
		GL11.glEnable(GL11.GL_CULL_FACE);
		
		//Physis.logger.info("rendering: "+digsite);
		//Physis.logger.info("render at "+x+","+y+","+z);
		
		GL11.glPopMatrix();
	}


}

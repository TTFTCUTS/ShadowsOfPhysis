package ttftcuts.physis.common.block.tile;


import java.util.ArrayList;
import java.util.List;

import ttftcuts.physis.Physis;
import ttftcuts.physis.client.texture.DigStripTexture;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class TileEntityDigSite extends TileEntityPhysis {
	
	public static final String LEVELTAG = "physisdiglevel";
	public static final String LAYERTAG = "physisdiglayer";
	public static final String LAYERLISTTAG = "physisdiglayerlist";
	
	// this one isn't saved, used for the hacky rendering
	public int renderlayer = 0;
	
	public int level = 0;
	public int currentlayer = 0;
	public int numlayers = 0;
	
	public List<DigSiteLayer> layerlist = new ArrayList<DigSiteLayer>();
	public List<DigSiteRenderLayer> renderdata = new ArrayList<DigSiteRenderLayer>();
	
	public void onPlaced(int level) {
		this.level = level;
		this.currentlayer = 0;
		
		this.buildLayerList();
		this.buildRenderData();
		
		this.markTileForUpdate();
	}
	
	private void buildLayerList() {
		this.layerlist.clear();
		
		for (int i=0; i<8; i++) {
			this.layerlist.add( new DigSiteLayer() );
		}
		
		this.numlayers = this.layerlist.size();
	}
	
	private void buildRenderData() {
		
	}
	
	public int getDigFrame() {
		double progress = currentlayer / (double)(numlayers-1);
	
		return Math.max(0, Math.min(DigStripTexture.numFrames-1, (int) Math.floor(progress * (DigStripTexture.numFrames-1))));
	}
	
	public boolean onActivation(World world, EntityPlayer player, int side) {
		Physis.logger.info("CLICK");
		if (this.currentlayer + 1 >= this.numlayers) {
			// destroy! dispense loot!
			
		} else {
			Physis.logger.info("++");
			this.currentlayer++;
			
			this.buildRenderData();
			
			this.markTileForUpdate();
			return true;
		}
		return false;
	}
	
	@Override
	public void writeCustomNBT(NBTTagCompound tag) {
		super.writeCustomNBT(tag);
		
		tag.setInteger(LEVELTAG, this.level);
		tag.setInteger(LAYERTAG, this.currentlayer);
		
		NBTTagCompound layerdata = new NBTTagCompound();
		for (int i=0; i<numlayers; i++) {
			layerdata.setTag("layer"+i, layerlist.get(i).writeToNBT());
		}
		layerdata.setInteger("numlayers", numlayers);
		tag.setTag(LAYERLISTTAG, layerdata);
	}

	@Override
	public void readCustomNBT(NBTTagCompound tag) {
		super.readCustomNBT(tag);

		this.level = tag.getInteger(LEVELTAG);
		this.currentlayer = tag.getInteger(LAYERTAG);
		
		this.layerlist.clear();
		NBTTagCompound layerdata = tag.getCompoundTag(LAYERLISTTAG);
		this.numlayers = layerdata.getInteger("numlayers");
		
		for (int i=0; i<numlayers; i++) {
			this.layerlist.add(new DigSiteLayer(layerdata.getCompoundTag("layer"+i)));
		}
		
		this.buildRenderData();
		this.markTileForUpdate();
	}
}
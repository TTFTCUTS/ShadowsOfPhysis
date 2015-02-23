package ttftcuts.physis.common.block.tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ttftcuts.physis.Physis;
import ttftcuts.physis.api.item.ITrowel;
import ttftcuts.physis.client.texture.DigStripTexture;
import ttftcuts.physis.common.artifact.LootSystem;
import ttftcuts.physis.common.helper.EffectHelper;
import ttftcuts.physis.common.item.ItemTrowel;
import ttftcuts.physis.puzzle.oddoneout.OddOneOutPuzzle;
import ttftcuts.physis.utils.TileUtilities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class TileEntityDigSite extends TileEntityPhysis {
	
	public static final String LEVELTAG = "physisdiglevel";
	public static final String LAYERTAG = "physisdiglayer";
	public static final String LAYERLISTTAG = "physisdiglayerlist";
	public static final String SEEDTAG = "physisoooseed";
	public static final String MISTAKESTAG = "physisdigmistakes";
	
	// this one isn't saved, used for the hacky rendering
	public int renderlayer = 0;
	
	public int level = 0;
	public int currentlayer = 0;
	public int numlayers = 0;
	
	public int mistakes = 0;
	
	public boolean loadRequestPuzzle = true;
	public int requestSeed = 0;
	
	public List<DigSiteLayer> layerlist = new ArrayList<DigSiteLayer>();
	public List<DigSiteRenderLayer> renderdata = new ArrayList<DigSiteRenderLayer>();
	
	public static Random digSiteRandom = new Random();
	
	private static final int[] layersPerLevel = {3,4,4,5,5,6,7,8,9,10};
	
	public void onPlaced(int level) {
		this.level = level;
		this.currentlayer = 0;
		this.requestSeed = digSiteRandom.nextInt();
		
		this.buildLayerList(level);
		this.buildRenderData();
		
		this.markTileForUpdate();
	}
	
	private void buildLayerList(int level) {
		this.layerlist.clear();

		for (int i=0; i<layersPerLevel[level]; i++) {
			this.layerlist.add( new DigSiteLayer() );
		}
		
		this.numlayers = this.layerlist.size();
	}
	
	private void buildRenderData() {
		this.renderdata.clear();
		this.layerlist.get(this.currentlayer).buildRenderData(this.renderdata, this);
		this.markTileForUpdate();
	}
	
	public int getDigFrame() {
		double progress = currentlayer / (double)(numlayers-1);
	
		return Math.max(0, Math.min(DigStripTexture.numFrames-1, (int) Math.floor(progress * (DigStripTexture.numFrames-1))));
	}
	
	public boolean onActivation(World world, EntityPlayer player, int side) {
		ItemStack held = player.getHeldItem();
		if (!(held.getItem() instanceof ITrowel)) {
			return false;
		}		
		ITrowel trowel = (ITrowel)(held.getItem());
		
		// Creative override
		if(ItemTrowel.isCreative(held)) {
			this.dropLoot(world, player);
			return true;
		}
		
		DigSiteLayer layer = this.layerlist.get(this.currentlayer);
		if (!layer.built) { 
			Physis.oooBuilder.requestPuzzle(this.level, this, this.currentlayer, digSiteRandom.nextInt());
			return false; 
		}
		
		OddOneOutPuzzle puzzle = this.layerlist.get(this.currentlayer).puzzle;
		if (puzzle != null) {
			if (side == puzzle.solution) {
				if (this.currentlayer + 1 >= this.numlayers) {
					// destroy! dispense loot!
					this.dropLoot(world, player);

				} else {
					//Physis.logger.info("++");
					this.currentlayer++;
					
					EffectHelper.doBlockBreakEffect(world, player, xCoord, yCoord, zCoord);
					
					loadRequestPuzzle = false;
					this.requestSeed = this.worldObj.rand.nextInt();
					Physis.oooBuilder.requestPuzzle(this.level, this, this.currentlayer, requestSeed);
					
					this.buildRenderData();
					
					this.markTileForUpdate();
				}
				trowel.onUseTrowel(held, player, true);
			} else {
				if (this.level > 0 && world.rand.nextDouble() < this.getBreakChance()) {
					this.level--;
					// STUFF BREAK NOISE
				} else {
					// fail noise
				}
				this.mistakes++;
				this.markTileForUpdate();
				
				trowel.onUseTrowel(held, player, false);
			}
		}
		return true;
	}
	
	private double getBreakChance() {
		switch(this.mistakes) {
		case 0:
		case 1:
			return 0.0;
		case 2:
			return 0.2;
		case 3:
			return 0.25;
		case 4:
			return 1.0/3.0;
		default:
			return 0.5;
		}
	}
	
	private void dropLoot(World world, EntityPlayer player) {
		EffectHelper.doBlockBreakEffect(world, player, xCoord, yCoord, zCoord);
		
		List<ItemStack> loot = LootSystem.getDigSiteLoot(digSiteRandom, this.level);
		TileUtilities.dropItemsInWorld(world, loot, xCoord, yCoord, zCoord);

		world.setBlockToAir(xCoord, yCoord, zCoord);
		this.invalidate();
		this.markTileForUpdate();
	}
	
	@Override
	public void updateEntity() {
		if (loadRequestPuzzle && layerlist.size() > 0 && !layerlist.get(currentlayer).built && this.getWorldObj() != null) {
			Physis.oooBuilder.requestPuzzle(this.level, this, this.currentlayer, requestSeed);
		}
	}
	
	@Override
	public void writeCustomNBT(NBTTagCompound tag) {
		super.writeCustomNBT(tag);
		
		tag.setInteger(LEVELTAG, this.level);
		tag.setInteger(LAYERTAG, this.currentlayer);
		tag.setInteger(SEEDTAG, this.requestSeed);
		tag.setInteger(MISTAKESTAG, this.mistakes);
		
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
		this.requestSeed = tag.getInteger(SEEDTAG);
		this.mistakes = tag.getInteger(MISTAKESTAG);
		
		this.layerlist.clear();
		NBTTagCompound layerdata = tag.getCompoundTag(LAYERLISTTAG);
		this.numlayers = layerdata.getInteger("numlayers");
		
		for (int i=0; i<numlayers; i++) {
			this.layerlist.add(new DigSiteLayer(layerdata.getCompoundTag("layer"+i)));
		}
		
		this.buildRenderData();
		this.markTileForUpdate();
	}
	
	public void setLayerPuzzle(int layerid, OddOneOutPuzzle puzzle) {
		loadRequestPuzzle = false;
		if (layerid < this.numlayers) {
			this.layerlist.get(layerid).setPuzzle(puzzle);
		}
		if (layerid == this.currentlayer) {
			this.buildRenderData();
		}
	}
}

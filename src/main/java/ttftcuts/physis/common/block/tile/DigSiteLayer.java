package ttftcuts.physis.common.block.tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ttftcuts.physis.Physis;
import ttftcuts.physis.puzzle.oddoneout.OddOneOutOption;
import ttftcuts.physis.puzzle.oddoneout.OddOneOutPuzzle;
import ttftcuts.physis.utils.TPair;
import net.minecraft.nbt.NBTTagCompound;

public class DigSiteLayer {
	private static final String BUILTTAG = "built";
	private static final String PUZZLETAG = "puzzle";
	
	public boolean built = false;
	
	public OddOneOutPuzzle puzzle;
	
	public DigSiteLayer() {
		
	}
	
	public DigSiteLayer(NBTTagCompound tag) {
		this();
		this.readFromNBT(tag);
	}
	
	public void buildRenderData(List<DigSiteRenderLayer> renderlayers, TileEntityDigSite site) {
		if (this.puzzle == null) { return; }
		
		long seed = site.xCoord * 11092001
				+ site.yCoord * 12121787
				+ site.zCoord * 23456789
				+ site.currentlayer * 56598313;
		
		Random rand = new Random(seed);
		
		for(int i=0; i<6; i++) {
			OddOneOutOption o = this.puzzle.options.get(i);
			
			symbolLoop:
			for(int j=0; j<o.symbols.size(); j++) {
				TPair<Integer> s = o.symbols.get(j);
				
				for(int l=0; l<renderlayers.size(); l++) {
					DigSiteRenderLayer layer = renderlayers.get(l);
					if (layer.colour == s.val1) {
						if (layer.shapes[i] == -1) {
							layer.setSymbol(i, s.val2, getSlotPos(rand, renderlayers, i));
							continue symbolLoop;
						}
					}
				}
				
				DigSiteRenderLayer layer = new DigSiteRenderLayer(s.val1);
				layer.setSymbol(i, s.val2, getSlotPos(rand, renderlayers, i));
				renderlayers.add(layer);
			}
		}
		
		Physis.logger.info(renderlayers);
	}
	
	private int getSlotPos(Random rand, List<DigSiteRenderLayer> data, int slot) {
		List<Integer> whitelist = new ArrayList<Integer>();
		whitelist.add(0);
		whitelist.add(1);
		whitelist.add(2);
		whitelist.add(3);
		whitelist.add(4);
		whitelist.add(5);
		whitelist.add(6);
		whitelist.add(7);
		
		for (int i=0; i<data.size(); i++) {
			DigSiteRenderLayer l = data.get(i);
			if (l.shapes[slot] != -1) {
				whitelist.remove(l.positions[slot]);
			}
		}
		
		if (whitelist.size() == 0) {
			return 0;
		}
		
		return whitelist.get(rand.nextInt(whitelist.size()));
	}
	
	public NBTTagCompound writeToNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		
		tag.setBoolean(BUILTTAG, built);
		
		if (this.puzzle != null) {
			//Physis.logger.info("Saving puzzle: "+this.puzzle);
			
			NBTTagCompound ptag = new NBTTagCompound();
			
			ptag.setInteger("answer", this.puzzle.solution);
			
			for (int i=0; i<6; i++) {
				NBTTagCompound otag = new NBTTagCompound();
				OddOneOutOption option = this.puzzle.options.get(i);
				
				int symbols = option.symbols.size();
				otag.setInteger("symbols", symbols);
				for (int j=0; j<symbols; j++) {
					NBTTagCompound stag = new NBTTagCompound();
					TPair<Integer> symbol = option.symbols.get(j);
					
					//Physis.logger.info(symbol);
					
					stag.setInteger("colour", symbol.val1);
					stag.setInteger("shape", symbol.val2);
					
					otag.setTag("symbol"+j, stag);
				}
				ptag.setTag("option"+i, otag);
			}
			tag.setTag(PUZZLETAG, ptag);
		}
		
		return tag;
	}
	
	public void readFromNBT(NBTTagCompound tag) {
		if (tag.hasKey(BUILTTAG)) {
			this.built = tag.getBoolean(BUILTTAG);
		}
		
		if (tag.hasKey(PUZZLETAG)) {
			NBTTagCompound ptag = tag.getCompoundTag(PUZZLETAG);
			int answer = ptag.getInteger("answer");
			
			List<OddOneOutOption> options = new ArrayList<OddOneOutOption>();
			
			for (int i=0; i<6; i++) {
				NBTTagCompound otag = ptag.getCompoundTag("option"+i);
				int symbols = otag.getInteger("symbols");
				
				OddOneOutOption option = new OddOneOutOption();
				
				for (int j=0; j<symbols; j++) {
					NBTTagCompound stag = otag.getCompoundTag("symbol"+j);
					int col = stag.getInteger("colour");
					int shape = stag.getInteger("shape");
					
					option.addSymbol(new TPair<Integer>(col, shape));
				}
				
				options.add(option);
			}
			
			this.puzzle = new OddOneOutPuzzle(options, answer);
			
			//Physis.logger.info("Loaded puzzle: "+this.puzzle);
		}
	}
	
	public void setPuzzle(OddOneOutPuzzle puzzle) {
		Physis.logger.info("Layer receieved puzzle: "+puzzle);
		this.puzzle = puzzle;
		this.built = true;
	}
}

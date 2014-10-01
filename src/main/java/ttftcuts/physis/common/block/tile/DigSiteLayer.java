package ttftcuts.physis.common.block.tile;

import ttftcuts.physis.Physis;
import ttftcuts.physis.puzzle.oddoneout.OddOneOutPuzzle;
import net.minecraft.nbt.NBTTagCompound;

public class DigSiteLayer {
	private static final String BUILTTAG = "built";
	
	public boolean built = false;
	
	public DigSiteLayer() {
		
	}
	
	public DigSiteLayer(NBTTagCompound tag) {
		this();
		this.readFromNBT(tag);
	}
	
	public NBTTagCompound writeToNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		
		tag.setBoolean(BUILTTAG, built);
		
		return tag;
	}
	
	public void readFromNBT(NBTTagCompound tag) {
		if (tag.hasKey(BUILTTAG)) {
			this.built = tag.getBoolean(BUILTTAG);
		}
	}
	
	public void setPuzzle(OddOneOutPuzzle puzzle) {
		Physis.logger.info("Layer receieved puzzle: "+puzzle);
	}
}

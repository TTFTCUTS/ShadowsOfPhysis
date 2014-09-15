package ttftcuts.physis.common.block.tile;

import net.minecraft.nbt.NBTTagCompound;

public class DigSiteLayer {

	public DigSiteLayer() {
		
	}
	
	public DigSiteLayer(NBTTagCompound tag) {
		this();
		this.readFromNBT(tag);
	}
	
	public NBTTagCompound writeToNBT() {
		return new NBTTagCompound();
	}
	
	public void readFromNBT(NBTTagCompound tag) {
		
	}
}

package ttftcuts.physis.common.block.tile;

import ttftcuts.physis.common.block.BlockDigSite;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityDigSite extends TileEntityPhysis {
	
	public int level = 0;
	
	@Override
	public void writeCustomNBT(NBTTagCompound tag) {
		super.writeCustomNBT(tag);
		
		tag.setInteger(BlockDigSite.LEVELTAG, this.level);
	}

	@Override
	public void readCustomNBT(NBTTagCompound tag) {
		super.readCustomNBT(tag);

		this.level = tag.getInteger(BlockDigSite.LEVELTAG);
	}
}

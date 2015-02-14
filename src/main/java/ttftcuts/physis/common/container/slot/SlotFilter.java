package ttftcuts.physis.common.container.slot;

import ttftcuts.physis.api.artifact.ISocketable;
import ttftcuts.physis.common.artifact.PhysisArtifacts;
import ttftcuts.physis.common.block.tile.TileEntitySocketTable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public enum SlotFilter {
	
	SOCKETABLE {
		@Override
		public boolean isValidStack(ItemStack stack) {
			return stack.getItem() instanceof ISocketable;
		}
	},
	
	SOCKETED {
		@Override
		public boolean isValidStack(ItemStack stack)
	    {
			NBTTagCompound[] sockets = PhysisArtifacts.getSocketablesFromStack(stack);
			
			if (sockets != null && sockets.length > 0) {
				return true;
			}
			
	        return false;
	    }
	},
	
	SOCKETREAGENT {
		@Override
		public boolean isValidStack(ItemStack stack) {
			return TileEntitySocketTable.checkReagentValidity(stack);
		}
	};
	
	SlotFilter() {}
		
	public boolean isValidStack(ItemStack stack) { return true; }

}

package ttftcuts.physis.common.container.slot;

import ttftcuts.physis.api.artifact.ISocketable;
import ttftcuts.physis.common.artifact.PhysisArtifacts;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class SlotFilter {
	
	public static SlotFilter SOCKETABLE = new SlotFilter() {
		@Override
		public boolean isValidStack(ItemStack stack) {
			return stack.getItem() instanceof ISocketable;
		}
	};
	
	public static SlotFilter SOCKETED = new SlotFilter() {
		@Override
		public boolean isValidStack(ItemStack stack)
	    {
			NBTTagCompound[] sockets = PhysisArtifacts.getSocketablesFromStack(stack);
			
			if (sockets != null && sockets.length > 0) {
				return true;
			}
			
	        return false;
	    }
	};
	
	public SlotFilter() {}
		
	public boolean isValidStack(ItemStack stack) { return true; }

}

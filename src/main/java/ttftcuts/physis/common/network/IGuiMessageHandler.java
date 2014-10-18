package ttftcuts.physis.common.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public interface IGuiMessageHandler {

	public void processMessage(EntityPlayer player, NBTTagCompound tag);
}

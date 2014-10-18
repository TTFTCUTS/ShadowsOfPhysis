package ttftcuts.physis.common.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class PacketGuiMessage extends PacketHandler {

	@Override
	public void handle(ByteBuf data, EntityPlayer player) {
		if (player.openContainer == null) return;
        int windowId = data.readInt();
        if (player.openContainer.windowId != windowId) return;
        if (!player.openContainer.isPlayerNotUsingContainer(player)) return;
        if (player.openContainer instanceof IGuiMessageHandler) {
            NBTTagCompound nbt = ByteBufUtils.readTag(data);
            ((IGuiMessageHandler) player.openContainer).processMessage(player, nbt);
        }
	}

	public static FMLProxyPacket createPacket(int windowId, NBTTagCompound tag) {
		ByteBuf data = PacketHandler.createDataBuffer(PacketGuiMessage.class);
				
		data.writeInt(windowId);
		ByteBufUtils.writeTag(data, tag);
		
		return buildPacket(data);
	}
}

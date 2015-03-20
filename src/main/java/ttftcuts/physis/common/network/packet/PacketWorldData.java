package ttftcuts.physis.common.network.packet;

import ttftcuts.physis.common.file.PhysisWorldSavedData;
import ttftcuts.physis.common.network.PacketHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class PacketWorldData extends PacketHandler {

	@Override
	public void handle(ByteBuf data, EntityPlayer player) {
		if (player.worldObj.isRemote) {
			NBTTagCompound tag = ByteBufUtils.readTag(data);
			
			NBTTagCompound playerdata = tag.getCompoundTag("p");
			NBTTagCompound worlddata = tag.getCompoundTag("w");
			
			PhysisWorldSavedData.clientPlayerData = playerdata;
			PhysisWorldSavedData.clientWorldData = worlddata;
			PhysisWorldSavedData.doCallbacksPost();
		}
	}

	public static FMLProxyPacket createPacket(NBTTagCompound playerdata, NBTTagCompound worlddata) {
		ByteBuf data = PacketHandler.createDataBuffer(PacketWorldData.class);
		
		PhysisWorldSavedData.doCallbacksPre();
		
		NBTTagCompound tag = new NBTTagCompound();
		
		tag.setTag("p", playerdata);
		tag.setTag("w", worlddata);
		
		ByteBufUtils.writeTag(data, tag);
		
		return buildPacket(data);
	}
}

package ttftcuts.physis.common.network;

import ttftcuts.physis.common.file.ServerData;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class PacketWorldTime extends PacketHandler {

	@Override
	public void handle(ByteBuf data, EntityPlayer player) {
		if (player.worldObj.isRemote) {
			long time = data.readLong();
			
			ServerData.setTime(time, true);
		}
	}

	public static FMLProxyPacket createPacket(long time) {
		ByteBuf data = PacketHandler.createDataBuffer(PacketWorldTime.class);
				
		data.writeLong(time);
		
		return buildPacket(data);
	}
}

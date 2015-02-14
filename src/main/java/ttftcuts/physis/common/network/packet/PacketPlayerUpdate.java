package ttftcuts.physis.common.network.packet;

import ttftcuts.physis.common.network.PacketHandler;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class PacketPlayerUpdate extends PacketHandler {

	@Override
	public void handle(ByteBuf data, EntityPlayer player) {
		if (player.worldObj.isRemote) {
			UpdateType type = UpdateType.values()[data.readInt()];
			
			switch(type) {
			case PUNT:
				double x = data.readDouble();
				double y = data.readDouble();
				double z = data.readDouble();
				
				player.motionX += x;
				player.motionY += y;
				player.motionZ += z;
				
				break;
			case AIR:
				int air = player.getAir();
				int airchange = data.readByte();
				int newair = Math.max(0, Math.min(300, air + airchange));
				
				player.setAir(newair);
				
				break;
			}
			
		}
	}

	public static FMLProxyPacket createPuntPacket(double x, double y, double z) {
		ByteBuf data = PacketHandler.createDataBuffer(PacketPlayerUpdate.class);
		
		data.writeInt(UpdateType.PUNT.ordinal());
		
		data.writeDouble(x);
		data.writeDouble(y);
		data.writeDouble(z);
		
		return buildPacket(data);
	}
	
	public static FMLProxyPacket createAirPacket(int air) {
		ByteBuf data = PacketHandler.createDataBuffer(PacketPlayerUpdate.class);
		
		data.writeInt(UpdateType.AIR.ordinal());
		
		data.writeByte(air);
		
		return buildPacket(data);
	}
	
	public static enum UpdateType {
		PUNT,
		AIR
	}
}

package ttftcuts.physis.common.network.packet;

import ttftcuts.physis.common.network.PacketHandler;
import ttftcuts.physis.common.story.StoryEngine;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class PacketStorySeed extends PacketHandler {

	@Override
	public void handle(ByteBuf data, EntityPlayer player) {
		if (player.worldObj.isRemote) {
			long seed = data.readLong();
			
			StoryEngine.reload(seed, true);
		}
	}

	public static FMLProxyPacket createPacket(long seed) {
		ByteBuf data = PacketHandler.createDataBuffer(PacketStorySeed.class);
				
		data.writeLong(seed);
		
		return buildPacket(data);
	}
}

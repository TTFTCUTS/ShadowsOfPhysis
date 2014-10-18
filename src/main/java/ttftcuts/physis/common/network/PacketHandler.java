package ttftcuts.physis.common.network;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import net.minecraft.entity.player.EntityPlayer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public abstract class PacketHandler {

	public abstract void handle(ByteBuf data, EntityPlayer player);
	 
    public static ByteBuf createDataBuffer(Class<? extends PacketHandler> handlerclass) {
            ByteBuf data = Unpooled.buffer();
            data.writeByte(PhysisPacketHandler.getId(handlerclass));
            return data;
    }

    protected static FMLProxyPacket buildPacket(ByteBuf payload) {
            return new FMLProxyPacket(payload, PhysisPacketHandler.CHANNEL);
    }
}

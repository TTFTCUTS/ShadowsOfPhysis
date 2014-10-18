package ttftcuts.physis.common.network;

import io.netty.buffer.ByteBuf;

import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import ttftcuts.physis.Physis;

public class PhysisPacketHandler {
	public static final String CHANNEL = Physis.MOD_ID;
	public static FMLEventChannel bus;
	
	private static HashMap<Byte, PacketHandler> packethandlers = new HashMap<Byte, PacketHandler>();
    private static HashMap<Class<? extends PacketHandler>, Byte> idmap  = new HashMap<Class<? extends PacketHandler>, Byte>();

    public static void init() {
    	bus = NetworkRegistry.INSTANCE.newEventDrivenChannel(CHANNEL);
    	bus.register(new PhysisPacketHandler());
    }
    
    public static void registerPacketHandler(PacketHandler handler, int id) {
    	byte bid = (byte) id;
        if (packethandlers.get(bid) != null) { 
        	throw new RuntimeException("Multiple id registrations for packet type on " + CHANNEL + " channel"); 
        }
        packethandlers.put(bid, handler);
        idmap.put(handler.getClass(), bid);
    }

    public static byte getId(PacketHandler handler) {
        return getId(handler.getClass());
    }

    public static byte getId(Class<? extends PacketHandler> handlerclass) {
        if (!idmap.containsKey(handlerclass)) {
    		throw new RuntimeException("Attempted to get id for unregistered network message handler.");
    	}
        return idmap.get(handlerclass);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onPacketData(ClientCustomPacketEvent event) {
        FMLProxyPacket pkt = event.packet;

        onPacketData(event.manager, pkt, Minecraft.getMinecraft().thePlayer);
    }

    @SubscribeEvent
    public void onPacketData(ServerCustomPacketEvent event) {
        FMLProxyPacket pkt = event.packet;

        onPacketData(event.manager, pkt, ((NetHandlerPlayServer) event.handler).playerEntity);
    }

    public void onPacketData(NetworkManager manager, FMLProxyPacket packet, EntityPlayer player) {
        try {
            if (packet == null || packet.payload() == null) { throw new RuntimeException("Empty packet sent to " + CHANNEL + " channel"); }
            ByteBuf data = packet.payload();
            byte type = data.readByte();

            try {
                PacketHandler handler = packethandlers.get(type);
                if (handler == null) { throw new RuntimeException("Unrecognized packet sent to " + CHANNEL + " channel"); }
                handler.handle(data, player);
            } catch (Exception e) {
                Physis.logger.warn("PacketHandler: Failed to handle packet type " + type);
                Physis.logger.warn(e.toString());
                e.printStackTrace();
            }
        } catch (Exception e) {
        	Physis.logger.warn("PacketHandler: Failed to read packet");
        	Physis.logger.warn(e.toString());
            e.printStackTrace();
        }
    }
}

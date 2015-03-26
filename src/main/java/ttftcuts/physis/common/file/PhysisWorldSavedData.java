package ttftcuts.physis.common.file;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.relauncher.Side;

import ttftcuts.physis.Physis;
import ttftcuts.physis.common.network.PhysisPacketHandler;
import ttftcuts.physis.common.network.packet.PacketWorldData;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class PhysisWorldSavedData extends WorldSavedData {
	public static class WorldDataHandler {
        @SubscribeEvent
        public void onPlayerLogin(PlayerLoggedInEvent event) {
            instance.sendDataToPlayer(event.player);
        }
	}
	
	private static final String PLAYERDATATAG = "PlayerData";
	private static final String WORLDDATATAG = "WorldData";
	private static final String UUIDTAG1 = "UUIDMost";
	private static final String UUIDTAG2 = "UUIDLeast";
	
	public static NBTTagCompound clientWorldData = new NBTTagCompound();
	public static NBTTagCompound clientPlayerData = new NBTTagCompound();
	
	private Map<UUID, NBTTagCompound> playerData;
	private NBTTagCompound worldData;
	
	public static PhysisWorldSavedData instance;
	private static List<IDataCallback> callbacks = new ArrayList<IDataCallback>();
	
	public PhysisWorldSavedData(String discard) {
		this();
	}
	public PhysisWorldSavedData() {
		super(Physis.MOD_ID);
		playerData = new HashMap<UUID, NBTTagCompound>();
		worldData = new NBTTagCompound();
		
		instance = this;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		NBTTagList list = tag.getTagList(PLAYERDATATAG, 10);
		for (int i=0; i<list.tagCount(); i++) {
			NBTTagCompound data = list.getCompoundTagAt(i);
			UUID uuid = new UUID(data.getLong(UUIDTAG1), data.getLong(UUIDTAG2));
			playerData.put(uuid, data);
		}
		worldData = tag.getCompoundTag(WORLDDATATAG);
		//Physis.logger.info("Read world data from NBT");
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		NBTTagList list = new NBTTagList();
		for (Entry<UUID, NBTTagCompound> entry : playerData.entrySet()) {
			NBTTagCompound ptag = entry.getValue();
			ptag.setLong(UUIDTAG1, entry.getKey().getMostSignificantBits());
			ptag.setLong(UUIDTAG2, entry.getKey().getLeastSignificantBits());
			list.appendTag(ptag);
		}
		tag.setTag(PLAYERDATATAG, list);
		tag.setTag(WORLDDATATAG, worldData);
		//Physis.logger.info("Saved world data to NBT");
	}
	
	private static NBTTagCompound getWorldData() {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			return clientWorldData;
		}
		return instance.worldData;
	}
	
	private static NBTTagCompound getPlayerData(EntityPlayer player) {
		return getPlayerData(player.getUniqueID());
	}
	
	private static NBTTagCompound getPlayerData(UUID uuid) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			if (uuid == Minecraft.getMinecraft().thePlayer.getUniqueID()) {
				return clientPlayerData;
			}
			return null;
		}
		NBTTagCompound data = instance.playerData.get(uuid);
		if (data == null) {
			data = new NBTTagCompound();
			instance.playerData.put(uuid, data);
		}
		return data;
	}
	
	public static void load(World world) {
		instance = (PhysisWorldSavedData) world.loadItemData(PhysisWorldSavedData.class, Physis.MOD_ID);
		if (instance == null) {
			instance = new PhysisWorldSavedData();
			world.setItemData(Physis.MOD_ID, instance);
		}
		//Physis.logger.info("Loaded server data");
	}
	
	public static void registerCallback(IDataCallback cb) {
		if (!callbacks.contains(cb)) {
			callbacks.add(cb);
		}
	}
	
	public static void doCallbacksPre() {
		for (IDataCallback cb : callbacks) {
			cb.dataPacketSending();
		}
	}
	
	public static void doCallbacksPost() {
		for (IDataCallback cb : callbacks) {
			cb.dataPacketReceived();
		}
	}
	
	public static void safeMarkDirty() {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) { return; }
		instance.markDirty();
	}
	
	// network
	public void sendDataToPlayer(EntityPlayer player) {
		if (player instanceof EntityPlayerMP) {
			PhysisPacketHandler.bus.sendTo(PacketWorldData.createPacket(getPlayerData(player), worldData), (EntityPlayerMP)player);
		}
	}
	
	public static void safeSendDataToPlayer(EntityPlayer player) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) { return; }
		instance.sendDataToPlayer(player);
	}
	
	// data getting/setting
	
	public static void setWorldInt(String name, int value) {
		getWorldData().setInteger(name, value);
		safeMarkDirty();
	}
	public static int getWorldInt(String name) {
		return getWorldData().getInteger(name);
	}
	public static void setWorldTag(String name, NBTTagCompound tag) {
		getWorldData().setTag(name, tag);
		safeMarkDirty();
	}
	public static NBTTagCompound getWorldTag(String name) {
		return getWorldData().getCompoundTag(name);
	}
	
	public static void setWorldLong(String name, long value) {
		getWorldData().setLong(name, value);
		safeMarkDirty();
	}
	public static long getWorldLong(String name) {
		return getWorldData().getLong(name);
	}
	
	public static void setPlayerInt(EntityPlayer player, String name, int value) {
		NBTTagCompound p = getPlayerData(player);
		if (p != null) {
			p.setInteger(name, value);
			safeMarkDirty();
		}
	}
	public static int getPlayerInt(EntityPlayer player, String name) {
		NBTTagCompound p = getPlayerData(player);
		if (p != null) {
			return p.getInteger(name);
		}
		return 0;
	}
	public static void setPlayerTag(EntityPlayer player, String name, NBTTagCompound tag) {
		NBTTagCompound p = getPlayerData(player);
		if (p != null) {
			getPlayerData(player).setTag(name, tag);
			safeMarkDirty();
		}
	}
	public static NBTTagCompound getPlayerTag(EntityPlayer player, String name) {
		NBTTagCompound p = getPlayerData(player);
		if (p != null) {
			if (!p.hasKey(name)) {
				p.setTag(name, new NBTTagCompound());
			}
			return p.getCompoundTag(name);
		}
		return null;
	}
}

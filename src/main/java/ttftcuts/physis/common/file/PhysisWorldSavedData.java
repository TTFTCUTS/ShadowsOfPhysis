package ttftcuts.physis.common.file;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import ttftcuts.physis.Physis;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class PhysisWorldSavedData extends WorldSavedData {
	private static final String PLAYERDATATAG = "PlayerData";
	private static final String WORLDDATATAG = "WorldData";
	private static final String UUIDTAG1 = "UUIDMost";
	private static final String UUIDTAG2 = "UUIDLeast";
	
	private Map<UUID, NBTTagCompound> playerData;
	private NBTTagCompound worldData;
	
	public static PhysisWorldSavedData instance;
	
	public PhysisWorldSavedData(String discard) {
		this();
	}
	public PhysisWorldSavedData() {
		super(Physis.MOD_ID);
		playerData = new HashMap<UUID, NBTTagCompound>();
		
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
		Physis.logger.info("Read world data from NBT");
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
		Physis.logger.info("Saved world data to NBT");
	}
	
	private NBTTagCompound getPlayerData(EntityPlayer player) {
		return getPlayerData(player.getUniqueID());
	}
	
	private NBTTagCompound getPlayerData(UUID uuid) {
		NBTTagCompound data = playerData.get(uuid);
		if (data == null) {
			data = new NBTTagCompound();
			playerData.put(uuid, data);
		}
		return data;
	}
	
	public static void load(World world) {
		instance = (PhysisWorldSavedData) world.loadItemData(PhysisWorldSavedData.class, Physis.MOD_ID);
		if (instance == null) {
			instance = new PhysisWorldSavedData();
			world.setItemData(Physis.MOD_ID, instance);
		}
		Physis.logger.info("Loaded server data");
	}

	// data getting/setting
	
	public void setWorldInt(String name, int value) {
		worldData.setInteger(name, value);
		this.markDirty();
	}
	public int getWorldInt(String name) {
		return worldData.getInteger(name);
	}
	public void setWorldTag(String name, NBTTagCompound tag) {
		worldData.setTag(name, tag);
		this.markDirty();
	}
	public NBTTagCompound getWorldTag(String name) {
		return worldData.getCompoundTag(name);
	}
	
	public void setWorldLong(String name, long value) {
		worldData.setLong(name, value);
		this.markDirty();
	}
	public long getWorldLong(String name) {
		return worldData.getLong(name);
	}
	
	public void setPlayerInt(EntityPlayer player, String name, int value) {
		getPlayerData(player).setInteger(name, value);
		this.markDirty();
	}
	public int getPlayerInt(EntityPlayer player, String name) {
		return getPlayerData(player).getInteger(name);
	}
	public void setPlayerTag(EntityPlayer player, String name, NBTTagCompound tag) {
		getPlayerData(player).setTag(name, tag);
		this.markDirty();
	}
	public NBTTagCompound getPlayerTag(EntityPlayer player, String name) {
		return getPlayerData(player).getCompoundTag(name);
	}
}

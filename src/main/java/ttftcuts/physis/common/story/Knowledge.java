package ttftcuts.physis.common.story;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

import ttftcuts.physis.common.file.PhysisWorldSavedData;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class Knowledge {
	public static void init() {
		// Knowledge types and max levels
		
		registry.put("test", 10);
	}
	
	// ----------------
	private static final String KNOWLEDGETAG = "knowledge";
	private static Map<String, Integer> registry = new HashMap<String, Integer>();
	
	public static int get(EntityPlayer player, String name) {
		if (!registry.containsKey(name)) { return 0; }
		
		NBTTagCompound tag = PhysisWorldSavedData.getPlayerTag(player, KNOWLEDGETAG);
		return tag.getInteger(name);
	}
	
	public static int getMax(String name) {
		if (!registry.containsKey(name)) { return 0; }
		return registry.get(name);
	}
	
	public static boolean set(EntityPlayer player, String name, int value) {
		if (!registry.containsKey(name)) { return false; }
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) { return false; }
		
		int clamped = Math.max(0, Math.min(registry.get(name), value));
		
		int current = get(player, name);
		
		if (clamped != current) {
			NBTTagCompound tag = PhysisWorldSavedData.getPlayerTag(player, KNOWLEDGETAG);
			tag.setInteger(name, clamped);
			PhysisWorldSavedData.safeMarkDirty();
			PhysisWorldSavedData.safeSendDataToPlayer(player);
			
			return true;
		}
		return false;
	}
	
	public static boolean modify(EntityPlayer player, String name, int delta) {
		if (!registry.containsKey(name)) { return false; }
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) { return false; }
		
		return set(player, name, get(player, name) +delta);
	}
	
	public static void giveAll(EntityPlayer player) {
		for (Entry<String, Integer> entry : registry.entrySet()) {
			set(player, entry.getKey(), entry.getValue());
		}
	}
	
	public static void removeAll(EntityPlayer player) {
		for (Entry<String, Integer> entry : registry.entrySet()) {
			set(player, entry.getKey(), 0);
		}
	}
}

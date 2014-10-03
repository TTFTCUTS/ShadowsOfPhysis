package ttftcuts.physis.common.artifact;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.WeightedRandom;

import ttftcuts.physis.api.artifact.IArtifactEffect;
import ttftcuts.physis.api.artifact.IArtifactTrigger;

public final class PhysisArtifacts {

	private static PhysisArtifacts instance;
	
	public static final String ARTIFACTTAG = "physisArtifact";
	public static final String TRIGGERTAG = "trigger";
	public static final String EFFECTTAG = "effect";
	public static final String SOCKETEDTAG = "physisSocketed";
	public static final String SOCKETTAG = "socket";
	public static final String SOCKETCOUNTTAG = "count";
	public static final String SOCKETFIXED = "socketFixed";
	
	private static Map<String, WeightedTrigger> triggers = new HashMap<String, WeightedTrigger>();
	private static Map<String, WeightedEffect> effects = new HashMap<String, WeightedEffect>();

	public static void init() {
		instance = new PhysisArtifacts();
	}
	
	// ################### registration ###################
	
	public static boolean registerTrigger(String name, IArtifactTrigger trigger, int weight) {
		if (!triggers.containsKey(name)) {
			triggers.put(name, instance.new WeightedTrigger(weight, trigger));
			return true;
		}
		return false;
	}
	
	public static boolean registerEffect(String name, IArtifactEffect effect, int weight) {
		if (!effects.containsKey(name)) {
			effects.put(name, instance.new WeightedEffect(weight, effect));
			return true;
		}
		return false;
	}
	
	// ################### getters ###################
	
	public static IArtifactTrigger getTrigger(String name) {
		if (triggers.containsKey(name)) {
			return triggers.get(name).theTrigger;
		}
		return null;
	}
	
	public static IArtifactEffect getEffect(String name) {
		if (effects.containsKey(name)) {
			return effects.get(name).theEffect;
		}
		return null;
	}
	
	public static IArtifactTrigger getRandomTrigger(Random rand) {
		return ((WeightedTrigger)(WeightedRandom.getRandomItem(rand, triggers.entrySet()))).theTrigger;
	}
	
	public static IArtifactEffect getRandomEffect(Random rand) {
		return ((WeightedEffect)(WeightedRandom.getRandomItem(rand, effects.entrySet()))).theEffect;
	}
	
	// ################### ItemStack methods ###################
	
	public static NBTTagCompound[] getSocketablesFromStack(ItemStack stack) {
		if (stack.stackTagCompound != null) {
			if (stack.stackTagCompound.hasKey(SOCKETEDTAG)) {
				NBTTagCompound data = stack.stackTagCompound.getCompoundTag(SOCKETEDTAG);
				
				if (data.hasKey(SOCKETCOUNTTAG)) {
					int count = data.getInteger(SOCKETCOUNTTAG);
					
					NBTTagCompound[] socketables = new NBTTagCompound[count];
					
					for (int i=0; i<count; i++) {
						if (data.hasKey(SOCKETTAG+i)) {
							socketables[i] = data.getCompoundTag(SOCKETTAG+i);
						}
					}
					
					return socketables;
				}
			}
		}
		return null;
	}
	
	public static void writeSocketablesToStack(ItemStack stack, NBTTagCompound[] socketables) {
		if (stack.stackTagCompound == null) {
			stack.stackTagCompound = new NBTTagCompound();
		}
		
		NBTTagCompound data = new NBTTagCompound();
		
		data.setInteger(SOCKETCOUNTTAG, socketables.length);
		
		for (int i=0; i<socketables.length; i++) {
			if (socketables[i] != null) {
				data.setTag(SOCKETTAG+i, socketables[i]);
			}
		}
		
		stack.stackTagCompound.setTag(SOCKETEDTAG, data);
	}
	
	public static void writeSocketablesToStack(ItemStack stack, ItemStack[] socketables) {
		NBTTagCompound[] tags = new NBTTagCompound[socketables.length];
		
		for(int i=0; i<socketables.length; i++) {
			if (socketables[i] != null) {
				NBTTagCompound tag = new NBTTagCompound();
				socketables[i].writeToNBT(tag);
				tags[i] = tag;
			}
		}
		
		writeSocketablesToStack(stack, tags);
	}

	public static int getSocketCount(ItemStack stack) {
		if (stack.stackTagCompound != null) {
			if (stack.stackTagCompound.hasKey(SOCKETEDTAG)) {
				NBTTagCompound data = stack.stackTagCompound.getCompoundTag(SOCKETEDTAG);
				
				if (data.hasKey(SOCKETCOUNTTAG)) {
					return data.getInteger(SOCKETCOUNTTAG);
				}
			}
		}
		return 0;
	}
	
	public static IArtifactTrigger getTriggerFromSocketable(NBTTagCompound tag) {
		if (tag.hasKey("tag")) {
			return getTriggerFromNBT(tag.getCompoundTag("tag"));
		}
		return null;
	}
	
	public static IArtifactTrigger getTriggerFromSocketable(ItemStack stack) {
		if (stack.stackTagCompound != null) {
			return getTriggerFromNBT(stack.stackTagCompound);
		}
		return null;
	}
	
	private static IArtifactTrigger getTriggerFromNBT(NBTTagCompound tag) {
		if (tag.hasKey(ARTIFACTTAG)) {
			if (tag.hasKey(TRIGGERTAG) && tag.hasKey(EFFECTTAG)) {
				return triggers.get(tag.getString(TRIGGERTAG)).theTrigger;
			}
		}
		return null;
	}
	
	public static IArtifactEffect getEffectFromSocketable(NBTTagCompound tag) {
		if (tag.hasKey("tag")) {
			return getEffectFromNBT(tag.getCompoundTag("tag"));
		}
		return null;
	}
	
	public static IArtifactEffect getEffectFromSocketable(ItemStack stack) {
		if (stack.stackTagCompound != null) {
			return getEffectFromNBT(stack.stackTagCompound);
		}
		return null;
	}
	
	private static IArtifactEffect getEffectFromNBT(NBTTagCompound tag) {
		if (tag.hasKey(ARTIFACTTAG)) {
			if (tag.hasKey(TRIGGERTAG) && tag.hasKey(EFFECTTAG)) {
				return effects.get(tag.getString(EFFECTTAG)).theEffect;
			}
		}
		return null;
	}
	
	public static void doEffectCooldownTick(NBTTagCompound tag) {
		if (tag.hasKey(ARTIFACTTAG)) {
			if (tag.hasKey(TRIGGERTAG) && tag.hasKey(EFFECTTAG)) {
				// NYI
			}
		}
	}
	
	public static void addSocketToItem(ItemStack stack) {
		if (stack.stackSize == 1) {
			if (stack.stackTagCompound == null) {
				stack.stackTagCompound = new NBTTagCompound();
			}
			NBTTagCompound tag = stack.stackTagCompound;
			if (!tag.hasKey(SOCKETEDTAG)) {
				tag.setTag(SOCKETEDTAG, new NBTTagCompound());
			}
			tag = tag.getCompoundTag(SOCKETEDTAG);
			if (tag.hasKey(SOCKETCOUNTTAG)) {
				tag.setInteger(SOCKETCOUNTTAG, tag.getInteger(SOCKETCOUNTTAG) +1);
			} else {
				tag.setInteger(SOCKETCOUNTTAG, 1);
			}
		}
	}
	
	public static void addItemToSocket(ItemStack stack, ItemStack socketable, int socket) {
		if (stack.stackSize != 1 
				|| stack.stackTagCompound == null 
				|| !stack.stackTagCompound.hasKey(SOCKETEDTAG)) {
			return;
		}
		NBTTagCompound data = stack.stackTagCompound.getCompoundTag(SOCKETEDTAG);
			
		if (data.hasKey(SOCKETCOUNTTAG)) {
			int sockets = data.getInteger(SOCKETCOUNTTAG);
			if (socket >= sockets) {
				return;
			}
			
			NBTTagCompound itemnbt = new NBTTagCompound();
			socketable.writeToNBT(itemnbt);
			
			data.setTag(SOCKETTAG + socket, itemnbt);
		}
	}
	
	// ################### internal classes ###################
	
	private class WeightedTrigger extends WeightedRandom.Item {

		public IArtifactTrigger theTrigger;
		
		public WeightedTrigger(int weight, IArtifactTrigger trigger) {
			super(weight);
			this.theTrigger = trigger;
		}
	}
	
	private class WeightedEffect extends WeightedRandom.Item {

		public IArtifactEffect theEffect;
		
		public WeightedEffect(int weight, IArtifactEffect effect) {
			super(weight);
			this.theEffect = effect;
		}
	}
}

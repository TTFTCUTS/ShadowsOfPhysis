package ttftcuts.physis.common.artifact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.IIcon;
import net.minecraft.util.WeightedRandom;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;

import ttftcuts.physis.Physis;
import ttftcuts.physis.api.artifact.IArtifactEffect;
import ttftcuts.physis.api.artifact.IArtifactTrigger;
import ttftcuts.physis.api.internal.IArtifactHandler.CooldownCategory;
import ttftcuts.physis.common.PhysisItems;
import ttftcuts.physis.common.artifact.effect.EffectAir;
import ttftcuts.physis.common.artifact.effect.EffectExplosion;
import ttftcuts.physis.common.artifact.effect.EffectFire;
import ttftcuts.physis.common.artifact.effect.EffectForce;
import ttftcuts.physis.common.artifact.effect.EffectPotion;
import ttftcuts.physis.common.artifact.trigger.TriggerOnDealDamage;
import ttftcuts.physis.common.artifact.trigger.TriggerOnEquippedUpdate;
import ttftcuts.physis.common.artifact.trigger.TriggerOnTakeDamage;
import ttftcuts.physis.common.artifact.trigger.TriggerOnUpdate;
import ttftcuts.physis.common.artifact.trigger.UpdateCondition;
import ttftcuts.physis.common.file.ServerData;

public final class PhysisArtifacts {

	private static PhysisArtifacts instance;
	
	public static Random triggerColourRand = new Random(23456);
	public static Random effectColourRand = new Random(12345);
	public static final String PREFIX = Physis.MOD_ID+".artifact.";
	
	public static final String ARTIFACTTAG = "physisArtifact";
	public static final String TRIGGERTAG = "trigger";
	public static final String EFFECTTAG = "effect";
	public static final String COOLDOWNTAG = "cooldown";
	public static final String SOCKETEDTAG = "physisSocketed";
	public static final String SOCKETTAG = "socket";
	public static final String SOCKETCOUNTTAG = "count";
	public static final String SOCKETFIXED = "socketFixed";
	
	public static Map<String, WeightedTrigger> triggers = new HashMap<String, WeightedTrigger>();
	public static Map<String, WeightedEffect> effects = new HashMap<String, WeightedEffect>();
	
	@SideOnly(Side.CLIENT)
	public static Map<IArtifactTrigger, IIcon> triggerIcons = new HashMap<IArtifactTrigger, IIcon>();
	@SideOnly(Side.CLIENT)
	public static Map<IArtifactEffect, IIcon> effectIcons = new HashMap<IArtifactEffect, IIcon>();
	@SideOnly(Side.CLIENT)
	public static IIcon defaultIcon;
	
	public static void init() {
		instance = new PhysisArtifacts();
		
		// register triggers
		//registerPhysisTrigger(new TriggerOnUpdate("OnUpdate"), 50);
		for(UpdateCondition condition : UpdateCondition.values()) {
			registerPhysisTrigger(new TriggerOnUpdate("OnUpdate", condition), (int)Math.floor(50 * condition.relativeRarity));
		}
		//registerPhysisTrigger(new TriggerOnEquippedUpdate("OnEquippedUpdate"), 100);
		for(UpdateCondition condition : UpdateCondition.values()) {
			registerPhysisTrigger(new TriggerOnEquippedUpdate("OnEquippedUpdate", condition), (int)Math.floor(100 * condition.relativeRarity));
		}
		registerPhysisTrigger(new TriggerOnDealDamage("OnDealDamage", false), 100);
		registerPhysisTrigger(new TriggerOnDealDamage("OnDealDamageSelf", true), 50);
		registerPhysisTrigger(new TriggerOnTakeDamage("OnTakeDamage", false), 100);
		registerPhysisTrigger(new TriggerOnTakeDamage("OnTakeDamageSelf", true), 50);
		
		// register effects
		
		// potions
		registerPhysisEffect(new EffectPotion("Poison", Potion.poison)
			.setCooldowns(2, 2.5, 3, 4, 6, 9, 12)
			.setDurations(4, 5, 5, 5, 6, 6, 6), 100);
		registerPhysisEffect(new EffectPotion("Wither", Potion.wither)
			.setCooldowns(3, 4, 6, 9, 12, 15, 25)
			.setDurations(4, 5, 5, 5, 6, 6, 6), 20);
		registerPhysisEffect(new EffectPotion("Hunger", Potion.hunger)
			.setCooldowns(1, 1.5, 2, 2.5, 5, 7.5, 10)
			.setDurations(4, 4, 5, 5, 6, 7, 8), 100);
		registerPhysisEffect(new EffectPotion("Slowness", Potion.moveSlowdown)
			.setCooldowns(1, 1.5, 2, 2.5, 5, 7.5, 10)
			.setDurations(1, 1.5, 2, 2.5, 3.5, 5, 7.5), 60);
		registerPhysisEffect(new EffectPotion("Speed", Potion.moveSpeed)
			.setCooldowns(2, 3, 4, 5, 10, 15, 20)
			.setDurations(1, 1, 1.5, 1.5, 2, 2, 2.5), 60);
		registerPhysisEffect(new EffectPotion("Fatigue", Potion.digSlowdown)
			.setCooldowns(1, 1.5, 2, 2.5, 5, 7.5, 10)
			.setDurations(2, 3, 4, 5, 6, 7, 8), 60);
		registerPhysisEffect(new EffectPotion("Haste", Potion.digSpeed)
			.setCooldowns(2, 3, 4, 5, 10, 15, 20)
			.setDurations(2, 2.5, 3, 3.5, 4, 4.5, 5), 80);
		registerPhysisEffect(new EffectPotion("Strength", Potion.damageBoost)
			.setCooldowns(2, 3, 4, 5, 10, 15, 20)
			.setDurations(1, 1, 1.5, 1.5, 2, 2, 2.5), 60);
		registerPhysisEffect(new EffectPotion("Weakness", Potion.weakness)
			.setCooldowns(1, 1.5, 2, 2.5, 5, 7.5, 10)
			.setDurations(4, 5, 6, 7, 8, 9, 10), 80);
		registerPhysisEffect(new EffectPotion("Blindness", Potion.blindness)
			.setCooldowns(2, 2.5, 3, 4, 6, 9, 12)
			.setDurations(4, 5, 5, 5, 6, 6, 6), 60);
		registerPhysisEffect(new EffectPotion("Regeneration", Potion.regeneration)
			.setCooldowns(10, 15, 20, 25, 30, 45, 60)
			.setDurations(6, 6, 7, 7, 8, 8, 10), 10);
		registerPhysisEffect(new EffectPotion("Resistance", Potion.resistance)
			.setCooldowns(1, 2, 3, 4, 6, 15, 25)
			.setDurations(4, 5, 6, 7, 8, 9, 10), 50);
		registerPhysisEffect(new EffectPotion("FireResistance", Potion.fireResistance)
			.setCooldowns(10, 15, 20, 25, 30, 45, 60)
			.setDurations(6, 6, 7, 7, 8, 8, 10), 10);
		
		// explosions
		registerPhysisEffect(new EffectExplosion("SmallExplosion", 4)
			.setCooldowns(2, 3, 4, 5, 7, 9, 11), 20);
		
		// fire
		registerPhysisEffect(new EffectFire("Fire")
			.setCooldowns(1,2,3,5,7,10,12)
			.setDurations(2,3,4,5,6,8,10), 100);
		
		// force
		registerPhysisEffect(new EffectForce("Forceup", 0.75)
			.setCooldowns(3, 5, 8, 10, 13, 17, 20), 10);
		registerPhysisEffect(new EffectForce("Forcedown", -1.5)
			.setCooldowns(3, 5, 8, 10, 13, 17, 20), 5);
		
		// air
		registerPhysisEffect(new EffectAir("Airup", 1)
			.setCooldowns(0.5, 1, 2, 3, 4, 6, 8), 10);
		registerPhysisEffect(new EffectAir("Airdown", -1)
			.setCooldowns(0.5, 1, 2, 3, 4, 6, 8), 5);
	}
	
	// registering the icon event handler
	public static void clientInit() {
		MinecraftForge.EVENT_BUS.register(instance.new IconHandler());
	}
	
	// ################### registration ###################
	
	public static boolean registerTrigger(IArtifactTrigger trigger, int weight) {
		String name = trigger.getName();
		if (!triggers.containsKey(name)) {
			triggers.put(name, instance.new WeightedTrigger(weight, trigger));
			return true;
		}
		return false;
	}
	
	public static boolean registerEffect(IArtifactEffect effect, int weight) {
		String name = effect.getName();
		if (!effects.containsKey(name)) {
			effects.put(name, instance.new WeightedEffect(weight, effect));
			return true;
		}
		return false;
	}
	
	private static IArtifactTrigger registerPhysisTrigger(IArtifactTrigger trigger, int weight) {
		registerTrigger(trigger, weight);
		return trigger;
	}
	
	private static IArtifactEffect registerPhysisEffect(IArtifactEffect effect, int weight) {
		registerEffect(effect, weight);
		return effect;
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
			tag = tag.getCompoundTag(ARTIFACTTAG);
			if (tag.hasKey(TRIGGERTAG) && tag.hasKey(EFFECTTAG)) {
				String name = tag.getString(TRIGGERTAG);
				if (triggers.containsKey(name)) {
					IArtifactTrigger trigger = triggers.get(name).theTrigger;
					return trigger;
				}
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
			tag = tag.getCompoundTag(ARTIFACTTAG);
			if (tag.hasKey(TRIGGERTAG) && tag.hasKey(EFFECTTAG)) {
				String name = tag.getString(EFFECTTAG);
				if (effects.containsKey(name)) {
					return effects.get(name).theEffect;
				}
			}
		}
		return null;
	}
	
	public static void doEffectCooldownTick(NBTTagCompound tag) {
		if (tag.hasKey("tag")) {
			doEffectCooldownTick(tag.getCompoundTag("tag"));
			return;
		}
		if (tag.hasKey(ARTIFACTTAG)) {
			tag = tag.getCompoundTag(ARTIFACTTAG);
			if (tag.hasKey(TRIGGERTAG) && tag.hasKey(EFFECTTAG)) {
				int cooldown = tag.getInteger(COOLDOWNTAG);
				if (cooldown > 0) {
					tag.setInteger(COOLDOWNTAG, cooldown - 1);
				}
			}
		}
	}
	
	public static long getEffectCooldown(NBTTagCompound tag, boolean client) {
		if (tag.hasKey("tag")) {
			return getEffectCooldown(tag.getCompoundTag("tag"), client);
		}
		if (tag.hasKey(ARTIFACTTAG)) {
			tag = tag.getCompoundTag(ARTIFACTTAG);
			if (tag.hasKey(TRIGGERTAG) && tag.hasKey(EFFECTTAG)) {
				return Math.max(0, tag.getLong(COOLDOWNTAG) - ServerData.instance(client).serverTick);
			}
		}
		return 0;
	}
	
	public static long getEffectCooldown(ItemStack stack, boolean client) {
		if (stack.stackTagCompound != null) {
			return getEffectCooldown(stack.stackTagCompound, client);
		}
		return 0;
	}
	
	public static long getEffectMaxCooldown(NBTTagCompound tag) {
		if (tag.hasKey("tag")) {
			return getEffectMaxCooldown(tag.getCompoundTag("tag"));
		}
		if (tag.hasKey(ARTIFACTTAG)) {
			tag = tag.getCompoundTag(ARTIFACTTAG);
			if (tag.hasKey(TRIGGERTAG) && tag.hasKey(EFFECTTAG)) {
				//return Math.max(0, tag.getLong(COOLDOWNTAG) - ServerData.instance.serverTick);
				String ename = tag.getString(EFFECTTAG);
				String tname = tag.getString(TRIGGERTAG);
				if (effects.containsKey(ename) && triggers.containsKey(tname)) {
					CooldownCategory cd = triggers.get(tname).theTrigger.getCooldownCategory();
					effects.get(ename).theEffect.getCooldown(cd);
				}
			}
		}
		return 0;
	}
	
	public static void setEffectCooldown(NBTTagCompound tag, int cooldown) {
		if (tag.hasKey("tag")) {
			setEffectCooldown(tag.getCompoundTag("tag"), cooldown);
			return;
		}
		if (tag.hasKey(ARTIFACTTAG)) {
			tag = tag.getCompoundTag(ARTIFACTTAG);
			if (tag.hasKey(TRIGGERTAG) && tag.hasKey(EFFECTTAG)) {
				if (cooldown >= 0 && ServerData.instance(false) != null) {
					tag.setLong(COOLDOWNTAG, cooldown + ServerData.instance(false).serverTick);
				}
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
	
	public static void removeItemFromSocket(ItemStack stack, int socket) {
		NBTTagCompound data = stack.stackTagCompound.getCompoundTag(SOCKETEDTAG);
		
		if (data.hasKey(SOCKETCOUNTTAG)) {
			int sockets = data.getInteger(SOCKETCOUNTTAG);
			if (socket >= sockets) {
				return;
			}

			data.removeTag(SOCKETTAG + socket);
		}
	}
	
	public static void addTriggerAndEffectToItem(ItemStack stack, IArtifactTrigger trigger, IArtifactEffect effect) {
		if (stack.stackSize == 1) {
			if (stack.stackTagCompound == null) {
				stack.stackTagCompound = new NBTTagCompound();
			}
			NBTTagCompound tag = stack.stackTagCompound;
			if (!tag.hasKey(ARTIFACTTAG)) {
				tag.setTag(ARTIFACTTAG, new NBTTagCompound());
			}
			tag = tag.getCompoundTag(ARTIFACTTAG);
			
			tag.setString(TRIGGERTAG, trigger.getName());
			tag.setString(EFFECTTAG, effect.getName());
		}		
	}
	
	public static void addRandomTriggerAndEffectToItem(ItemStack stack, Random rand) {
		addTriggerAndEffectToItem(stack, getRandomTrigger(rand), getRandomEffect(rand));
	}
	
	public static boolean canItemAcceptSockets(ItemStack stack) {
		Item item = stack.getItem();
		if (item == PhysisItems.socketable) {
			return false;
		}
		if (item instanceof ItemBlock) {
			return false;
		}
		if (item.getItemStackLimit(stack) > 1) {
			return false;
		}
		return true;
	}
	
	// ################### internal classes ###################
	
	public class WeightedTrigger extends WeightedRandom.Item {

		public IArtifactTrigger theTrigger;
		public IIcon icon;
		
		public WeightedTrigger(int weight, IArtifactTrigger trigger) {
			super(weight);
			this.theTrigger = trigger;
		}
	}
	
	public class WeightedEffect extends WeightedRandom.Item {

		public IArtifactEffect theEffect;
		public IIcon icon;
		
		public WeightedEffect(int weight, IArtifactEffect effect) {
			super(weight);
			this.theEffect = effect;
		}
	}
	
	public static List<DelayedPunt> entitiesToPunt = new ArrayList<DelayedPunt>();
	public class DelayedPunt {
		public int delay = 0;
		public final EntityLivingBase entity;
		public final double force;
		
		public DelayedPunt(EntityLivingBase entity, double force, int delay) {
			this.entity = entity;
			this.force = force;
			this.delay = delay;
		}
	}
	public static void puntEntity(EntityLivingBase entity, double force, int delay) {
		entitiesToPunt.add(instance.new DelayedPunt(entity, force, delay));
	}
	public static void doPuntEntities() {
		for(int i=entitiesToPunt.size()-1; i>=0; i--) {
			DelayedPunt punt = entitiesToPunt.get(i);
			if (punt.delay > 0) {
				punt.delay--;
			} else {
				punt.entity.motionY += punt.force;
				entitiesToPunt.remove(i);
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public class IconHandler {
		@SubscribeEvent
		public void OnTextureStitch(TextureStitchEvent event) {
			// if item sheet
			if (event.map.getTextureType() == 1) {
				Physis.logger.info("STITCHING THE THINGS");
				
				Physis.logger.info("Default icon");
				PhysisArtifacts.defaultIcon = event.map.registerIcon(Physis.MOD_ID+":trigger_effect/default");
				
				for(WeightedTrigger trigger : PhysisArtifacts.triggers.values()) {
					Physis.logger.info("Trigger: "+trigger.theTrigger.getName());
					IIcon icon = trigger.theTrigger.registerIcon(event.map);
					PhysisArtifacts.triggerIcons.put(trigger.theTrigger, icon);
				}
				
				for(WeightedEffect effect : PhysisArtifacts.effects.values()) {
					Physis.logger.info("Effect: "+effect.theEffect.getName());
					IIcon icon = effect.theEffect.registerIcon(event.map);
					PhysisArtifacts.effectIcons.put(effect.theEffect, icon);
				}
				
				Physis.logger.info("FINISHED STITCHING");
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static IIcon getTriggerIcon(String name) {
		IArtifactTrigger trigger = getTrigger(name);
		return getTriggerIcon(trigger);
	}
	@SideOnly(Side.CLIENT)
	public static IIcon getTriggerIcon(IArtifactTrigger trigger) {	
		if (trigger != null && triggerIcons.containsKey(trigger)) {
			IIcon icon = triggerIcons.get(trigger);
			if (icon != null ) {
				return icon;
			}
		}
		return defaultIcon;
	}
	
	@SideOnly(Side.CLIENT)
	public static IIcon getEffectIcon(String name) {
		IArtifactEffect effect = getEffect(name);
		return getEffectIcon(effect);
	}
	@SideOnly(Side.CLIENT)
	public static IIcon getEffectIcon(IArtifactEffect effect) {
		if (effect != null && effectIcons.containsKey(effect)) {
			IIcon icon = effectIcons.get(effect);
			if (icon != null) {
				return icon;
			}
		}
		return defaultIcon;
	}
}

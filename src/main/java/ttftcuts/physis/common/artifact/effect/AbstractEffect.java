package ttftcuts.physis.common.artifact.effect;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import ttftcuts.physis.Physis;
import ttftcuts.physis.api.artifact.IArtifactEffect;
import ttftcuts.physis.api.internal.IArtifactHandler.CooldownCategory;
import ttftcuts.physis.common.artifact.PhysisArtifacts;

public abstract class AbstractEffect implements IArtifactEffect {
	
	protected String name;
	protected int[] cooldowns = {20, 20, 20, 20, 20, 20, 20};
	protected int[] durations = {20, 20, 20, 20, 20, 20, 20};
	
	public AbstractEffect(String name) {
		this.name = Physis.MOD_ID +"_"+ name;
	}
	
	@Override
	public void doEffect(ItemStack stack, EntityLivingBase target, EntityLivingBase source, int id, CooldownCategory cooldowntype) {
		NBTTagCompound[] sockets = PhysisArtifacts.getSocketablesFromStack(stack);
		
		if (sockets.length <= id+1 && sockets[id] != null) {
			long remainingCooldown = PhysisArtifacts.getEffectCooldown(sockets[id]);
			if (remainingCooldown == 0) {
				
				this.doEffectChecked(sockets[id], stack, target, source, id, cooldowntype);
				this.doCooldown(sockets[id], cooldowntype);
			}
		}
	}
	
	public void doEffectChecked(NBTTagCompound tag, ItemStack stack, EntityLivingBase target, EntityLivingBase source, int id, CooldownCategory cooldowntype) {}
	
	protected void doCooldown(NBTTagCompound tag, CooldownCategory cd) {
		PhysisArtifacts.setEffectCooldown(tag, cooldowns[cd.ordinal()]);
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	public AbstractEffect setCooldowns(double... cd) {
		int len = Math.min(6, cd.length);
		for (int i=0; i<len; i++) {
			cooldowns[i] = (int)Math.ceil(cd[i] * 20.0);
		}
		return this;
	}
	
	public AbstractEffect setDurations(double... d) {
		int len = Math.min(6, d.length);
		for (int i=0; i<len; i++) {
			durations[i] = (int)Math.ceil(d[i] * 20.0);
		}
		return this;
	}
}

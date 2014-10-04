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
			}
		}
	}
	
	public void doEffectChecked(NBTTagCompound tag, ItemStack stack, EntityLivingBase target, EntityLivingBase source, int id, CooldownCategory cooldowntype) {}
	
	@Override
	public String getName() {
		return this.name;
	}
}

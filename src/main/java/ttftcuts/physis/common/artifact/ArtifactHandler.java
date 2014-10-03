package ttftcuts.physis.common.artifact;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import ttftcuts.physis.api.artifact.IArtifactEffect;
import ttftcuts.physis.api.artifact.IArtifactTrigger;
import ttftcuts.physis.api.internal.IArtifactHandler;

public class ArtifactHandler implements IArtifactHandler {
	
	@Override
	public boolean registerArtifactTrigger(String name, IArtifactTrigger trigger, int weight) {
		return PhysisArtifacts.registerTrigger(name, trigger, weight);
	}

	@Override
	public boolean registerArtifactEffect(String name, IArtifactEffect effect, int weight) {
		return PhysisArtifacts.registerEffect(name, effect, weight);
	}

	@Override
	public boolean triggerArtifactEffect(ItemStack stack, EntityLivingBase target, EntityLivingBase source, int id, CooldownCategory cooldown) {
		NBTTagCompound[] sockets = PhysisArtifacts.getSocketablesFromStack(stack);
		if (sockets != null) {
			if (sockets[id] != null) {
				IArtifactEffect effect = PhysisArtifacts.getEffectFromSocketable(sockets[id]);
				effect.doEffect(target, source, cooldown);
			}
		}
		return false;
	}

	@Override
	public boolean triggerInWorldArtifactEffect(int x, int y, int z, EntityLivingBase target, int id, CooldownCategory cooldown) {
		return false;
	}

}

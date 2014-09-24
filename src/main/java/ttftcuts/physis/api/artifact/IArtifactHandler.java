package ttftcuts.physis.api.artifact;

import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;

public interface IArtifactHandler {

	public boolean registerArtifactTrigger(String name, IArtifactTrigger trigger, int weight);
	
	public boolean registerArtifactEffect(String name, IArtifactEffect effect, int weight);
	
	public boolean triggerArtifactEffect(ItemStack stack, EntityLiving target, int id, CooldownCategory cooldown);
	
	public boolean triggerInWorldArtifactEffect(int x, int y, int z, EntityLiving target, int id, CooldownCategory cooldown);
	
	public enum CooldownCategory {
		LONG,
		SHORT,
		NONE
	}
}

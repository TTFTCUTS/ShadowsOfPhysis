package ttftcuts.physis.common.artifact.effect;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import ttftcuts.physis.api.internal.IArtifactHandler.CooldownCategory;
import ttftcuts.physis.common.artifact.PhysisArtifacts;

public class EffectPoison extends AbstractEffect {

	public EffectPoison(String name) {
		super(name);
	}

	@Override
	public void doEffectChecked(NBTTagCompound tag, ItemStack stack, EntityLivingBase target, EntityLivingBase source, int id, CooldownCategory cooldowntype) {
		PhysisArtifacts.setEffectCooldown(tag, 200);
		
		target.addPotionEffect(new PotionEffect(Potion.poison.id, 100));
	}
}

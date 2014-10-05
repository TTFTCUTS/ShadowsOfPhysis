package ttftcuts.physis.common.artifact.effect;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import ttftcuts.physis.api.internal.IArtifactHandler.CooldownCategory;

public class EffectPotion extends AbstractEffect {

	private Potion potionEffect;
	
	public EffectPotion(String name, Potion potion) {
		super(name);
		this.potionEffect = potion;
	}

	@Override
	public void doEffectChecked(NBTTagCompound tag, ItemStack stack, EntityLivingBase target, EntityLivingBase source, int id, CooldownCategory cooldowntype) {
		target.addPotionEffect(new PotionEffect(this.potionEffect.id, durations[cooldowntype.ordinal()]));
	}
	
	
}

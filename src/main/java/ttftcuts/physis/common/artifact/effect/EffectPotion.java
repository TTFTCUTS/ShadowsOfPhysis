package ttftcuts.physis.common.artifact.effect;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import ttftcuts.physis.api.internal.IArtifactHandler.CooldownCategory;

public class EffectPotion extends AbstractEffect {

	private Potion potionEffect;
	private int[] durations = {20, 20, 20, 20, 20, 20, 20};
	
	public EffectPotion(String name, Potion potion) {
		super(name);
		this.potionEffect = potion;
	}

	@Override
	public void doEffectChecked(NBTTagCompound tag, ItemStack stack, EntityLivingBase target, EntityLivingBase source, int id, CooldownCategory cooldowntype) {
		target.addPotionEffect(new PotionEffect(this.potionEffect.id, durations[cooldowntype.ordinal()]));
	}
	
	public AbstractEffect setDurations(int... d) {
		int len = Math.min(6, d.length);
		for (int i=0; i<len; i++) {
			durations[i] = d[i];
		}
		return this;
	}
}

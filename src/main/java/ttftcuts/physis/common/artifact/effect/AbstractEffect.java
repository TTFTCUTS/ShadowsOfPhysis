package ttftcuts.physis.common.artifact.effect;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import ttftcuts.physis.Physis;
import ttftcuts.physis.api.artifact.IArtifactEffect;
import ttftcuts.physis.api.internal.IArtifactHandler.CooldownCategory;
import ttftcuts.physis.common.artifact.PhysisArtifacts;

public abstract class AbstractEffect implements IArtifactEffect {
	
	protected String name;
	protected String image;
	protected int[] cooldowns = {0, 20, 20, 20, 20, 20, 20, 20};
	protected int[] durations = {0, 20, 20, 20, 20, 20, 20, 20};
	
	public double hue = 0.0;
	public double saturation = 0.0;
	
	public AbstractEffect(String name, double hue, double saturation) {
		this.name = PhysisArtifacts.PREFIX + name;
		this.image = "effect"+name;
		this.hue = hue;
		this.saturation = saturation;
	}
	public AbstractEffect(String name) {
		this(name,
				PhysisArtifacts.effectColourRand.nextDouble(),
				1.0 - (PhysisArtifacts.effectColourRand.nextDouble() * PhysisArtifacts.effectColourRand.nextDouble())
		);
	}
	
	@Override
	public void doEffect(ItemStack stack, EntityLivingBase target, EntityLivingBase source, int id, CooldownCategory cooldowntype) {
		if (target.worldObj.isRemote) { return; }
		NBTTagCompound[] sockets = PhysisArtifacts.getSocketablesFromStack(stack);
		
		if (sockets.length >= id+1 && sockets[id] != null) {
			long remainingCooldown = PhysisArtifacts.getEffectCooldown(sockets[id], false);
			if (remainingCooldown <= 0) {
				
				this.doEffectChecked(sockets[id], stack, target, source, id, cooldowntype);
				this.doCooldown(sockets[id], cooldowntype);
			}
		}
	}
	
	public void doEffectChecked(NBTTagCompound tag, ItemStack stack, EntityLivingBase target, EntityLivingBase source, int id, CooldownCategory cooldowntype) {}
	
	@Override
	public int getCooldown(CooldownCategory cd) {
		return cooldowns[cd.ordinal()];
	}
	
	@Override
	public int getDuration(CooldownCategory cd) {
		return durations[cd.ordinal()];
	}
	
	protected void doCooldown(NBTTagCompound tag, CooldownCategory cd) {
		PhysisArtifacts.setEffectCooldown(tag, getCooldown(cd));
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public String getLocalizationName() {
		return this.name;
	}
	
	@Override
	public String getUnlocalizedEffectString() {
		return this.getLocalizationName() + ".effect";
		//return "does a thing to %2$s";
	}
	
	@Override
	public String getTooltipInfo() {
		return "<No extra info>";
	}
	
	public AbstractEffect setCooldowns(double... cd) {
		int len = Math.min(7, cd.length);
		for (int i=0; i<len; i++) {
			cooldowns[i+1] = (int)Math.ceil(cd[i] * 20.0);
		}
		return this;
	}
	
	public AbstractEffect setDurations(double... d) {
		int len = Math.min(7, d.length);
		for (int i=0; i<len; i++) {
			durations[i+1] = (int)Math.ceil(d[i] * 20.0);
		}
		return this;
	}
	
	public AbstractEffect setColour(double hue, double saturation) {
		this.hue = hue;
		this.saturation = saturation;
		return this;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon registerIcon(IIconRegister register) {
		return register.registerIcon(Physis.MOD_ID+":trigger_effect/"+this.image);
	}
	
	@Override
	public double getHue() {
		return this.hue;
	}
	
	@Override
	public double getSaturation() {
		return this.saturation;
	}
}

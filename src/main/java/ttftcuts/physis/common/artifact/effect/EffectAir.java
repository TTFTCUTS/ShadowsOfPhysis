package ttftcuts.physis.common.artifact.effect;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import ttftcuts.physis.api.internal.IArtifactHandler.CooldownCategory;
import ttftcuts.physis.common.network.PhysisPacketHandler;
import ttftcuts.physis.common.network.packet.PacketPlayerUpdate;

public class EffectAir extends AbstractEffect {

	public int amount;
	
	public EffectAir(String name, int amount) {
		super(name);
		this.amount = amount;
	}
	
	@Override
	public void doEffectChecked(NBTTagCompound tag, ItemStack stack, EntityLivingBase target, EntityLivingBase source, int id, CooldownCategory cooldowntype) {
		int air = target.getAir();
		int newair = Math.max(0, Math.min(300, air + amount));
		target.setAir(newair);
		
		if (target instanceof EntityPlayerMP) {
			EntityPlayerMP p = (EntityPlayerMP)target;
			PhysisPacketHandler.bus.sendTo(PacketPlayerUpdate.createAirPacket(amount), p);
		}
	}
}

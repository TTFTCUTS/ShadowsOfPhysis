package ttftcuts.physis.utils;

import ttftcuts.physis.api.artifact.IArtifactEffect;
import ttftcuts.physis.api.artifact.IArtifactTrigger;
import ttftcuts.physis.common.artifact.PhysisArtifacts;
import net.minecraft.nbt.NBTTagCompound;

public class Socket {
	public NBTTagCompound tag;
	public int slot;
	public IArtifactTrigger trigger;
	public IArtifactEffect effect;
	public Socket(NBTTagCompound tag, int slot, boolean trigger, boolean effect) {
		this.tag = tag;
		this.slot = slot;
		
		if (tag != null && trigger) {
			this.trigger = PhysisArtifacts.getTriggerFromSocketable(tag);
		}
		if (tag != null && effect) {
			this.effect = PhysisArtifacts.getEffectFromSocketable(tag);
		}
	}
	public Socket(NBTTagCompound tag, int slot) {
		this(tag, slot, false, false);
	}
}

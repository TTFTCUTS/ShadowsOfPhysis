package ttftcuts.physis.common.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import ttftcuts.physis.Physis;
import ttftcuts.physis.api.artifact.IArtifactEffect;
import ttftcuts.physis.api.artifact.IArtifactTrigger;
import ttftcuts.physis.api.artifact.ISocketable;
import ttftcuts.physis.common.artifact.PhysisArtifacts;
import ttftcuts.physis.common.artifact.PhysisArtifacts.WeightedEffect;
import ttftcuts.physis.common.artifact.PhysisArtifacts.WeightedTrigger;

public class ItemSocketable extends ItemPhysis implements ISocketable {

	public ItemSocketable() {
		super();
		this.setMaxStackSize(1);
		this.setUnlocalizedName("socketable");
		this.setTextureName(Physis.MOD_ID+":gem");
	}
	
	@Override
	public int getSocketColour(ItemStack stack) {
		return 5;
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List types) {
		
		for(WeightedTrigger wtrig : PhysisArtifacts.triggers.values()) {
			IArtifactTrigger trigger = wtrig.theTrigger;
			
			for(WeightedEffect weff : PhysisArtifacts.effects.values()) {
				IArtifactEffect effect = weff.theEffect;
				
				ItemStack stack = new ItemStack(this);
				
				PhysisArtifacts.addTriggerAndEffectToItem(stack, trigger, effect);
				
				types.add(stack);
			}
		}
	}
}

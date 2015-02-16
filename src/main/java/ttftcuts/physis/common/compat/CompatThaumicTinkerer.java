package ttftcuts.physis.common.compat;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class CompatThaumicTinkerer extends CompatModule {

	public Item kamiResource;
	
	@Override
	public void postInitStart(FMLPostInitializationEvent event, boolean client) {
		kamiResource = GameRegistry.findItem("ThaumicTinkerer", "kamiResource");
		ItemStack ingotIchorium = new ItemStack(kamiResource, 1, 2);
		ItemStack nuggetIchorium = new ItemStack(kamiResource, 1, 3);
		
		if (OreDictionary.getOres("ingotIchorium").isEmpty()) {
			OreDictionary.registerOre("ingotIchorium", ingotIchorium);
		}
		if (OreDictionary.getOres("nuggetIchorium").isEmpty()) {
			OreDictionary.registerOre("nuggetIchorium", nuggetIchorium);
		}
	}

}

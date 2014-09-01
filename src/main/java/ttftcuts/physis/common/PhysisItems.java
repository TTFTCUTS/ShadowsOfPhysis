package ttftcuts.physis.common;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import ttftcuts.physis.common.item.ItemJournal;

public final class PhysisItems {
	public static Item journal;
	
	public static void init() {
		journal = registerItem(new ItemJournal());
	}
	
	private static Item registerItem(Item item) {
		GameRegistry.registerItem(item, item.getUnlocalizedName().replace("item.", ""));
		return item;
	}
}

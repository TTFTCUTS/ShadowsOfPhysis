package ttftcuts.physis.common;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import ttftcuts.physis.common.item.ItemCreativeTome;
import ttftcuts.physis.common.item.ItemPhysis;
import ttftcuts.physis.common.item.ItemPhysisThemedMeta;
import ttftcuts.physis.common.item.ItemJournal;
import ttftcuts.physis.common.item.ItemSocketable;
import ttftcuts.physis.common.item.ItemTrowel;

public final class PhysisItems {
	public static Item journal;
	public static Item creativeTome;
	
	public static Item trowel;
	
	public static Item addsocket;
	public static Item socketable;
	public static Item component;
	
	public static void init() {
		journal = registerItem(new ItemJournal());
		creativeTome = registerItem(new ItemCreativeTome());
		trowel = registerItem(new ItemTrowel());
		addsocket = registerItem(new ItemPhysis().setShowTooltip(true).setUnlocalizedName("addsocket").setTextureName("journal"));
		socketable = registerItem(new ItemSocketable());
		component = registerItem(new ItemPhysisThemedMeta("component", 2).setShowTooltip(true));
	}
	
	private static Item registerItem(Item item) {
		GameRegistry.registerItem(item, item.getUnlocalizedName().replace("item.", ""));
		return item;
	}
}

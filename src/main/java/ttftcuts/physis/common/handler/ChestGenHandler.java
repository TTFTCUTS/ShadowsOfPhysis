package ttftcuts.physis.common.handler;

import ttftcuts.physis.common.PhysisItems;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

public class ChestGenHandler {

	public static void init() {
		
		// debug things... not final at all!
		for (int i = 0; i<100; i++) {
			ChestGenHooks.addItem(ChestGenHooks.BONUS_CHEST, new WeightedRandomChestContent(new ItemStack(PhysisItems.socketable), 1,1, 5));
		}
	}
}

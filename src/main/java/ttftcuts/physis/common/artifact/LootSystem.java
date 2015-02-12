package ttftcuts.physis.common.artifact;

import java.util.List;
import java.util.Random;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import ttftcuts.physis.api.PhysisAPI;
import ttftcuts.physis.api.internal.LootList;
import ttftcuts.physis.common.PhysisItems;

public class LootSystem {

	public static LootList digSiteLootList;
	
	private static final int[] digBudgets 		= {200, 250, 300, 400, 500, 600, 800, 1000, 1500, 2500}; 
	private static final int[] digPrimeMinVals 	= {50,  50,  100, 150, 200, 250, 300, 400,  800,  1000};
	private static final int[] digMinVals 		= {25,  30,  40,  40,  50,  75,  100, 100,  150,  200 }; 
	private static final int[] digMaxVals 		= {200, 250, 300, 400, 400, 500, 600, 800,  1000, 1500}; 
	
	public static void init() {
		digSiteLootList = new LootList();
		PhysisAPI.digSiteLootList = digSiteLootList;
		
		digSiteLootList.addItemStackChestGen(new ItemStack(PhysisItems.socketable), 1, 1, 1000);
		
		digSiteLootList.addItemStackChestGen(new ItemStack(Items.enchanted_book), 1, 1, 400);
		
		digSiteLootList.addItemStack(new ItemStack(PhysisItems.component, 1, 0), 1, 1, 100);
		
		digSiteLootList.addItemStack(new ItemStack(Items.paper), 1, 2, 25);
		digSiteLootList.addItemStack(new ItemStack(Items.book), 1, 1, 40);
	}
	
	public static List<ItemStack> getDigSiteLoot(Random rand, int level) {
		level = Math.max(0, Math.min(digBudgets.length, level));
		
		int budget = (int)Math.round((rand.nextDouble()*0.4 + 0.8) * digBudgets[level]);
		return digSiteLootList.getLoot(rand, budget, digPrimeMinVals[level], digMinVals[level], digMaxVals[level]);
	}
}

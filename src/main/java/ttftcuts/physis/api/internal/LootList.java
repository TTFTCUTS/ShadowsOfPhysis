package ttftcuts.physis.api.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

public class LootList {

	public void addItemStack(ItemStack stack, int minCount, int maxCount, int price) {
		this.list.add(new LootEntry(stack, minCount, maxCount, price));
	}
	
	public void addItemStackChestGen(ItemStack stack, int minCount, int maxCount, int price) {
		this.list.add(new LootEntry(stack, minCount, maxCount, price, true));
	}
	
	public void addChest(ChestGenHooks chest, int price) {
		this.list.add(new LootEntry(chest, price));
	}
	
	// -------------------------------------
	
	private List<LootEntry> list;
	
	public LootList() {
		this.list = new ArrayList<LootEntry>();
	}
	
	private List<LootEntry> getLootRange(int minValue, int maxValue) {
		List<LootEntry> filtered = new ArrayList<LootEntry>();
		for (LootEntry entry : this.list) {
			if (entry.price >= minValue && entry.price <= maxValue) {
				filtered.add(entry);
			}
		}
		return filtered;
	}
	
	public List<ItemStack> getLoot(Random rand, int budget, int primeMinValue, int minValue, int maxValue) {
		List<ItemStack> loot = new ArrayList<ItemStack>();
		
		boolean first = true;
		
		while (budget > 0) {
			int min = Math.min(budget, (first) ? primeMinValue : minValue);
			int max = Math.min(budget, maxValue);
			
			List<LootEntry> lootlist = getLootRange(min, max);
			if (first && lootlist.isEmpty()) {
				lootlist = getLootRange(Math.min(budget, minValue), max);
			}
			
			if (lootlist.size() == 0) { break; }
			
			LootEntry lootitem = lootlist.get(rand.nextInt(lootlist.size()));
			
			loot.addAll(lootitem.getLoot(rand));
			
			budget -= lootitem.price;
			first = false;
		}
		
		return loot;
	}
	
	private enum LootType {
		NONE,
		ITEMSTACK,
		ITEMSTACKCHESTGEN,
		CHEST
	}
	
	private class LootEntry {
		
		public LootType type = LootType.NONE;
		public int price = 0;
		
		public ItemStack stack;
		public ChestGenHooks chest;
		public int min, max;
		
		public LootEntry(int min, int max, int price) {
			this.min = min;
			this.max = max;
			this.price = price;
		}
		
		public LootEntry(ItemStack stack, int min, int max, int price) {
			this(stack, min, max, price, false);
		}
		public LootEntry(ItemStack stack, int min, int max, int price, boolean chestGen) {
			this(min, max, price);
			this.type = chestGen ? LootType.ITEMSTACKCHESTGEN : LootType.ITEMSTACK;
			this.stack = stack;
		}
		
		public LootEntry(ChestGenHooks chest, int price) {
			this(1, 1, price);
			this.type = LootType.ITEMSTACK;
			this.chest = chest;
		}
		
		public List<ItemStack> getLoot(Random rand) {
			List<ItemStack> lootlist = new ArrayList<ItemStack>();
			int diff = this.max - this.min;
			int num = diff == 0 ? this.min : (rand.nextInt(this.max - this.min) + this.min);
			
			switch(this.type) {
			case ITEMSTACK:
				for (int i=0; i<num; i++) {
					ItemStack loot = this.stack.copy();
					lootlist.add(loot);
				}
				break;
			case ITEMSTACKCHESTGEN:
				for (int i=0; i<num; i++) {
					WeightedRandomChestContent c = new WeightedRandomChestContent(this.stack.copy(), 1,1,1);
					c = this.stack.getItem().getChestGenBase(ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST), rand, c);
					ItemStack[] stacks = ChestGenHooks.generateStacks(rand, c.theItemId, 1, 1);
					if (stacks.length > 0) {
						lootlist.add(stacks[0]);
					}
				}
				break;
			case CHEST:
				lootlist.add(this.chest.getOneItem(rand));
				break;
			default:
			}
			return lootlist;
		}
	}
}

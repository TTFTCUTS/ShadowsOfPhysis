package ttftcuts.physis.api.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ttftcuts.physis.api.PhysisAPI;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.oredict.OreDictionary;

public class LootList {

	public void addItemStack(ItemStack stack, int minCount, int maxCount, int price, double weight) {
		this.list.add(new LootEntry(stack, minCount, maxCount, price, weight));
	}
	public void addItemStack(ItemStack stack, int minCount, int maxCount, int price) {
		this.addItemStack(stack, minCount, maxCount, price, 1.0);
	}
	
	public void addItemStackChestGen(ItemStack stack, int minCount, int maxCount, int price, double weight) {
		this.list.add(new LootEntry(stack, minCount, maxCount, price, weight, true));
	}
	public void addItemStackChestGen(ItemStack stack, int minCount, int maxCount, int price) {
		this.addItemStackChestGen(stack, minCount, maxCount, price, 1.0);
	}
	
	public void addChest(ChestGenHooks chest, int price, double weight) {
		this.list.add(new LootEntry(chest, price, weight));
	}
	public void addChest(ChestGenHooks chest, int price) {
		this.addChest(chest, price, 1.0);
	}
	
	public void addOrePrefix(String prefix, int minCount, int maxCount, int price, double weight) {
		this.list.add(new LootEntry(prefix, minCount, maxCount, price, weight));
	}
	public void addOrePrefix(String prefix, int minCount, int maxCount, int price) {
		this.addOrePrefix(prefix, minCount, maxCount, price, 1.0);
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
			int min = (first) ? primeMinValue : minValue;
			int max = Math.min(budget, maxValue);
			
			List<LootEntry> lootlist = getLootRange(min, max);
			if (first && lootlist.isEmpty()) {
				lootlist = getLootRange(Math.min(budget, minValue), max);
			}
			
			if (lootlist.size() == 0) { break; }
			
			double totalweight = 0;
			for (int i=0; i<lootlist.size(); i++) {
				totalweight += lootlist.get(i).weight;
			}
			double roll = rand.nextDouble() * totalweight;
			double currentweight = 0;
			LootEntry lootitem = null;
			
			for (int i=0; i<lootlist.size(); i++) {
				LootEntry e = lootlist.get(i);
				currentweight += e.weight;
				if (currentweight >= roll) {
					lootitem = e;
					break;
				}
			}
			if (lootitem == null) { continue; }
			
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
		CHEST,
		OREPREFIX
	}
	
	private class LootEntry {
		
		public LootType type = LootType.NONE;
		public int price = 0;
		public double weight = 1.0;
		
		public ItemStack stack;
		public ChestGenHooks chest;
		public String oreprefix;
		private List<ItemStack> ores;
		public int min, max;
		
		public LootEntry(int min, int max, int price, double weight) {
			this.min = min;
			this.max = max;
			this.price = price;
			this.weight = weight;
		}
		
		public LootEntry(ItemStack stack, int min, int max, int price, double weight) {
			this(stack, min, max, price, weight, false);
		}
		public LootEntry(ItemStack stack, int min, int max, int price, double weight, boolean chestGen) {
			this(min, max, price, weight);
			this.type = chestGen ? LootType.ITEMSTACKCHESTGEN : LootType.ITEMSTACK;
			this.stack = stack;
		}
		public LootEntry(ChestGenHooks chest, int price, double weight) {
			this(1, 1, price, weight);
			this.type = LootType.CHEST;
			this.chest = chest;
		}
		public LootEntry(String oreprefix, int min, int max, int price, double weight) {
			this(min, max, price, weight);
			this.type = LootType.OREPREFIX;
			this.oreprefix = oreprefix;
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
			case OREPREFIX:
				if (ores == null) {
					this.ores = new ArrayList<ItemStack>();
					String[] orenames = OreDictionary.getOreNames();
					for (String orename : orenames) {
						if (PhysisAPI.forbiddenOreNames.contains(orename)) {
							continue;
						}
						if (orename.startsWith(this.oreprefix)) {
							this.ores.addAll(OreDictionary.getOres(orename));
						}
					}
				}
				if (ores.size() == 0) { break; }
				
				ItemStack stack = this.ores.get(rand.nextInt(this.ores.size())).copy();
				if (stack != null) {
					stack.stackSize = num;
					lootlist.add(stack);
				}
				break;
			default:
			}
			return lootlist;
		}
	}
}

package ttftcuts.physis.common.item.material;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import ttftcuts.physis.Physis;
import ttftcuts.physis.common.helper.TextureHelper;

import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class PhysisToolMaterial {
	
	public static final String MATERIALTAG = "physisMaterial";
	
	public static Map<String,PhysisToolMaterial> materials;
	private static List<PhysisToolMaterial> materialsById;
	public static List<RecipeListGetter> recipeLists = new ArrayList<RecipeListGetter>();
	public static RecipeListGetter defaultRecipeList;
	
	public static final int TINTS = 10; 
	private static int[] defaultTints;
	
	public static boolean generated = false;
	private static int nextId = 0;
	
	public final int id;
	
	public String orename;
	public String orematerial;
	public String stickorename;
	
	public ItemStack ingot;
	public ItemStack stick;
	public ItemStack pick;
	
	public ItemPickaxe pickitem;
	
	public Item.ToolMaterial toolmaterial;
	
	public int maxdamage;
	
	public boolean hastint = false;
	public int[] tints;
	public int shafttint;
	
	public Object sourceRecipe;
	
	public PhysisToolMaterial(String orename, ItemStack ingot, String stickorename, ItemStack stick, ItemStack pick, Object source) {
		//Physis.logger.info("Registering material for "+orename+" with ingot "+ingot+", stick "+stick+" and pick "+pick);
		this.id = nextId;
		materialsById.add(this);
		nextId++;
		
		this.orename = orename;
		this.orematerial = orename.replaceFirst("[^A-Z]*(?=[A-Z])", "");
		this.ingot = ingot;
		this.stick = stick;
		this.stickorename = stickorename;
		this.pick = pick;
		this.pickitem = (ItemPickaxe)(pick.getItem());
		
		this.toolmaterial = this.pickitem.func_150913_i();
		
		this.maxdamage = this.toolmaterial.getMaxUses();
		
		this.sourceRecipe = source;
	}
	
	public String getMaterialName() {
		return this.orematerial;
	}
	
	public int[] getHeadTints() {
		if (this.hastint) {
			return this.tints;
		}
		buildTintData(this);
		if (this.hastint) {
			return this.tints;
		}
		return defaultTints;
	}
	
	public int getShaftTint() {
		if (this.hastint) {
			return this.shafttint;
		}
		return 0x808080;
	}
	
	@SuppressWarnings("unchecked")
	public static void buildMaterials() {		
		//Physis.logger.info("Building tool material list");
		materials = new HashMap<String, PhysisToolMaterial>();
		materialsById = new ArrayList<PhysisToolMaterial>();
		
		//Physis.logger.info("Getting Ingots");
		
		//String[] orenames = OreDictionary.getOreNames();
		
		/*List<String> ingottypes = new ArrayList<String>();
		
		for(String name : orenames) {
			if (name.startsWith("ingot")) {
				ingottypes.add(name);
				//Physis.logger.info(name);
			}
		}*/
		
		//Physis.logger.info("Searching for picks");
		
		List<ItemPickaxe> picks = new ArrayList<ItemPickaxe>();
		
		Iterator<Item> ir = Item.itemRegistry.iterator();
		
		while(ir.hasNext()) {
			Item item = ir.next();
			
			if (item instanceof ItemPickaxe) {
				picks.add((ItemPickaxe)item);
				//Physis.logger.info(item.getUnlocalizedName());
			}
		}
		
		//Physis.logger.info("Cross-checking materials");
		for (RecipeListGetter list : recipeLists) {
			Iterator<?> iter = list.getIterator();
			
			while(iter.hasNext()) {
				Object recipe = iter.next();
				IRecipeComponentTranslator translator = getTranslatorForRecipe(list, recipe);
				
				if (translator == null || translator.getRecipeOutput(recipe) == null || translator.getRecipeOutput(recipe).getItem() == null) {
					continue;
				}
				ItemStack output = translator.getRecipeOutput(recipe);
				Item out = output.getItem();
				
				for(ItemPickaxe pick : picks) {
					if (out == pick) {
						ItemStack[] comp = null;
						boolean stickore = false;
						
						comp = translator.getRecipeComponents(recipe);
						stickore = translator.hasOreDictStick();
						
						if (comp != null && comp.length == 9) {
							if (comp[0] != null && comp[1] != null && comp[2] != null && comp[4] != null && comp[7] != null) {
								// looks pick shaped to me!
								ItemStack stickitem = comp[4];
								ItemStack otherstick = comp[7];
								if (!compareStacks(stickitem, otherstick)) {
									// but the sticks don't match
									continue;
								}
								
								// stick processing
								String stickorename = null;
								
								if (stickore) {
									int[] stickoreids = OreDictionary.getOreIDs(stickitem);
									if (stickoreids.length > 0) {
										stickorename = OreDictionary.getOreName(stickoreids[0]);
									}
								}
								
								// head processing
								ItemStack[] head = {
									comp[0], 
									comp[1], 
									comp[2]
								};
								
								String orename = "";
								List<ItemStack> candidates = new ArrayList<ItemStack>();
								
								for(int i=0; i<3; i++) {
									ItemStack h = head[i];
									int[] oreids = OreDictionary.getOreIDs(h);
									if (oreids.length > 0) {
										candidates.add(h);
									}
								}
								
								ItemStack oreitem = null;
								if (candidates.size() == 0) {
									continue;
								} else { 
									if (candidates.contains(head[1])) {
										if (candidates.size() == 3) {
											if (compareStacks(head[0], head[2])) {
												oreitem = head[0];
											} else {
												oreitem = head[1];
											}
										}
									} else {
										oreitem = candidates.get(0);
									}
								}
								
								if (oreitem != null) {
									orename = OreDictionary.getOreName(OreDictionary.getOreIDs(oreitem)[0]);
								} else {
									continue;
								}
								
								if(materials.containsKey(orename)) {
									if (materials.get(orename).stickorename == null && stickorename != null) {
										materials.remove(orename);
									}
								}
								if(!materials.containsKey(orename)) {
									materials.put(orename, new PhysisToolMaterial(orename, oreitem, stickorename, stickitem, output, recipe));
								}
							}
						}
					}
				}
			}
		}
		generated = true;
		
		//Physis.logger.info("Finished tool material list");
	}
	
	public static void registerRecipeListGetter(RecipeListGetter list) {
		recipeLists.add(list);
	}
	
	public static IRecipeComponentTranslator getTranslatorForRecipe(Object recipe) {
		for(RecipeListGetter list : recipeLists) {
			IRecipeComponentTranslator t = getTranslatorForRecipe(list, recipe);
			if (t != null) {
				return t;
			}
		}
		return null;
	}
	
	public static IRecipeComponentTranslator getTranslatorForRecipe(RecipeListGetter list, Object recipe) {
		for(Entry<Class<?>, IRecipeComponentTranslator> entry : list.translators.entrySet()) {
			if (entry.getKey().isInstance(recipe)) {
				return entry.getValue();
			}
		}
		return null;
	}
	
	public static IRecipeComponentTranslator getTranslatorForMaterial(PhysisToolMaterial mat) {
		return getTranslatorForRecipe(mat.sourceRecipe);
	}
	
	public static void addRecipeComponentTranslator(Class<?> clazz, IRecipeComponentTranslator trans) {
		addRecipeComponentTranslator(defaultRecipeList, clazz, trans);
	}
	
	public static void addRecipeComponentTranslator(RecipeListGetter list, Class<?> clazz, IRecipeComponentTranslator trans) {
		if (!list.translators.containsKey(clazz)) {
			list.translators.put(clazz, trans);
		}
	}
	
	public void registerRecipe(ItemStack output, Object... inputs) {
		IRecipeComponentTranslator translator = getTranslatorForMaterial(this);
		translator.registerRecipe(this.sourceRecipe, output, inputs);
	}
	
	@SuppressWarnings("unchecked")
	public static ItemStack getRecipeCompStack(Object o) {
		if (o instanceof ItemStack) { return (ItemStack)o; }
		if (o instanceof List) { return ((List<ItemStack>)o).get(0); }
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	public static void buildTintData() {
		defaultTints = new int[TINTS];
		for (int i=0; i<TINTS; i++) {
			int c = (int)((i/(double)(TINTS)) * 255);
			defaultTints[i] = TextureHelper.compose(c, c, c, 255);
		}
		
		for(Entry<String, PhysisToolMaterial> entry : materials.entrySet()) { 
			PhysisToolMaterial mat = entry.getValue();
			
			buildTintData(mat);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static void buildTintData(PhysisToolMaterial mat){
		if (!mat.hastint) {
			try {
				BufferedImage matimage = TextureHelper.getItemStackImage(mat.ingot);
				List<Integer> colours = TextureHelper.getImageColourRange(matimage);
				
				if (colours.isEmpty()) {
					// something seriously wrong if this happens... unable to get image back out of graphics memory?
					mat.hastint = true;
					mat.tints = defaultTints.clone();
					return;
				}
				
				mat.tints = new int[TINTS];
				
				mat.tints[0] = colours.remove(0);
				mat.tints[TINTS-1] = colours.remove(colours.size()-1);
				
				int avetints = TINTS-2;
				float dper = colours.size() / (float)avetints;
				
				for(int i=0; i<avetints; i++) {
					int lower = Math.round(dper*i);
					int upper = Math.round(dper*(i+1));
					
					int tint = TextureHelper.getAverageColour(colours.subList(lower, upper));
					mat.tints[i+1] = tint;
				}
				
				BufferedImage stickimage = TextureHelper.getItemStackImage(mat.stick);
				mat.shafttint = TextureHelper.getAverageColour(TextureHelper.getImageColourRange(stickimage));
				
				mat.hastint = true;
			}
			catch(Exception e) {
				Physis.logger.warn("Failed to generate tint data for "+mat.getMaterialName(), e);
				mat.hastint = false;
			}
		}
	}
	
	public static boolean compareStacks(ItemStack stack1, ItemStack stack2) {
		if (stack1 != null || stack2 != null)
        {
            if (stack2 == null && stack1 != null || stack2 != null && stack1 == null)
            {
                return false;
            }

            if (stack1.getItem() != stack2.getItem())
            {
                return false;
            }

            if (stack1.getItemDamage() != 32767 && stack1.getItemDamage() != stack2.getItemDamage())
            {
                return false;
            }
        }
		return true;
	}
	
	public static PhysisToolMaterial getMaterialFromItemStack(ItemStack stack) {
		if (stack.stackTagCompound != null) {
			if(stack.stackTagCompound.hasKey(MATERIALTAG)) {
				String matname = stack.stackTagCompound.getString(MATERIALTAG);
				if (materials.containsKey(matname)) {
					return materials.get(matname);
				}
			}
		}
		return null;
	}
	
	public static void writeMaterialToStack(PhysisToolMaterial mat, ItemStack stack) {
		if (stack.stackTagCompound == null) {
			stack.stackTagCompound = new NBTTagCompound();
		}
		
		stack.stackTagCompound.setString(MATERIALTAG, mat.orename);
	}
	
	public static PhysisToolMaterial getMaterialById(int id) {
		return materialsById.get(id);
	}
	
	public static PhysisToolMaterial getRandomMaterial(Random rand) {
		return materialsById.get(rand.nextInt(materialsById.size()));
	}
	
	public static void initDefaultTranslators() {
		defaultRecipeList = new RecipeListGetter() {
			@Override
			public Iterator<?> getIterator() {
				return CraftingManager.getInstance().getRecipeList().iterator();
			}
		};
		registerRecipeListGetter(defaultRecipeList);
		
		PhysisToolMaterial.addRecipeComponentTranslator(ShapedRecipes.class, new ShapedRecipeCT());
		PhysisToolMaterial.addRecipeComponentTranslator(ShapedOreRecipe.class, new ShapedOreRecipeCT());
	}
}

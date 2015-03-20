package ttftcuts.physis.common.item.material;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.ImmutableList;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import ttftcuts.physis.Physis;
import ttftcuts.physis.client.render.item.RenderSocketed;
import ttftcuts.physis.common.helper.TextureHelper;
import ttftcuts.physis.utils.ModFinder;

import net.minecraft.item.Item;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class PhysisToolMaterial {
	
	public static final String MATERIALTAG = "physisMaterial";
	// list of ore dict names considered vanilla - will not consider mod items when getting colours
	public static final ImmutableList<String> vanillaOreNames = ImmutableList.of("logWood", "plankWood", "ingotIron", "ingotGold", "gemDiamond", "gemEmerald", "gemQuartz", "blockQuartz", "stone", "cobblestone", "sandstone", "blockGlass");
	// list of mods excluded from colour calculation because of issues (e.g. GT items come up as grey)
	public static final ImmutableList<String> excludedMods = ImmutableList.of("gregtech");
	
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
	
	public ItemTool pickitem;
	
	public Item.ToolMaterial toolmaterial;
	
	public int maxdamage;
	
	public boolean hastint = false;
	public Map<ItemStack, int[]> intermediateTints = new HashMap<ItemStack, int[]>();
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
		this.pickitem = (ItemTool)(pick.getItem());
		
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
		
		List<ItemTool> picks = new ArrayList<ItemTool>();
		
		Iterator<Item> ir = Item.itemRegistry.iterator();
		
		String pickclass = "pickaxe";
		
		while(ir.hasNext()) {
			Item item = ir.next();
			
			if (item instanceof ItemTool) {
				ItemTool tool = (ItemTool)item;
				//Physis.logger.info("Tool: "+tool.getUnlocalizedName());
				
				ItemStack toolstack = new ItemStack(tool);
				Set<String> toolclasses = tool.getToolClasses(toolstack);
				
				//Physis.logger.info(toolclasses);
				
				if (toolclasses.contains(pickclass)) {
					picks.add(tool);
					//Physis.logger.info("Pick: "+tool.getUnlocalizedName());
				}
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
				
				for(ItemTool pick : picks) {
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
		RenderSocketed.drawSocketIcon = false;
		if (!mat.hastint) {
			try {
				List<ItemStack> ingots = OreDictionary.getOres(mat.orename);
				boolean vanilla = vanillaOreNames.contains(mat.orename);
				
				boolean present = true;
				for (ItemStack stack : ingots) {
					if (stack == null || stack.getItem() == null) {
						continue;
					}
					if (vanilla) {
						if (!ModFinder.isVanilla(stack.getItem())) {
							// this is a vanilla ore name and the item isn't vanilla
							continue;
						}
					}
					String mod = ModFinder.idFromObject(stack.getItem());
					if (excludedMods.contains(mod)) {
						continue;
					}
					if (!mat.intermediateTints.containsKey(stack) || mat.intermediateTints.get(stack) == null) {
						BufferedImage matimage = TextureHelper.getItemStackImage(stack);
						List<Integer> colours = TextureHelper.getImageColourRange(matimage);
						if (colours.isEmpty()) {
							mat.intermediateTints.put(stack, defaultTints.clone());
							continue;
						}
						int[] mattints = processColourList(colours);
						mat.intermediateTints.put(stack, mattints);
						if( mattints == null) { present = false; }
					}
				}
				
				// if not all of the tints are there, don't bother.
				if(!present) { return; }
				
				// canonical colour
				BufferedImage matimage = TextureHelper.getItemStackImage(mat.ingot);
				List<Integer> colours = TextureHelper.getImageColourRange(matimage);
				if (colours.isEmpty()) {
					//no canonical tints, bail
					Physis.logger.warn("No valid colours for material "+mat.getMaterialName()+", using defaults");
					mat.tints = defaultTints.clone();
					mat.hastint = true;
					return;
				}
				int canonical = TextureHelper.getAverageColour(colours);
				double[] canonicalhsl = TextureHelper.rgb2hsl(canonical);
				
				// work out the best tint set
				List<TintInfo> tintinfos = new ArrayList<TintInfo>();
				for (Entry<ItemStack, int[]> entry : mat.intermediateTints.entrySet()) {
					TintInfo info = mat.new TintInfo(entry.getValue(), canonicalhsl, entry.getKey().getDisplayName());
					tintinfos.add(info);
				}
				if (tintinfos.isEmpty()) {
					//no tints, bail
					Physis.logger.warn("No valid colours for material "+mat.getMaterialName()+", using defaults");
					mat.tints = defaultTints.clone();
					mat.hastint = true;
					return;
				}
				
				Collections.sort(tintinfos);
				
				/*Physis.logger.info("Tint prettiness: "+mat.orename+", canonical stack: "+mat.ingot.getDisplayName());
				for (TintInfo i : tintinfos) {
					Physis.logger.info(i.name+": "+i.prettiness);
				}*/
				
				mat.tints = tintinfos.get(0).tints;
				
				BufferedImage stickimage = TextureHelper.getItemStackImage(mat.stick);
				mat.shafttint = TextureHelper.getAverageColour(TextureHelper.getImageColourRange(stickimage));
				
				mat.hastint = true;
			}
			catch(Exception e) {
				Physis.logger.warn("Failed to generate tint data for "+mat.getMaterialName()+", will retry.");
				mat.hastint = false;
			}
		}
		RenderSocketed.drawSocketIcon = true;
	}
	
	private static int[] processColourList(List<Integer> colours) {
		int[] tint = new int[TINTS];
		
		tint[0] = colours.remove(0);
		tint[TINTS-1] = colours.remove(colours.size()-1);
		
		// If this is the case... we've got some MISSING TEXTURE FUN
		if ((tint[0] == 0xFFF800F8 && tint[TINTS-1] == 0xFF000000)|| (tint[0] == 0xFF000000 && tint[TINTS-1] == 0xFFF800F8)) {
			return null;
		}
		
		int avetints = TINTS-2;
		float dper = colours.size() / (float)avetints;
		
		for(int i=0; i<avetints; i++) {
			int lower = Math.round(dper*i);
			int upper = Math.round(dper*(i+1));
			
			int t = TextureHelper.getAverageColour(colours.subList(lower, upper));
			tint[i+1] = t;
		}
		
		return tint;
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
	
	private class TintInfo implements Comparable<TintInfo> {
		public double prettiness = 0;
		int[] tints;
		@SuppressWarnings("unused")
		String name;
		
		public TintInfo(int[] tints, double[] canonicalhsl, String name) {
			this.tints = tints;
			this.name = name;
			
			int dark = tints[0];
			int light = tints[TINTS-1];
			
			int median = tints[(int)Math.floor(TINTS/2.0)];
			
			int darkbrightness = TextureHelper.getPerceptualBrightness(dark);
			int medbrightness = TextureHelper.getPerceptualBrightness(median);
			int lightbrightness = TextureHelper.getPerceptualBrightness(light);
			
			int spread = Math.abs(lightbrightness - darkbrightness);
			int dmdiff = Math.abs(medbrightness - darkbrightness);
			int lmdiff = Math.abs(lightbrightness - medbrightness);
			
			double centredness = (255 - Math.abs(dmdiff - lmdiff))/255.0;
			
			// hsl of median colour
			double[] medhsl = TextureHelper.rgb2hsl(median);
			double ldiff = Math.abs(medhsl[2] - canonicalhsl[2]);
			
			// hue difference, switching to the other way if big
			double hdiff = Math.abs(medhsl[0] - canonicalhsl[0]);
			if (hdiff > 0.5) { hdiff = 1.0 - hdiff; }
			
			// average saturation times difference in hue times half of 1-light difference
			//double colourdiff = (medhsl[1] + canonicalhsl[1]) * 0.5 * hdiff * (0.5 + (1-ldiff)*0.5);
			double colourdiff = hdiff * 2.0 * (0.5 + (1-ldiff)*0.5);
			
			this.prettiness = centredness * (0.5 +spread*0.5) * (1.0-colourdiff);//(0.5 + colourdiff*0.5);
			
			//Physis.logger.info(name+" canonical: "+Arrays.toString(canonicalhsl)+", median: "+Arrays.toString(medhsl)+", colour diff: "+ colourdiff);
		}
		
		@Override
		public int compareTo(TintInfo other) {
			return (int)Math.signum(other.prettiness - this.prettiness);
		}
	}
}

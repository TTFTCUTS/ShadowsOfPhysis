package ttftcuts.physis.common.item.material;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import ttftcuts.physis.common.helper.TextureHelper;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

public class PhysisToolMaterial {
	
	public static final String MATERIALTAG = "physisMaterial";
	
	public static boolean generateTextures = OpenGlHelper.isFramebufferEnabled();
	
	public static Map<String,PhysisToolMaterial> materials;
	public static Map<Class<? extends IRecipe>, IRecipeComponentTranslator> handlers = new HashMap<Class<? extends IRecipe>, IRecipeComponentTranslator>();
	
	public static boolean generated = false;
	
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
	
	public PhysisToolMaterial(String orename, ItemStack ingot, String stickorename, ItemStack stick, ItemStack pick) {
		//Physis.logger.info("Registering material for "+orename+" with ingot "+ingot+", stick "+stick+" and pick "+pick);
		
		this.orename = orename;
		this.orematerial = orename.replaceFirst("[^A-Z]*(?=[A-Z])", "");
		this.ingot = ingot;
		this.stick = stick;
		this.stickorename = stickorename;
		this.pick = pick;
		this.pickitem = (ItemPickaxe)(pick.getItem());
		
		this.toolmaterial = this.pickitem.func_150913_i();
		
		this.maxdamage = this.toolmaterial.getMaxUses();
	}
	
	public String getMaterialName() {
		return this.orematerial;
	}
	
	@SuppressWarnings("unchecked")
	public static void buildMaterials() {		
		//Physis.logger.info("Building tool material list");
		materials = new HashMap<String, PhysisToolMaterial>();
		
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
		CraftingManager craft = CraftingManager.getInstance();
		
		List<IRecipe> recipes = craft.getRecipeList();
		
		for(IRecipe recipe : recipes) {
			if (recipe.getRecipeOutput() == null || recipe.getRecipeOutput().getItem() == null) {
				continue;
			}
			ItemStack output = recipe.getRecipeOutput();
			Item out = output.getItem();
			
			for(ItemPickaxe pick : picks) {
				if (out == pick) {
					ItemStack[] comp = null;
					boolean stickore = false;
					for(Entry<Class<? extends IRecipe>, IRecipeComponentTranslator> entry : handlers.entrySet()) {
						if (entry.getKey().isInstance(recipe)) {
							IRecipeComponentTranslator trans = entry.getValue();
							comp = trans.getRecipeComponents(recipe);
							stickore = trans.hasOreDictStick();
							break;
						}
					}
					
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
								materials.put(orename, new PhysisToolMaterial(orename, oreitem, stickorename, stickitem, output));
							}
						}
					}
				}
			}
		}
		generated = true;
		
		//Physis.logger.info("Finished tool material list");
	}
	
	public static void addRecipeComponentTranslator(Class<? extends IRecipe> clazz, IRecipeComponentTranslator trans) {
		if (!handlers.containsKey(clazz)) {
			handlers.put(clazz, trans);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static ItemStack getRecipeCompStack(Object o) {
		if (o instanceof ItemStack) { return (ItemStack)o; }
		if (o instanceof List) { return ((List<ItemStack>)o).get(0); }
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	public static void buildTintData(int tintcount) {
		for(Entry<String, PhysisToolMaterial> entry : materials.entrySet()) { 
			PhysisToolMaterial mat = entry.getValue();
			
			if (!mat.hastint) {
				mat.tints = new int[tintcount];
				
				BufferedImage matimage = TextureHelper.getItemStackImage(mat.ingot);
				List<Integer> colours = TextureHelper.getImageColourRange(matimage);
				
				mat.tints[0] = colours.remove(0);
				mat.tints[tintcount-1] = colours.remove(colours.size()-1);
				
				int avetints = tintcount-2;
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
}

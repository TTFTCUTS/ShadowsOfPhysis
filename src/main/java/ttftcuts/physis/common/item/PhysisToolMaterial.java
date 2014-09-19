package ttftcuts.physis.common.item;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import ttftcuts.physis.Physis;
import ttftcuts.physis.common.helper.TextureHelper;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class PhysisToolMaterial {
	
	public static final String MATERIALTAG = "physisMaterial";
	
	public static boolean generateTextures = OpenGlHelper.isFramebufferEnabled();
	
	public static Map<String,PhysisToolMaterial> materials;
	
	public static boolean generated = false;
	
	public String orename;
	public String orematerial;
	
	public ItemStack ingot;
	public ItemStack stick;
	public ItemStack pick;
	
	public ItemPickaxe pickitem;
	
	public Item.ToolMaterial toolmaterial;
	
	public int maxdamage;
	
	public boolean hastint = false;
	public int[] tints;
	public int shafttint;
	
	public PhysisToolMaterial(String orename, ItemStack ingot, ItemStack stick, ItemStack pick) {
		//Physis.logger.info("Registering material for "+orename+" with ingot "+ingot+", stick "+stick+" and pick "+pick);
		
		this.orename = orename;
		this.orematerial = orename.replaceFirst("[^A-Z]*(?=[A-Z])", "");
		this.ingot = ingot;
		this.stick = stick;
		this.pick = pick;
		this.pickitem = (ItemPickaxe)(pick.getItem());
		
		this.toolmaterial = Item.ToolMaterial.valueOf(pickitem.getToolMaterialName());
		
		this.maxdamage = this.toolmaterial.getMaxUses();
	}
	
	public String getMaterialName() {
		return this.orematerial;
	}
	
	@SuppressWarnings("unchecked")
	public static void buildMaterials() {		
		Physis.logger.info("Building tool material list");
		materials = new HashMap<String, PhysisToolMaterial>();
		
		//Physis.logger.info("Getting Ingots");
		
		String[] orenames = OreDictionary.getOreNames();
		
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
					//Physis.logger.info("Recipe for "+pick.getUnlocalizedName());
					if (recipe instanceof ShapedOreRecipe) {
						ShapedOreRecipe r = (ShapedOreRecipe)recipe;
						
						Object[] comp = r.getInput();
						
						if (comp[0] != null && comp[1] != null && comp[2] != null && comp[4] != null && comp[7] != null) {
							// looks pick shaped to me!
							ItemStack stickitem = getRecipeCompStack(comp[4]);
							ItemStack otherstick = getRecipeCompStack(comp[7]);
							if (!stickitem.equals(otherstick)) {
								// but the sticks don't match
								continue;
							}
							
							ItemStack[] head = {
								getRecipeCompStack(comp[0]), 
								getRecipeCompStack(comp[1]), 
								getRecipeCompStack(comp[2])
							};
							
							String orename = "";
							int ingotid = -1;
							
							headloop:
							for(int i=0; i<3; i++) {
								ItemStack h = head[i];
								int[] oreids = OreDictionary.getOreIDs(h);
								for(int j=0; j<orenames.length; j++) {
									for (int k=0; k<oreids.length; k++) {
										if (OreDictionary.getOreName(oreids[k]).equals(orenames[j])) {
											// match!
											//Physis.logger.info("Matched component "+h+" to oredict: "+orenames[j]);
											orename = orenames[j];
											ingotid = i;
											break headloop;
										}
									}
								}
							}
							
							if (orename.isEmpty()) {
								continue;
							}
							
							if(!materials.containsKey(orename)) {
								materials.put(orename, new PhysisToolMaterial(orename, head[ingotid], stickitem, output));
							}
						}
					}
				}
			}
		}
		generated = true;
		
		//Physis.logger.info("Finished tool material list");
	}
	
	@SuppressWarnings("unchecked")
	private static ItemStack getRecipeCompStack(Object o) {
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

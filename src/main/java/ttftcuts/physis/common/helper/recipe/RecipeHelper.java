package ttftcuts.physis.common.helper.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeHelper {

	public static List<RecipeListGetter> recipeLists = new ArrayList<RecipeListGetter>();
	public static RecipeListGetter defaultRecipeList;
	
	private static Map<ItemStackWrapper, Map<Class<?>, List<Object>>> registry;
	
	public static List<Object> getRecipes(ItemStack stack, Class<?> clazz) {
		ItemStackWrapper wrap = new ItemStackWrapper(stack);
		
		Map<Class<?>, List<Object>> entry = registry.get(wrap);
		if (entry == null) {
			return null;
		}
		
		List<Object> recipes = entry.get(clazz);
		if (recipes == null || recipes.size() == 0) {
			return null;
		}
		return recipes;
	}
	
	public static Object getRecipe(ItemStack stack, Class<?> clazz, int index) {
		List<Object> recipes = getRecipes(stack, clazz);
		
		if (recipes == null || index >= recipes.size()) {
			return null;
		}
		return recipes.get(index);
	}
	public static Object getRecipe(ItemStack stack, Class<?> clazz) {
		return getRecipe(stack, clazz, 0);
	}
	
	public static List<Object> getRecipes(ItemStack stack) {
		ItemStackWrapper wrap = new ItemStackWrapper(stack);
		
		Map<Class<?>, List<Object>> entry = registry.get(wrap);
		if (entry == null) {
			return null;
		}
		
		List<Object> recipes = new ArrayList<Object>();
		
		for (List<Object> rlist : entry.values()) {
			recipes.addAll(rlist);
		}
		
		return recipes;
	}
	
	public static Object getRecipe(ItemStack stack, int index) {
		List<Object> recipes = getRecipes(stack);
		
		if (recipes == null || index >= recipes.size()) {
			return null;
		}
		return recipes.get(index);
	}
	public static Object getRecipe(ItemStack stack) {
		return getRecipe(stack, 0);
	}
	
	// parse recipes
	
	public static void parseRecipes() {
		registry = new HashMap<ItemStackWrapper, Map<Class<?>, List<Object>>>();
		
		for (RecipeListGetter list : recipeLists) {
			Iterator<?> iter = list.getIterator();
			
			while(iter.hasNext()) {
				Object recipe = iter.next();
				
				IRecipeComponentTranslator translator = getTranslatorForRecipe(list, recipe);
				
				if (translator == null || translator.getRecipeOutput(recipe) == null || translator.getRecipeOutput(recipe).getItem() == null) {
					continue;
				}
				
				ItemStack output = translator.getRecipeOutput(recipe);
				
				ItemStackWrapper wrap = new ItemStackWrapper(output);
				
				Map<Class<?>, List<Object>> entry = registry.get(wrap);
				if (entry == null) {
					entry = new HashMap<Class<?>, List<Object>>();
					registry.put(wrap, entry);
				}
				
				List<Object> recipes = entry.get(recipe.getClass());
				if (recipes == null) {
					recipes = new ArrayList<Object>();
					entry.put(recipe.getClass(), recipes);
				}
				
				if (!recipes.contains(recipe)) {
					recipes.add(recipe);
				}
			}
		}
	}
	
	
	// recipe lists and translators
	
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
	
	public static void addRecipeComponentTranslator(Class<?> clazz, IRecipeComponentTranslator trans) {
		addRecipeComponentTranslator(defaultRecipeList, clazz, trans);
	}
	
	public static void addRecipeComponentTranslator(RecipeListGetter list, Class<?> clazz, IRecipeComponentTranslator trans) {
		if (!list.translators.containsKey(clazz)) {
			list.translators.put(clazz, trans);
		}
	}
	
	public static void initDefaultTranslators() {
		defaultRecipeList = new RecipeListGetter() {
			@Override
			public Iterator<?> getIterator() {
				return CraftingManager.getInstance().getRecipeList().listIterator();
			}
		};
		registerRecipeListGetter(defaultRecipeList);
		
		addRecipeComponentTranslator(ShapedRecipes.class, new ShapedRecipeCT());
		addRecipeComponentTranslator(ShapedOreRecipe.class, new ShapedOreRecipeCT());
	}
}

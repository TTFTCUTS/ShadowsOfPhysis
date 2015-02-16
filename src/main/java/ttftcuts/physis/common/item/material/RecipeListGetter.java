package ttftcuts.physis.common.item.material;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class RecipeListGetter {
	public Map<Class<?>, IRecipeComponentTranslator> translators;
	
	public RecipeListGetter() {
		this.translators = new HashMap<Class<?>, IRecipeComponentTranslator>();
	}
	
	public abstract Iterator<?> getIterator();
}

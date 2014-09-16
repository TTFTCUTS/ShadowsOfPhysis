package ttftcuts.physis.common.item;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import ttftcuts.physis.Physis;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemTrowel extends ItemPhysis {
	
	public ItemTrowel() {
		super();
		this.maxStackSize = 1;
		this.setUnlocalizedName("trowel");
		this.setTextureName(Physis.MOD_ID+":trowel");
		this.setMaxDamage(5);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List types) {
		Iterator<Entry<String,PhysisToolMaterial>> iter = PhysisToolMaterial.materials.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, PhysisToolMaterial> entry = iter.next();
			PhysisToolMaterial mat = entry.getValue();
			ItemStack stack = new ItemStack(this, 1, 0);
			PhysisToolMaterial.writeMaterialToStack(mat, stack);

			types.add(stack);
		}
	}
	
	@Override
	public int getMaxDamage(ItemStack stack) {
		if (stack.getItem() == this) {
			PhysisToolMaterial mat = PhysisToolMaterial.getMaterialFromItemStack(stack);
			if (mat != null) {
				return mat.maxdamage;
			}
		}
		return this.getMaxDamage();
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		if (stack.getItem() == this) {
			PhysisToolMaterial mat = PhysisToolMaterial.getMaterialFromItemStack(stack);
			if (mat != null) {
				return this.getUnlocalizedName() + mat.getMaterialName();
			}
		}
		return this.getUnlocalizedName();
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		stack.damageItem(1, player);
		return stack;
	}
}

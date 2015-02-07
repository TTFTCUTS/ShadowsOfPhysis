package ttftcuts.physis.common.crafting;

import ttftcuts.physis.common.PhysisItems;
import ttftcuts.physis.common.artifact.PhysisArtifacts;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class AddSocketRecipe implements IRecipe {

	private Item socketItem = PhysisItems.addsocket;
	
	@Override
	public boolean matches(InventoryCrafting grid, World world) {
		ItemStack toSocket = null;
		boolean foundSocket = false;
		
		for (int i=0; i<grid.getSizeInventory(); i++) {
			ItemStack stack = grid.getStackInSlot(i);
			
			if (stack != null) {
				Item item = stack.getItem();
				
				if (item == socketItem) {
					foundSocket = true;
				} else {
					if (toSocket == null && checkItem(stack)) {
						toSocket = stack;
					} else {
						return false;
					}
				}
			}
		}
		
		if(foundSocket && toSocket != null && toSocket.stackSize == 1) {
			int socketCount = PhysisArtifacts.getSocketCount(toSocket);
			
			if (socketCount < 2) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting grid) {
		ItemStack toSocket = null;
		
		for (int i=0; i<grid.getSizeInventory(); i++) {
			ItemStack stack = grid.getStackInSlot(i);
			
			if (stack != null && checkItem(stack)) {
				toSocket = stack;
			}
		}
		
		if (toSocket == null || toSocket.stackSize != 1) {
			return null;
		}
		
		ItemStack copy = toSocket.copy();
		PhysisArtifacts.addSocketToItem(copy);
		return copy;
	}
	
	private boolean checkItem(ItemStack stack) {
		if (stack.stackSize != 1) { return false; }
		return PhysisArtifacts.canItemAcceptSockets(stack);
	}

	@Override
	public int getRecipeSize() {
		return 10;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return null;
	}

}

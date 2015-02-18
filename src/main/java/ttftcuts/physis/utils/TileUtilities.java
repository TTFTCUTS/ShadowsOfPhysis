package ttftcuts.physis.utils;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class TileUtilities {
	private static Random dropRand = new Random();
	
	public static void dropItemInWorld(World world, ItemStack stack, double x, double y, double z) {
		if (world.isRemote) { return; }
        if (stack != null)
        {
            float f = dropRand.nextFloat() * 0.8F + 0.1F;
            float f1 = dropRand.nextFloat() * 0.8F + 0.1F;
            EntityItem entityitem;

            for (float f2 = dropRand.nextFloat() * 0.8F + 0.1F; stack.stackSize > 0; world.spawnEntityInWorld(entityitem))
            {
                int j1 = dropRand.nextInt(21) + 10;

                if (j1 > stack.stackSize)
                {
                    j1 = stack.stackSize;
                }

                stack.stackSize -= j1;
                entityitem = new EntityItem(world, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(stack.getItem(), j1, stack.getItemDamage()));
                float f3 = 0.05F;
                entityitem.motionX = (double)((float)dropRand.nextGaussian() * f3);
                entityitem.motionY = (double)((float)dropRand.nextGaussian() * f3 + 0.2F);
                entityitem.motionZ = (double)((float)dropRand.nextGaussian() * f3);

                if (stack.hasTagCompound())
                {
                    entityitem.getEntityItem().setTagCompound((NBTTagCompound)stack.getTagCompound().copy());
                }
            }
        }
	}
	
	public static void dropItemsInWorld(World world, List<ItemStack> stacks, int x, int y, int z) {
		for (ItemStack stack : stacks) {
			dropItemInWorld(world, stack, x,y,z);
		}
	}
}

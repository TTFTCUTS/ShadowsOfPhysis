package ttftcuts.physis.common.block;

import java.util.List;

import ttftcuts.physis.Physis;
import ttftcuts.physis.common.artifact.PhysisArtifacts;
import ttftcuts.physis.common.handler.TooltipHandler;
import ttftcuts.physis.common.item.block.IBlockTooltip;
import ttftcuts.physis.utils.Socket;
import ttftcuts.physis.utils.SocketIterator;
import net.minecraft.block.BlockJukebox;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockSocketJukebox extends BlockJukebox implements IBlockTooltip {

	public BlockSocketJukebox() {
		super();
		this.setCreativeTab(Physis.creativeTab);
		this.setHardness(2.0F);
		this.setResistance(10.0F);
		this.setStepSound(soundTypePiston);
		this.setBlockName("jukebox");
		this.setBlockTextureName("jukebox");
	}
	
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntitySocketJukebox();
    }
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float px, float py, float pz)
	{
		boolean ejected = super.onBlockActivated(world, x, y, z, player, side, px, py, pz);
		
		if (!ejected && player != null && player.getHeldItem() != null && (player.getHeldItem().getItem() instanceof ItemRecord)) {
			ItemStack held = player.getHeldItem();
			
			this.func_149926_b(world, x, y, z, held);
            world.playAuxSFXAtEntity((EntityPlayer)null, 1005, x, y, z, Item.getIdFromItem(held.getItem()));
            --held.stackSize;
			
			return true;
		}
		
		return false;
	}
	
	// ------------------------------------------
	
	public class TileEntitySocketJukebox extends BlockJukebox.TileEntityJukebox {
		public TileEntitySocketJukebox() {
			super();
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public void updateEntity()
	    {
			ItemStack record = this.func_145856_a();
			
			if (record != null) {
				if (PhysisArtifacts.getSocketCount(record) > 0) {
				
					double dx = xCoord + 0.5;
					double dy = yCoord + 0.5;
					double dz = zCoord + 0.5;
					
					// +.5 for getting to the edge of the block
					double range = 12.5;
					
					AxisAlignedBB area = AxisAlignedBB.getBoundingBox(dx - range, dy - range, dz - range, dx + range, dy + range, dz + range);
					List<EntityLivingBase> entities = this.worldObj.selectEntitiesWithinAABB(EntityLivingBase.class, area, IEntitySelector.selectAnything);
					
					// filter out entities outside sphere range
					for (EntityLivingBase e : entities) {
						double xdiff = e.posX - dx;
						double ydiff = e.posY - dy;
						double zdiff = e.posZ - dz;
						if (Math.sqrt(xdiff*xdiff + ydiff*ydiff + zdiff*zdiff) > range) {
							entities.remove(e);
						}
					}
					
					// apply effects to the list
					for(Socket socket : SocketIterator.triggers(record)) {
						if (socket.trigger != null) {
							socket.trigger.onTileUpdate(record, entities, this, socket.slot);
						}
					}
				}
			}
	    }
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void AddTooltipInformation(ItemStack stack, EntityPlayer player,	List list, boolean par4) {
		List<String> tip = Physis.text.translateAndWrap("tile.physis:jukebox.tooltip", TooltipHandler.tipWidth);
		if (list.size() == 0) {
			list.add("");
		}
		list.addAll(tip);
	}
}

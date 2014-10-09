package ttftcuts.physis.common.handler;

import ttftcuts.physis.client.gui.*;
import ttftcuts.physis.client.gui.journal.PageDefs;
import ttftcuts.physis.common.block.tile.TileEntitySocketTable;
import ttftcuts.physis.common.container.ContainerSocketTable;
import ttftcuts.physis.common.container.ContainerSocketTableDrawer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,	int x, int y, int z) {
		final PhysisGuis gui = PhysisGuis.fromId(ID);
		switch(gui) {
			case SOCKET_TABLE:
				TileEntitySocketTable table = (TileEntitySocketTable) world.getTileEntity(x, y, z);
				return new ContainerSocketTable(player.inventory, table);
				
			case SOCKET_TABLE_DRAWER:
				TileEntitySocketTable tabled = (TileEntitySocketTable) world.getTileEntity(x, y, z);
				return new ContainerSocketTableDrawer(player.inventory, tabled);
		
			default: return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,	int x, int y, int z) {
		final PhysisGuis gui = PhysisGuis.fromId(ID);
		switch(gui) {
			case JOURNAL: return new GuiJournal(PageDefs.index);
			
			case SOCKET_TABLE:
				TileEntitySocketTable table = (TileEntitySocketTable) world.getTileEntity(x, y, z);
				return new GuiSocketTable(player.inventory, table);
				
			case SOCKET_TABLE_DRAWER:
				TileEntitySocketTable tabled = (TileEntitySocketTable) world.getTileEntity(x, y, z);
				return new GuiSocketTableDrawer(player.inventory, tabled);
			
			default: return null;
		}
	}

}

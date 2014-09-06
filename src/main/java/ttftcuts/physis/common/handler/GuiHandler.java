package ttftcuts.physis.common.handler;

import ttftcuts.physis.client.gui.*;
import ttftcuts.physis.client.gui.journal.PageDefs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		switch(ID) {
			case 0: return new GuiJournal(PageDefs.index);
			default: return null;
		}
	}

}

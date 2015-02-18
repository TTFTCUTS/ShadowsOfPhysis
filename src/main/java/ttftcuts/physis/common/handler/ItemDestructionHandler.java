package ttftcuts.physis.common.handler;

import java.util.Random;

import ttftcuts.physis.common.artifact.PhysisArtifacts;
import ttftcuts.physis.utils.Socket;
import ttftcuts.physis.utils.SocketIterator;
import ttftcuts.physis.utils.TileUtilities;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;

public class ItemDestructionHandler {
	
	@SubscribeEvent
	public void OnItemDestruction(PlayerDestroyItemEvent event) {
		if (event.entity.worldObj.isRemote) { return; }
		
		Random rand = event.entityPlayer.getRNG();
		
		ItemStack stack = event.original;
		EntityPlayer player = event.entityPlayer;
		if (PhysisArtifacts.canItemAcceptSockets(stack)) {
			for(Socket socket : SocketIterator.triggers(stack)) {
				if(socket.tag != null) {
					if (rand.nextDouble() < 0.75) {
						ItemStack socketable = ItemStack.loadItemStackFromNBT(socket.tag);
						
						TileUtilities.dropItemInWorld(event.entityPlayer.worldObj, socketable, player.posX, player.posY - 0.3 + player.getEyeHeight(), player.posZ);
					}
				}
			}
		}
	}
}

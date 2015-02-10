package ttftcuts.physis.common.handler;

import java.util.List;

import ttftcuts.physis.Physis;
import ttftcuts.physis.api.artifact.IArtifactTrigger;
import ttftcuts.physis.api.artifact.ISocketable;
import ttftcuts.physis.common.PhysisItems;
import ttftcuts.physis.common.artifact.PhysisArtifacts;
import ttftcuts.physis.common.helper.InputHelper;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class TooltipHandler {

	public static final int tipWidth = 200;
	
	@SubscribeEvent
	public void onTooltip(ItemTooltipEvent event) {
		if (event.itemStack.stackTagCompound != null){
			if (event.itemStack.stackTagCompound.hasKey(PhysisArtifacts.SOCKETEDTAG)) {
				int index = -1;
				for(int i=0; i<event.toolTip.size(); i++) {
					if (event.toolTip.get(i).isEmpty()) {
						index = i;
					}
				}
				
				if (index != -1) {
					addSocketTooltip(event.entityPlayer, event.itemStack, event.toolTip, index);
				} else {
					addSocketTooltip(event.entityPlayer, event.itemStack, event.toolTip, event.toolTip.size());
				}
			}
			
			if (event.itemStack.stackTagCompound.hasKey(PhysisArtifacts.ARTIFACTTAG)) {
				List<String> content = Physis.text.translateAndWrap(this.getTooltipForSocketable(event.itemStack), tipWidth);
				event.toolTip.addAll(content);
			}
		} else if (event.itemStack.getItem() instanceof ItemRecord) {
			ItemStack held = event.entityPlayer.getHeldItem();
			if (held != null && held.getItem() == PhysisItems.addsocket) {
				addRecordTooltip(event.itemStack, event.toolTip, event.toolTip.size());
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void addSocketTooltip(EntityPlayer player, ItemStack stack, List<String> lines, int index) {
		
		NBTTagCompound[] sockets = PhysisArtifacts.getSocketablesFromStack(stack);
		
		lines.add(index, "");
		index++;
		
		int totaloffset = 0;
		int postoffset = 0;
		for (int i=0; i<sockets.length; i++) {
			int offset = totaloffset + i;
			if (sockets[i] != null) {
				ItemStack socketed = ItemStack.loadItemStackFromNBT(sockets[i]);
				
				List<String> stt = socketed.getTooltip(player, false);
				String format = socketed.getItem().getRarity(socketed).rarityColor.toString();
				int socketColour = 3;
				if (socketed.getItem() instanceof ISocketable) {
					socketColour = ((ISocketable)socketed.getItem()).getSocketColour(socketed);
				}
				socketColour = Math.max(Math.min(15, socketColour), 0);
				postoffset++;
				
				if (InputHelper.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode())) {
					for(int j=0; j<stt.size(); j++) {
						if (j == 0) {
							stt.set(j, " \u00A7"+socketColour+"\u25a0\u00A7r  "+format+stt.get(j));
						} else {
							stt.set(j, "     "+stt.get(j));
						}
					}
					lines.addAll(index+offset, stt);
					totaloffset += stt.size() - 1;
				} else {
					String name = socketed.getDisplayName();
					lines.add(index+offset, " \u00A7"+socketColour+"\u25a0\u00A7r  "+format+name+ (stt.size() > 1 ? " \u00A78+" : ""));
				}
			} else {
				lines.add(index+offset, " \u00A78\u25a0  Empty socket");
			}
		}
		if (stack.getItem() instanceof ItemRecord) {
			addRecordTooltip(stack, lines, index+totaloffset+postoffset);
		}
	}
	
	public String getTooltipForSocketable(ItemStack stack) {		
		NBTTagCompound tag = stack.stackTagCompound.getCompoundTag(PhysisArtifacts.ARTIFACTTAG);
		if (tag.hasKey(PhysisArtifacts.TRIGGERTAG) && tag.hasKey(PhysisArtifacts.EFFECTTAG)) {
			IArtifactTrigger trigger = PhysisArtifacts.getTriggerFromSocketable(stack);
			
			if (trigger != null) {
				return Physis.text.formatArtifactNames(trigger.getUnlocalizedTriggerString(), stack);
			}
		}
		return null;
	}
	
	public void addRecordTooltip(ItemStack stack, List<String> lines, int offset) {
		List<String> tip = Physis.text.translateAndWrap("tile.physis:jukebox.recordtip", tipWidth);
		lines.add(offset, "");
		lines.addAll(offset+1, tip);
	}
}

package ttftcuts.physis.common.network.packet;

import java.util.ArrayList;
import java.util.List;

import ttftcuts.physis.Physis;
import ttftcuts.physis.client.gui.journal.JournalArticle;
import ttftcuts.physis.client.gui.journal.PageDefs;
import ttftcuts.physis.client.gui.journal.PageDefs.Category;
import ttftcuts.physis.common.file.PhysisWorldSavedData;
import ttftcuts.physis.common.network.PacketHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class PacketWorldData extends PacketHandler {

	@Override
	public void handle(ByteBuf data, EntityPlayer player) {
		if (player.worldObj.isRemote) {
			NBTTagCompound tag = ByteBufUtils.readTag(data);
			
			NBTTagCompound playerdata = tag.getCompoundTag("p");
			NBTTagCompound worlddata = tag.getCompoundTag("w");
			
			List<JournalArticle> locked = new ArrayList<JournalArticle>();
			for(Category cat : PageDefs.articleMap.keySet()) {
				for(JournalArticle article : PageDefs.articleMap.get(cat)) {
					if (!article.canView()) {
						locked.add(article);
					}
				}
			}
			
			PhysisWorldSavedData.clientPlayerData = playerdata;
			PhysisWorldSavedData.clientWorldData = worlddata;
			PhysisWorldSavedData.doCallbacksPost();
			
			for (JournalArticle article : locked) {
				if (article.canView()) {
					Physis.proxy.doArticlePopup(article);
					//Physis.logger.info("Unlocked article: "+article.getTranslatedName());
				}
			}
		}
	}

	public static FMLProxyPacket createPacket(NBTTagCompound playerdata, NBTTagCompound worlddata) {
		ByteBuf data = PacketHandler.createDataBuffer(PacketWorldData.class);
		
		PhysisWorldSavedData.doCallbacksPre();
		
		//Physis.logger.info(worlddata);
		
		NBTTagCompound tag = new NBTTagCompound();
		
		tag.setTag("p", playerdata);
		tag.setTag("w", worlddata);
		
		ByteBufUtils.writeTag(data, tag);
		
		return buildPacket(data);
	}
}

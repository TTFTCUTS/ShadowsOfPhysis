package ttftcuts.physis.common.handler;

import ttftcuts.physis.client.gui.journal.GuiArticlePopup;
import ttftcuts.physis.client.gui.journal.JournalArticle;
import ttftcuts.physis.client.render.item.RenderSocketed;
import ttftcuts.physis.common.file.ServerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;

public class ClientTickHandler {
	public static int gameTicks = 0;
	
	public static GuiArticlePopup articlePopup;
	
	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) {
		if(event.phase == Phase.END) {
			GuiScreen gui = Minecraft.getMinecraft().currentScreen;
			if (gui == null || !gui.doesGuiPauseGame()) {
				gameTicks++;
			}
			
			ServerData.tick(true);
			
			RenderSocketed.tryInjectRenderer();
		}	
	}
	
	@SubscribeEvent
	public void onRenderTick(RenderTickEvent event) {
		if(Minecraft.getMinecraft().theWorld != null && articlePopup != null) {
			articlePopup.update();
		}
	}
	
	public static void clearArticlePopup() {
		if (articlePopup != null) {
			articlePopup.clear();
		}
	}
	
	public static void setArticlePopup(JournalArticle article) {
		if (articlePopup == null) {
			articlePopup = new GuiArticlePopup(Minecraft.getMinecraft());
		}
		
		articlePopup.setArticleInfo(article);
	}
}

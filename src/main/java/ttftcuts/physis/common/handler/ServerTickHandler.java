package ttftcuts.physis.common.handler;

import ttftcuts.physis.Physis;
import ttftcuts.physis.common.file.ServerData;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

public class ServerTickHandler {

	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event) {
		if (event.phase == Phase.START) {
			if (Physis.oooBuilder != null) {
				Physis.oooBuilder.update();
			}
			
			if (ServerData.instance != null) {
				ServerData.instance.serverTick++;
			}
		}
	}
}
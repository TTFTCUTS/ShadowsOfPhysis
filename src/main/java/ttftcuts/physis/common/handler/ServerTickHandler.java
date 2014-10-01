package ttftcuts.physis.common.handler;

import ttftcuts.physis.Physis;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class ServerTickHandler {

	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event) {
		if (Physis.oooBuilder != null) {
			Physis.oooBuilder.update();
		}
	}
}

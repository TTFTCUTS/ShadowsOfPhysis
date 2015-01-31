package ttftcuts.physis.common.handler;

import ttftcuts.physis.Physis;
import ttftcuts.physis.common.artifact.PhysisArtifacts;
import ttftcuts.physis.common.file.ServerData;
import ttftcuts.physis.common.network.PacketWorldTime;
import ttftcuts.physis.common.network.PhysisPacketHandler;
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
			
			ServerData.tick(false);
		}
		
		if (event.phase == Phase.END) {
			PhysisArtifacts.doPuntEntities();
		}
	}
}

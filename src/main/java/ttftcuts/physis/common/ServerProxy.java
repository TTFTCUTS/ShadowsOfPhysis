package ttftcuts.physis.common;

import ttftcuts.physis.Physis;
import ttftcuts.physis.puzzle.OddOneOutBuilder;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;

public class ServerProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		
		Physis.oooBuilder = new OddOneOutBuilder();
	}
	
	@Override
	public void serverStarting(FMLServerStartingEvent event) {
		super.serverStarting(event);
		Physis.logger.info("SERVER: starting server");
		
		Physis.oooBuilder.start();
	}
	
	@Override
	public void serverStopped(FMLServerStoppedEvent event) {
		super.serverStopped(event);
		Physis.logger.info("SERVER: stopped server");
		
		Physis.oooBuilder.stop();
	}
	
	@Override
	public void requestOddOneOutPuzzle(int difficulty, TileEntity tile) {
		super.requestOddOneOutPuzzle(difficulty, tile);
		Physis.logger.info("SERVER: requesting puzzle");
		
		Physis.oooBuilder.requestPuzzle(difficulty, tile);
	}
}

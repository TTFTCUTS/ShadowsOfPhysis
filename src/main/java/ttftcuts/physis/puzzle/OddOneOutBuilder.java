package ttftcuts.physis.puzzle;

import java.util.concurrent.LinkedBlockingQueue;

import ttftcuts.physis.Physis;

import net.minecraft.tileentity.TileEntity;

public class OddOneOutBuilder {
	private LinkedBlockingQueue<Request> queue;
	private Builder builder;
	
	private final Request poison = new Request(-1, null);
	
	public OddOneOutBuilder() {
		queue = new LinkedBlockingQueue<Request>();
	}
	
	public void requestPuzzle(int difficulty, TileEntity tile) {
		Physis.logger.info("Requesting puzzle for "+tile+", difficulty "+difficulty);
		if (queue != null) {
			try {
				queue.put(new Request(difficulty, tile));
			} catch (InterruptedException e) {
	        	e.printStackTrace();
			}
		}
	}
	
	public void start() {
		Physis.logger.info("Starting Odd One Out Builder");
		
		queue.clear();
		builder = new Builder("Physis Odd One Out Builder");
		builder.setPriority(Thread.MIN_PRIORITY);
		builder.start();
	}
	
	public void stop() {
		if (builder != null) {
			try {
				Physis.logger.info("Stopping Odd One Out Builder");
				queue.clear();
				queue.put(poison);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private class Request {
		public TileEntity tile;
		public int difficulty;
		
		public Request(int difficulty, TileEntity tile) {
			this.difficulty = difficulty;
			this.tile = tile;
		}
	}
	
	private class Builder extends Thread {
		private volatile boolean keepRunning = true;
		
		public Builder(String name) {
			super(name);
		}
		
		public void run() {
			Physis.logger.info("Starting thread");
			
			while(keepRunning) {
				Physis.logger.info("keepRunning: "+keepRunning);
				try {
					Request r = queue.take();
					
					if ( r.difficulty == -1 ){ break; }
					
					Physis.logger.info("Took request by "+r.tile+", difficulty "+r.difficulty);
				} catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }				
			}
			
			Physis.logger.info("Shutting down thread");
		}
	}
}

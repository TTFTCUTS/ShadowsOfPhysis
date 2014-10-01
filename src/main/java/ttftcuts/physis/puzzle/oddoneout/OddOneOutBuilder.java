package ttftcuts.physis.puzzle.oddoneout;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import ttftcuts.physis.Physis;
import ttftcuts.physis.common.block.tile.TileEntityDigSite;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class OddOneOutBuilder {
	private LinkedBlockingQueue<Request> queue;
	private volatile Queue<Request> finishedQueue;
	private Set<Request> waitingRoom;
	private Builder builder;
	
	private final Request poison = new Request(-1, null, 0);
	
	public OddOneOutBuilder() {
		queue = new LinkedBlockingQueue<Request>();
		finishedQueue = new ConcurrentLinkedQueue<Request>();
		waitingRoom = new HashSet<Request>();
		
		OddOneOutProperty.registerProperties();
		OddOneOutConstraintSolver.makePermutationList();
	}
	
	public void requestPuzzle(int difficulty, TileEntity tile, int layerid) {
		World world = tile.getWorldObj();
		if (world.isRemote) { return; }
		
		if (queue != null) {
			try {
				Request req = new Request(difficulty, tile, layerid);
				if (!waitingRoom.contains(req)) {
					queue.put(req);
					waitingRoom.add(req);
				}
			} catch (InterruptedException e) {
	        	e.printStackTrace();
			}
		}
	}
	
	public void start() {
		Physis.logger.info("Starting Odd One Out Builder");
		
		queue.clear();
		finishedQueue.clear();
		waitingRoom.clear();
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
	
	public void update() {
		Request r = finishedQueue.poll();
		if (r != null) {
			Physis.logger.info("Received finished puzzle for tile at "+r.x+","+r.y+","+r.z+" in "+r.world+" with difficulty "+r.difficulty);
			waitingRoom.remove(r);
			
			if (r.world != null) {
				TileEntity tile = r.world.getTileEntity(r.x, r.y, r.z);
				if (tile != null && (tile instanceof TileEntityDigSite)) {
					TileEntityDigSite dig = (TileEntityDigSite)tile;
					
					dig.setLayerPuzzle(r.layerid, r.puzzle);
				}
			}
		}
	}
	
	@SuppressWarnings("unused")
	private class Request {
		public final World world;
		public final int x;
		public final int y;
		public final int z;
		public final int difficulty;
		public final int layerid;
		
		public boolean finished = false;
		public OddOneOutPuzzle puzzle;
		//public
		
		public Request(int difficulty, TileEntity tile, int layerid) {
			this.difficulty = difficulty;
			this.world = tile == null ? null : tile.getWorldObj();
			this.x = tile == null ? 0 : tile.xCoord;
			this.y = tile == null ? 0 : tile.yCoord;
			this.z = tile == null ? 0 : tile.zCoord;
			this.layerid = layerid;
		}
		
		@Override
		public boolean equals(Object other) {
			if (other instanceof Request) {
				Request or = (Request) other;
				
				if (this.world == or.world
						&& this.x == or.x
						&& this.y == or.y
						&& this.z == or.z
						&& this.difficulty == or.difficulty
						&& this.layerid == or.layerid) {
					return true;
				}
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return this.world.hashCode() 
					// 8 digit primes
					+ this.x * 45269999 
					+ this.y * 10312007 
					+ this.z * 60686069 
					+ this.difficulty * 23010067
					+ this.layerid * 37171397;
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
				try {
					Request r = queue.take();
					if ( r.difficulty == -1 ){ break; }
					process(r);
				} catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }				
			}
			
			Physis.logger.info("Shutting down thread");
		}
		
		private void process(Request r) {
			Physis.logger.info("Took request by tile at "+r.x+","+r.y+","+r.z+" in "+r.world+" with difficulty "+r.difficulty);

			r.finished = true;
			r.puzzle = OddOneOutConstraintSolver.buildPuzzle(r.difficulty);
			
			finishedQueue.add(r);
		}
	}
}

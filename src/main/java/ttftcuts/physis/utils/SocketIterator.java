package ttftcuts.physis.utils;

import java.util.Iterator;
import java.util.NoSuchElementException;

import ttftcuts.physis.common.artifact.PhysisArtifacts;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class SocketIterator implements Iterable<Socket> {
	private static SocketIterator instance = new SocketIterator();
	
	private NBTTagCompound[] list;
	private boolean getTriggers = false;
	private boolean getEffects = false;
	
	private SocketIterator() {}

	private static SocketIterator iterate(ItemStack stack) {
		instance.list = PhysisArtifacts.getSocketablesFromStack(stack);
		return instance;
	}
	
	public static SocketIterator triggers(ItemStack stack) {
		instance.getTriggers = true;
		instance.getEffects = false;
		return iterate(stack);
	}
	
	public static SocketIterator effects(ItemStack stack) {
		instance.getTriggers = false;
		instance.getEffects = true;
		return iterate(stack);
	}
	
	public static SocketIterator both(ItemStack stack) {
		instance.getTriggers = true;
		instance.getEffects = true;
		return iterate(stack);
	}
	
	@Override
	public Iterator<Socket> iterator() {
		return new Iter(list, getTriggers, getEffects);
	}
	
	private class Iter implements Iterator<Socket> {
		private NBTTagCompound[] array;
		private int pos = 0;
		private boolean triggers;
		private boolean effects;
		
		public Iter(NBTTagCompound[] array, boolean triggers, boolean effects) {
			this.array = array;
			if (array == null) { this.array = new NBTTagCompound[0]; }
			this.triggers = triggers;
			this.effects = effects;
		}
		
		public boolean hasNext() {
			return this.pos < this.array.length;
		}
		
		public Socket next() throws NoSuchElementException {
			if (hasNext()) {
				return new Socket(array[pos], pos++, triggers, effects);
			} else {
				throw new NoSuchElementException();
			}
		}
		
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}

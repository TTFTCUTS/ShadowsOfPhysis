package ttftcuts.physis.utils;

public class TPair<T> {
	public T val1;
	public T val2;
	public TPair(T v1, T v2) {
		val1 = v1;
		val2 = v2;
	}
	
	@Override
	public String toString() {
		return "TPair("+val1.toString()+", "+val2.toString()+")";
	}
}

package modules.util;

public class Counter {

	private static volatile int id = 0;
	
	public synchronized static int nextInt() {
		return ++id;
	}

}

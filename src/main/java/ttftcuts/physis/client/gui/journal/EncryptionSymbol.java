package ttftcuts.physis.client.gui.journal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EncryptionSymbol {

	public static List<EncryptionSymbol> symbols;
	private static int totalweight = 0;
	
	public static void init() {
		symbols = new ArrayList<EncryptionSymbol>();
		
		new EncryptionSymbol(0,		0,		100,	100,	30);
		new EncryptionSymbol(100,	0,		100,	100,	30);
		new EncryptionSymbol(200,	0,		56,		56,		100);
		new EncryptionSymbol(200,	56,		56,		56,		100);
		new EncryptionSymbol(0,		100,	75,		75,		100);
		new EncryptionSymbol(75,	100,	75,		75,		100);
		new EncryptionSymbol(0,		175,	75,		75,		100);
		new EncryptionSymbol(75,	175,	75,		75,		100);
		new EncryptionSymbol(150,	100,	50,		50,		100);
		new EncryptionSymbol(150,	150,	50,		50,		200);
		new EncryptionSymbol(200,	112,	28,		28,		300);
		new EncryptionSymbol(228,	112,	28,		28,		300);
		new EncryptionSymbol(200,	140,	28,		28,		300);
		new EncryptionSymbol(228,	140,	28,		28,		300);
		new EncryptionSymbol(200,	168,	32,		32,		300);
		new EncryptionSymbol(232,	168,	24,		24,		250);
		new EncryptionSymbol(232,	192,	24,		24,		250);
		new EncryptionSymbol(232,	216,	24,		24,		250);
		new EncryptionSymbol(150,	200,	26,		26,		200);
		new EncryptionSymbol(150,	226,	26,		26,		200);
		new EncryptionSymbol(176,	200,	56,		56,		200);
		new EncryptionSymbol(232,	240,	16,		16,		150);
	}
	
	public static EncryptionSymbol getRandomSymbol(Random rand) {
		int r = rand.nextInt(totalweight);
		int weighttally = 0;
		for (int i=0; i<symbols.size(); i++) {
			weighttally += symbols.get(i).weight;
			if (r <= weighttally) {
				return symbols.get(i);
			}
		}
		return null;
	}
	
	//-----------
	
	public int x;
	public int y;
	public int width;
	public int height;
	public int weight;
	
	public EncryptionSymbol(int x, int y, int w, int h, int weight) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		this.weight = weight;
		totalweight += weight;
		symbols.add(this);
	}
}

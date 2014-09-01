package ttftcuts.physis.common.item;

import ttftcuts.physis.Physis;

public class ItemJournal extends ItemPhysis {

	public ItemJournal() {
		super();
		this.setMaxStackSize(1);
		this.setUnlocalizedName("journal");
		this.setTextureName(Physis.MOD_ID+":journal");
		
	}
}

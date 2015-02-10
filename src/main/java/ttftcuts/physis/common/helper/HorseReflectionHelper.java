package ttftcuts.physis.common.helper;

import java.lang.reflect.Field;

import ttftcuts.physis.Physis;

import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.inventory.AnimalChest;

import cpw.mods.fml.common.ObfuscationReflectionHelper;

public class HorseReflectionHelper {

	private static Field horseChest;
	public static boolean available = true;
	
	public static void init() {
		String fieldname = ObfuscationReflectionHelper.remapFieldNames(EntityHorse.class.getName(), "horseChest")[0];
		try {
			horseChest = EntityHorse.class.getDeclaredField(fieldname);
			horseChest.setAccessible(true);
		} catch (NoSuchFieldException e) {
			Physis.logger.error("Unable to resolve field for horse inventory, disabling horse socketables.", e);
			available = false;
		} catch (SecurityException e) {
			Physis.logger.error("Unable to access field for horse inventory, disabling horse socketables.", e);
			available = false;
		}
	}
	
	public static AnimalChest getHorseChest(EntityHorse horse) {
		if (available) {
			try {
				return (AnimalChest) horseChest.get(horse);
			} catch (IllegalArgumentException e) {
				Physis.logger.error("Illegal Argument getting horse chest:", e);
			} catch (IllegalAccessException e) {
				Physis.logger.error("Illegal Access getting horse chest:", e);
			}
		}
		return null;
	}
}

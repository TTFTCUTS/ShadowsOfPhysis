package ttftcuts.physis.common.helper;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class InputHelper {
	
	public static boolean isKeyDown(int keyCode)
	{
		try
		{
			if (keyCode < 0) // Keycodes less than zero are probably mouse buttons
			{
				return Mouse.isButtonDown(keyCode + 100);
			}
			
			return Keyboard.isKeyDown(keyCode);
		}
		catch (Exception ex)
		{
			// Can happen if an invalid index is used
			return false;
		}
	}
}

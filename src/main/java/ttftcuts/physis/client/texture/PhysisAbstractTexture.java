package ttftcuts.physis.client.texture;

import java.awt.image.BufferedImage;

import net.minecraft.client.renderer.texture.AbstractTexture;

public abstract class PhysisAbstractTexture extends AbstractTexture {

	public BufferedImage generatedImage;
	
    /**
     * Composes the provided values into an int that in the the TYPE_INT_ARGB colour model
     * @param r The red value
     * @param g The green value
     * @param b The blue value
     * @param a The alpha value
     * @return The TYPE_INT_ARGB colour
     */
	protected int compose(int r, int g, int b, int a) {
		int rgb = a;
		rgb = (rgb << 8) + r;
		rgb = (rgb << 8) + g;
		rgb = (rgb << 8) + b;
		return rgb;
	}

    /**
     * Gets the alpha value from a value that has the colour model TYPE_INT_ARGB
     * @param c The colour value
     * @return The alpha value
     */
	protected int alpha(int c) {
		return (c >> 24) & 0xFF;
	}

    /**
     * Gets the red value from a value that has the colour model TYPE_INT_ARGB
     * @param c The colour value
     * @return The red value
     */
	protected int red(int c) {
		return (c >> 16) & 0xFF;
	}

    /**
     * Gets the green value from a value that has the colour model TYPE_INT_ARGB
     * @param c The colour value
     * @return The green value
     */
	protected int green(int c) {
		return (c >> 8) & 0xFF;
	}

    /**
     * Gets the blue value from a value that has the colour model TYPE_INT_ARGB
     * @param c The colour value
     * @return The blue value
     */
	protected int blue(int c) {
		return (c) & 0xFF;
	}
	
	/**
	 * Multiplies two channel values in the range 0-255 and returns the result as 0-255
	 * @param c1 Value 1
	 * @param c2 Value 2
	 * @return The multiplied value 
	 */
	protected int mult(int c1, int c2) {
		return (int)(c1 * (c2/255.0));
	}
}

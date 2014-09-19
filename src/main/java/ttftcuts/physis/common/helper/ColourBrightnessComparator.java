package ttftcuts.physis.common.helper;

import java.util.Comparator;

public class ColourBrightnessComparator implements Comparator<Integer> {

	@Override
	public int compare(Integer col1, Integer col2) {
		Integer b1 = TextureHelper.getPerceptualBrightness(col1);
		Integer b2 = TextureHelper.getPerceptualBrightness(col2);
		return b1.compareTo(b2);
	}
}

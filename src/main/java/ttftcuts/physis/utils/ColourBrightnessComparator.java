package ttftcuts.physis.utils;

import java.util.Comparator;

import ttftcuts.physis.common.helper.TextureHelper;

public class ColourBrightnessComparator implements Comparator<Integer> {

	@Override
	public int compare(Integer col1, Integer col2) {
		Integer b1 = TextureHelper.getPerceptualBrightness(col1);
		Integer b2 = TextureHelper.getPerceptualBrightness(col2);
		return b1.compareTo(b2);
	}
}

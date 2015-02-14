package ttftcuts.physis.api;

import java.util.ArrayList;
import java.util.List;

import ttftcuts.physis.api.internal.DummyArtifactHandler;
import ttftcuts.physis.api.internal.IArtifactHandler;
import ttftcuts.physis.api.internal.LootList;

public final class PhysisAPI {

	public static IArtifactHandler artifactHandler = new DummyArtifactHandler();
	
	public static LootList digSiteLootList;
	
	public static List<String> forbiddenOreNames = new ArrayList<String>();
	
	public static void init() {
		forbiddenOreNames.add("ingotUnstable");
		forbiddenOreNames.add("nuggetUnstable");
	}
}

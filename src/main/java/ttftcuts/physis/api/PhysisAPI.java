package ttftcuts.physis.api;

import ttftcuts.physis.api.internal.DummyArtifactHandler;
import ttftcuts.physis.api.internal.IArtifactHandler;
import ttftcuts.physis.api.internal.LootList;

public final class PhysisAPI {

	public static IArtifactHandler artifactHandler = new DummyArtifactHandler();
	
	public static LootList digSiteLootList;
}

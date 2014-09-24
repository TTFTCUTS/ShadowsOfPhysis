package ttftcuts.physis.api;

import ttftcuts.physis.api.artifact.IArtifactHandler;
import ttftcuts.physis.api.internal.DummyArtifactHandler;

public final class PhysisAPI {

	public static IArtifactHandler artifactHandler = new DummyArtifactHandler();
}

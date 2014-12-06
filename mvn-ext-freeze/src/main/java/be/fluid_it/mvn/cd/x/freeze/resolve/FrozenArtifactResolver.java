package be.fluid_it.mvn.cd.x.freeze.resolve;

import be.fluid_it.mvn.cd.x.freeze.model.GroupIdArtifactIdVersion;

public interface FrozenArtifactResolver {
    GroupIdArtifactIdVersion getLatestFrozenVersion(GroupIdArtifactIdVersion snapshotGroupIdArtifactIdVersion);
    boolean artifactInheritsVersionFromParent();
    String artifactFrozenVersion();
}

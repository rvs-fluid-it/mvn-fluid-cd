package be.fluid_it.mvn.cd.x.freeze.mapping;

import be.fluid_it.mvn.cd.x.freeze.model.GroupIdArtifactIdVersion;

import java.io.File;

public interface ArtifactFreezeMapping {
    boolean contains(GroupIdArtifactIdVersion snapshotGroupIdArtifactIdVersion);
    GroupIdArtifactIdVersion getFrozenArtifact(GroupIdArtifactIdVersion snapshotGroupIdArtifactIdVersion);
    void put(GroupIdArtifactIdVersion snapshotGroupIdArtifactIdVersion, GroupIdArtifactIdVersion frozenGroupIdArtifactIdVersion);
    void put(File pom);
    boolean artifactInheritsVersionOfParent();
    String artifactFrozenVersion();
}

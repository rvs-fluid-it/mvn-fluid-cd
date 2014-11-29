package be.fluid_it.mvn.cd.x.freeze.mapping;

import be.fluid_it.mvn.cd.x.freeze.model.GroupIdArtifactIdVersion;
import be.fluid_it.mvn.cd.x.freeze.model.GroupIdArtifactIdVersionPrefix;

import java.io.File;

public interface ArtifactFreezeMapping {
    boolean contains(GroupIdArtifactIdVersionPrefix groupIdArtifactIdVersionPrefix);
    GroupIdArtifactIdVersion getFrozenArtifact(GroupIdArtifactIdVersionPrefix groupIdArtifactIdVersionPrefix);
    void put(GroupIdArtifactIdVersionPrefix groupIdArtifactIdVersionPrefix, GroupIdArtifactIdVersion groupIdArtifactIdVersion);
    void put(String revision, File pom);
    boolean artifactInheritsVersionOfParent();
    String artifactFrozenVersion();
}

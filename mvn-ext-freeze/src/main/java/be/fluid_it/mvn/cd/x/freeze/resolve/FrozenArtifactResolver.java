package be.fluid_it.mvn.cd.x.freeze.resolve;

import be.fluid_it.mvn.cd.x.freeze.model.GroupIdArtifactIdVersion;
import be.fluid_it.mvn.cd.x.freeze.model.GroupIdArtifactIdVersionPrefix;

public interface FrozenArtifactResolver {
    GroupIdArtifactIdVersion getLatestFrozenVersion(GroupIdArtifactIdVersionPrefix groupIdArtifactIdVersionPrefix);
    boolean artifactInheritsVersionFromParent();
    String artifactFrozenVersion();
}

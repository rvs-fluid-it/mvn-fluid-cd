package be.fluid.mvn.cd.x.freeze.resolve;

import be.fluid.mvn.cd.x.freeze.model.GroupIdArtifactIdVersion;
import be.fluid.mvn.cd.x.freeze.model.GroupIdArtifactIdVersionPrefix;

public interface FrozenArtifactResolver {
    GroupIdArtifactIdVersion getLatestFrozenVersion(GroupIdArtifactIdVersionPrefix groupIdArtifactIdVersionPrefix);
}

package be.fluid.mvn.cd.x.freeze.local;

import be.fluid.mvn.cd.x.freeze.FreezeException;
import be.fluid.mvn.cd.x.freeze.mapping.ArtifactFreezeMapping;
import be.fluid.mvn.cd.x.freeze.model.GroupIdArtifactIdVersion;
import be.fluid.mvn.cd.x.freeze.model.GroupIdArtifactIdVersionPrefix;
import be.fluid.mvn.cd.x.freeze.model.MavenConventions;
import be.fluid.mvn.cd.x.freeze.resolve.FrozenArtifactResolver;
import org.apache.maven.eventspy.EventSpy;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

import java.io.File;
import java.io.FilenameFilter;

@Component(role = FrozenArtifactResolver.class)
public class LocalRepositoryFrozenArtficactResolver implements FrozenArtifactResolver {
    @Requirement
    private Logger logger;

    @Requirement
    private ArtifactFreezeMapping artifactFreezeMapping;

    @Requirement(role = EventSpy.class, hint = LocalRepositoryDirectorySpy.HINT)
    private LocalRepositoryDirectorySpy localRepositoryDirectorySpy;

    public LocalRepositoryFrozenArtficactResolver() {
    }

    LocalRepositoryFrozenArtficactResolver(Logger logger,
                                           LocalRepositoryDirectorySpy localRepositoryDirectorySpy,
                                           ArtifactFreezeMapping artifactFreezeMapping) {
        this.logger = logger;
        this.localRepositoryDirectorySpy = localRepositoryDirectorySpy;
        this.artifactFreezeMapping = artifactFreezeMapping;
    }

    @Override
    public GroupIdArtifactIdVersion getLatestFrozenVersion(final GroupIdArtifactIdVersionPrefix groupIdArtifactIdVersionPrefix) {
        logger.debug("[LocalRepositoryFrozenArtifactResolver]: Looking up frozen version of " + groupIdArtifactIdVersionPrefix + " ...");
        if (artifactFreezeMapping.contains(groupIdArtifactIdVersionPrefix)) {
            return artifactFreezeMapping.getFrozenArtifact(groupIdArtifactIdVersionPrefix);
        } else {
            if (localRepositoryDirectorySpy != null) {
                if (localRepositoryDirectorySpy.localRepositoryDirectory() != null) {
                    StringBuffer artifactPathBuffer = new StringBuffer(localRepositoryDirectorySpy.localRepositoryDirectory().getAbsolutePath());
                    artifactPathBuffer.append("/").append(groupIdArtifactIdVersionPrefix.groupId().replace(".", "/"));
                    artifactPathBuffer.append("/").append(groupIdArtifactIdVersionPrefix.artifactId()).append("/");
                    File artifactPath = new File(artifactPathBuffer.toString());
                    logger.debug("[LocalRepositoryFrozenArtifactResolver]: Looking for frozen version in folder " + artifactPath.getAbsolutePath());
                    if (artifactPath.exists() && artifactPath.isDirectory()) {
                        String[] versionCandidates = artifactPath.list(new FilenameFilter() {
                            @Override
                            public boolean accept(File dir, String name) {
                                return name.startsWith(groupIdArtifactIdVersionPrefix.versionPrefix());
                            }
                        });
                        logger.debug("nr of candidates : " + versionCandidates.length);
                        return new GroupIdArtifactIdVersion(groupIdArtifactIdVersionPrefix.groupId(), groupIdArtifactIdVersionPrefix.artifactId(), latestFrozenVersion(versionCandidates));
                    }
                    throw new FreezeException("[LocalRepositoryFrozenArtifactResolver]: Frozen version not found for " +
                            groupIdArtifactIdVersionPrefix +
                            " ...");
                }
                throw new FreezeException("[LocalRepositoryFrozenArtifactResolver]: Unknown local repository folder");
            }
            throw new FreezeException("[LocalRepositoryFrozenArtifactResolver]: Missing " + LocalRepositoryDirectorySpy.class.getSimpleName());
        }
    }

    String latestFrozenVersion(String[] versionCandidates) {
        String latestFrozenVersion = null;
        StringBuffer versionsText = new StringBuffer("[");
        if (versionCandidates != null) {
            for (String versionCandidate : versionCandidates) {
                if (latestFrozenVersion != null) {
                    versionsText.append(", ");
                }
                versionsText.append(versionCandidate);
                if (latestFrozenVersion == null) {
                    latestFrozenVersion = versionCandidate;
                } else if (!versionCandidate.endsWith(MavenConventions.SNAPSHOT_POSTFIX) && versionCandidate.compareTo(latestFrozenVersion) > 0) {
                    latestFrozenVersion = versionCandidate;
                }
            }
        }
        versionsText.append("]");
        logger.debug("Version candidates: " + versionsText.toString());
        logger.debug("Latest frozen version: " + latestFrozenVersion);
        return latestFrozenVersion;
    }
}
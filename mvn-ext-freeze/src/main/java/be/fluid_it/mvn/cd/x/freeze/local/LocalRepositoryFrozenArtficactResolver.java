package be.fluid_it.mvn.cd.x.freeze.local;

import be.fluid_it.mvn.cd.x.freeze.FreezeException;
import be.fluid_it.mvn.cd.x.freeze.mapping.ArtifactFreezeMapping;
import be.fluid_it.mvn.cd.x.freeze.model.GroupIdArtifactIdVersion;
import be.fluid_it.mvn.cd.x.freeze.model.MavenConventions;
import be.fluid_it.mvn.cd.x.freeze.resolve.FrozenArtifactResolver;
import be.fluid_it.mvn.cd.x.freeze.stamp.Stamper;
import be.fluid_it.mvn.cd.x.freeze.stamp.StamperSwitch;
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

    @Requirement
    private StamperSwitch stamper;

    @Requirement(role = EventSpy.class, hint = LocalRepositoryDirectorySpy.HINT)
    private LocalRepositoryDirectorySpy localRepositoryDirectorySpy;

    // Plexus
    public LocalRepositoryFrozenArtficactResolver() {
    }
    // Testing
    LocalRepositoryFrozenArtficactResolver(Logger logger,
                                           LocalRepositoryDirectorySpy localRepositoryDirectorySpy,
                                           ArtifactFreezeMapping artifactFreezeMapping,
                                           StamperSwitch stamper) {
        this.logger = logger;
        this.localRepositoryDirectorySpy = localRepositoryDirectorySpy;
        this.artifactFreezeMapping = artifactFreezeMapping;
        this.stamper = stamper;
    }

    @Override
    public GroupIdArtifactIdVersion getLatestFrozenVersion(final GroupIdArtifactIdVersion snapshotGroupIdArtifactIdVersion) {
        logger.debug("[LocalRepositoryFrozenArtifactResolver]: Looking up frozen version of " + snapshotGroupIdArtifactIdVersion + " ...");
        if (artifactFreezeMapping.contains(snapshotGroupIdArtifactIdVersion)) {
            return artifactFreezeMapping.getFrozenArtifact(snapshotGroupIdArtifactIdVersion);
        } else {
            if (localRepositoryDirectorySpy != null) {
                if (localRepositoryDirectorySpy.localRepositoryDirectory() != null) {
                    StringBuffer artifactPathBuffer = new StringBuffer(localRepositoryDirectorySpy.localRepositoryDirectory().getAbsolutePath());
                    artifactPathBuffer.append("/").append(snapshotGroupIdArtifactIdVersion.groupId().replace(".", "/"));
                    artifactPathBuffer.append("/").append(snapshotGroupIdArtifactIdVersion.artifactId()).append("/");
                    File artifactPath = new File(artifactPathBuffer.toString());
                    logger.debug("[LocalRepositoryFrozenArtifactResolver]: Looking for frozen version in folder " + artifactPath.getAbsolutePath());
                    if (artifactPath.exists() && artifactPath.isDirectory()) {
                        String[] versionCandidates = artifactPath.list(new FilenameFilter() {
                            @Override
                            public boolean accept(File dir, String name) {
                                return name.startsWith(snapshotGroupIdArtifactIdVersion.snapshotStrippedVersion());
                            }
                        });
                        logger.debug("nr of candidates : " + versionCandidates.length);
                        return new GroupIdArtifactIdVersion(snapshotGroupIdArtifactIdVersion.groupId(), snapshotGroupIdArtifactIdVersion.artifactId(), latestFrozenVersion(versionCandidates));
                    }
                    throw new FreezeException("[LocalRepositoryFrozenArtifactResolver]: Frozen version not found for " +
                            snapshotGroupIdArtifactIdVersion +
                            " ...");
                }
                throw new FreezeException("[LocalRepositoryFrozenArtifactResolver]: Unknown local repository folder");
            }
            throw new FreezeException("[LocalRepositoryFrozenArtifactResolver]: Missing " + LocalRepositoryDirectorySpy.class.getSimpleName());
        }
    }

    @Override
    public boolean artifactInheritsVersionFromParent() {
        return artifactFreezeMapping.artifactInheritsVersionOfParent();
    }

    @Override
    public String artifactFrozenVersion() {
        return artifactFreezeMapping.artifactFrozenVersion();
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
                } else if (!versionCandidate.endsWith(MavenConventions.SNAPSHOT)) {
                    if (stamper.extract(versionCandidate).compareTo(stamper.extract(latestFrozenVersion))  > 0) {
                        latestFrozenVersion = versionCandidate;
                    }
                }
            }
        }
        versionsText.append("]");
        logger.debug("Version candidates: " + versionsText.toString());
        logger.debug("Latest frozen version: " + latestFrozenVersion);
        return latestFrozenVersion;
    }
}
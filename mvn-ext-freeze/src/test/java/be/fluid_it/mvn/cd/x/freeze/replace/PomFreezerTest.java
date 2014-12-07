package be.fluid_it.mvn.cd.x.freeze.replace;

import be.fluid_it.mvn.cd.x.freeze.mapping.DefaultArtifactFreezeMapping;
import be.fluid_it.mvn.cd.x.freeze.model.GroupIdArtifactIdVersion;
import be.fluid_it.mvn.cd.x.freeze.pom.SamplePom_ArtifactInheritsVersion;
import be.fluid_it.mvn.cd.x.freeze.pom.SamplePom_Library;
import be.fluid_it.mvn.cd.x.freeze.resolve.FrozenArtifactResolver;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class PomFreezerTest {
    @Test
    public void freezePom_ArtifactInheritsVersion() {
        FrozenArtifactResolver frozenArtifactResolverDummy = new FrozenArtifactResolver() {
            @Override
            public GroupIdArtifactIdVersion getLatestFrozenVersion(GroupIdArtifactIdVersion snapshotGroupIdArtifactIdVersion) {
                if (SamplePom_ArtifactInheritsVersion.parentGroupIdArtifactIdVersion().equals(snapshotGroupIdArtifactIdVersion)) {
                    return SamplePom_ArtifactInheritsVersion.frozenParentGroupIdArtifactIdVersion();
                } else if (SamplePom_ArtifactInheritsVersion.libraryGroupIdArtifactIdVersion().equals(snapshotGroupIdArtifactIdVersion)) {
                    return SamplePom_ArtifactInheritsVersion.frozenLibraryGroupIdArtifactIdVersion();
                }
                throw new IllegalStateException("Not expected groupIdArtifactIdVersionPrefix : " + snapshotGroupIdArtifactIdVersion);
            }

            @Override
            public boolean artifactInheritsVersionFromParent() {
                return true;
            }

            @Override
            public String artifactFrozenVersion() {
                return SamplePom_ArtifactInheritsVersion.FROZEN_VERSION;
            }
        };

        ByteArrayOutputStream pomOutputStream = new ByteArrayOutputStream();
        new DefaultPomFreezer(frozenArtifactResolverDummy, SamplePom_ArtifactInheritsVersion.fakedStamper(), new ConsoleLogger()).freeze(SamplePom_ArtifactInheritsVersion.asStream(), pomOutputStream);
        String pomContent = pomOutputStream.toString();
        System.out.print(pomContent);
        Assert.assertTrue(pomContent.contains("<version>" + SamplePom_ArtifactInheritsVersion.FROZEN_VERSION + "</version>"));
        Assert.assertTrue(pomContent.contains("<version>" + SamplePom_ArtifactInheritsVersion.LIBRARY_FROZEN_VERSION + "</version>"));
    }

    @Test
    public void freezePom() {
        FrozenArtifactResolver frozenArtifactResolverDummy = new FrozenArtifactResolver() {
            @Override
            public GroupIdArtifactIdVersion getLatestFrozenVersion(GroupIdArtifactIdVersion snapshotGroupIdArtifactIdVersion) {
                if (SamplePom_ArtifactInheritsVersion.parentGroupIdArtifactIdVersion().equals(snapshotGroupIdArtifactIdVersion)) {
                    return SamplePom_ArtifactInheritsVersion.frozenParentGroupIdArtifactIdVersion();
                } else if (SamplePom_ArtifactInheritsVersion.groupIdArtifactIdVersion().equals(snapshotGroupIdArtifactIdVersion)) {
                    return SamplePom_ArtifactInheritsVersion.frozenGroupIdArtifactIdVersion();
                } else if (SamplePom_ArtifactInheritsVersion.libraryGroupIdArtifactIdVersion().equals(snapshotGroupIdArtifactIdVersion)) {
                    return SamplePom_ArtifactInheritsVersion.frozenLibraryGroupIdArtifactIdVersion();
                }
                throw new IllegalStateException("Not expected groupIdArtifactIdVersionPrefix : " + snapshotGroupIdArtifactIdVersion);
            }

            @Override
            public boolean artifactInheritsVersionFromParent() {
                return false;
            }

            @Override
            public String artifactFrozenVersion() {
                return SamplePom_ArtifactInheritsVersion.FROZEN_VERSION;
            }
        };

        ByteArrayOutputStream pomOutputStream = new ByteArrayOutputStream();
        new DefaultPomFreezer(frozenArtifactResolverDummy, SamplePom_ArtifactInheritsVersion.fakedStamper(), new ConsoleLogger()).freeze(SamplePom_Library.asStream(), pomOutputStream);
        String pomContent = pomOutputStream.toString();
        System.out.print(pomContent);
        Assert.assertTrue(pomContent.contains("<version>" + SamplePom_ArtifactInheritsVersion.FROZEN_VERSION + "</version>"));
        Assert.assertTrue(pomContent.contains("<version>" + SamplePom_ArtifactInheritsVersion.LIBRARY_FROZEN_VERSION + "</version>"));
    }

}

package be.fluid.mvn.cd.x.freeze.replace;

import be.fluid.mvn.cd.x.freeze.model.GroupIdArtifactIdVersion;
import be.fluid.mvn.cd.x.freeze.model.GroupIdArtifactIdVersionPrefix;
import be.fluid.mvn.cd.x.freeze.pom.SamplePom;
import be.fluid.mvn.cd.x.freeze.pom.SampleUtilPom;
import be.fluid.mvn.cd.x.freeze.resolve.FrozenArtifactResolver;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

public class UtilPomFreezerTest {
    @Test
    public void testReadingPom() {
        FrozenArtifactResolver frozenArtifactResolverDummy = new FrozenArtifactResolver() {
            @Override
            public GroupIdArtifactIdVersion getLatestFrozenVersion(GroupIdArtifactIdVersionPrefix groupIdArtifactIdVersionPrefix) {
                if (SamplePom.parentGroupIdArtifactIdVersionPrefix().equals(groupIdArtifactIdVersionPrefix)) {
                    return SamplePom.frozenParentGroupIdArtifactIdVersion();
                } else if (SamplePom.groupIdArtifactIdVersionPrefix().equals(groupIdArtifactIdVersionPrefix)) {
                    return SamplePom.frozenGroupIdArtifactIdVersion();
                } else if (SamplePom.libraryGroupIdArtifactIdVersionPrefix().equals(groupIdArtifactIdVersionPrefix)) {
                    return SamplePom.frozenLibraryGroupIdArtifactIdVersion();
                }
                throw new IllegalStateException("Not expected groupIdArtifactIdVersionPrefix : " + groupIdArtifactIdVersionPrefix);
            }

            @Override
            public boolean artifactInheritsVersionFromParent() {
                return false;
            }

            @Override
            public String artifactFrozenVersion() {
                return SamplePom.FROZEN_VERSION;
            }
        };

        ByteArrayOutputStream pomOutputStream = new ByteArrayOutputStream();
        new DefaultPomFreezer(frozenArtifactResolverDummy, new ConsoleLogger()).freeze(SampleUtilPom.asStream(), pomOutputStream);
        String pomContent = pomOutputStream.toString();
        System.out.print(pomContent);
        Assert.assertTrue(pomContent.contains("<version>" + SamplePom.FROZEN_VERSION + "</version>"));
        Assert.assertTrue(pomContent.contains("<version>" + SamplePom.LIBRARY_FROZEN_VERSION + "</version>"));
    }
}

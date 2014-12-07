package be.fluid_it.mvn.cd.x.freeze.local;

import be.fluid_it.mvn.cd.x.freeze.mapping.DefaultArtifactFreezeMapping;
import be.fluid_it.mvn.cd.x.freeze.model.GroupIdArtifactIdVersion;
import be.fluid_it.mvn.cd.x.freeze.stamp.StamperSwitch;
import be.fluid_it.mvn.cd.x.freeze.stamp.revision_buildnumber.RevisionBuildNumberStamper;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class LocalRepositoryFrozenArtficactResolverTest {
    private LocalRepositoryFrozenArtficactResolver resolver;

    @Before
    public void initializeResolver() {
        this.resolver = new LocalRepositoryFrozenArtficactResolver(
                new ConsoleLogger(),
                new LocalRepositoryDirectorySpy() {
                    @Override
                    public File localRepositoryDirectory() {
                        return new File("src/test/repo");
                    }
                },
                new DefaultArtifactFreezeMapping(),
                new StamperSwitch(RevisionBuildNumberStamper.HINT,new RevisionBuildNumberStamper(), new ConsoleLogger())
        );
    }

    @Test
    public void testLatestFrozenVersion() {
        Assert.assertEquals("1.2-123-1", resolver.latestFrozenVersion(new String[] {"1.2-1-1", "1.2-12-1",  "1.2-123-1", "1.2-SNAPSHOT"}));
    }

    @Test
    public void test() {
        Assert.assertEquals(new GroupIdArtifactIdVersion("be.fluid.mvn.test", "sample-artifact-id", "1.2-123-1"), resolver.getLatestFrozenVersion(new GroupIdArtifactIdVersion("be.fluid.mvn.test", "sample-artifact-id", "1.2-SNAPSHOT")));
    }
}
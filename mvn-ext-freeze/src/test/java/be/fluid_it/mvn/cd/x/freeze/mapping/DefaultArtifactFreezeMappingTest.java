package be.fluid_it.mvn.cd.x.freeze.mapping;

import be.fluid_it.mvn.cd.x.freeze.pom.SamplePom_ArtifactInheritsVersion;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.junit.Assert;
import org.junit.Test;

public class DefaultArtifactFreezeMappingTest {
    @Test
    public void testReadingPom() {
        DefaultArtifactFreezeMapping mapping = new DefaultArtifactFreezeMapping(
                SamplePom_ArtifactInheritsVersion.fakedStamper(),
                new ConsoleLogger());

        mapping.put(SamplePom_ArtifactInheritsVersion.asStream());
        Assert.assertTrue(mapping.contains(SamplePom_ArtifactInheritsVersion.groupIdArtifactIdVersion()));
        Assert.assertEquals(SamplePom_ArtifactInheritsVersion.frozenGroupIdArtifactIdVersion(), mapping.getFrozenArtifact(SamplePom_ArtifactInheritsVersion.groupIdArtifactIdVersion()));
    }
}

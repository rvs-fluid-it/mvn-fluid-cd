package be.fluid.mvn.cd.x.freeze.mapping;

import be.fluid.mvn.cd.x.freeze.model.GroupIdArtifactIdVersion;
import be.fluid.mvn.cd.x.freeze.model.GroupIdArtifactIdVersionPrefix;
import be.fluid.mvn.cd.x.freeze.pom.SamplePom;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.StringBufferInputStream;

public class DefaultArtifactFreezeMappingTest {

    public static final String GROUP_ID = "be.fluid.tools.mvn.cd";
    public static final String ARTIFACT_ID = "sample-artifact-id";
    public static final String VERSION_PREFIX = "0.1";
    public static final String VERSION = VERSION_PREFIX + "-SNAPSHOT";
    public static final String REVISION = "123";

    @Test
    public void testReadingPom() {
        DefaultArtifactFreezeMapping mapping = new DefaultArtifactFreezeMapping(new ConsoleLogger());

        mapping.put(REVISION, SamplePom.asStream());
        Assert.assertTrue(mapping.contains(SamplePom.groupIdArtifactIdVersionPrefix()));
        Assert.assertEquals(SamplePom.frozenGroupIdArtifactIdVersion(), mapping.getFrozenArtifact(SamplePom.groupIdArtifactIdVersionPrefix()));
    }
}

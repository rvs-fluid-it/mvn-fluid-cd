package be.fluid.mvn.cd.x.freeze.mapping;

import be.fluid.mvn.cd.x.freeze.model.GroupIdArtifactIdVersion;
import be.fluid.mvn.cd.x.freeze.model.GroupIdArtifactIdVersionPrefix;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.StringBufferInputStream;

public class DefaultArtifactFreezeMappingTest {

    public static final String GROUP_ID = "be.fluid.tools.mvn.cd";
    public static final String ARTIFACT_ID = "sample-arifact-id";
    public static final String VERSION_PREFIX = "0.1";
    public static final String VERSION = VERSION_PREFIX + "-SNAPSHOT";
    public static final String REVISION = "123";

    @Test
    public void testReadingPom() {
        DefaultArtifactFreezeMapping mapping = new DefaultArtifactFreezeMapping();

        String pom = "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "  xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                "  <modelVersion>4.0.0</modelVersion>\n" +
                "  <parent>\n" +
                "    <groupId>" + GROUP_ID + "</groupId>\n" +
                "    <artifactId>mvn-ext-parent</artifactId>\n" +
                "    <version>" + VERSION + "</version>\n" +
                "    <relativePath>../mvn-ext-parent/pom.xml</relativePath>\n" +
                "  </parent>\n" +
                "  <artifactId>" + ARTIFACT_ID + "</artifactId>\n" +
                "</project>";

        InputStream pomInputStream = new ByteArrayInputStream(pom.getBytes());
        mapping.put(REVISION, pomInputStream);
        GroupIdArtifactIdVersionPrefix expectedGroupIdArtifactIdVersionPrefix = new GroupIdArtifactIdVersionPrefix(GROUP_ID, ARTIFACT_ID, VERSION_PREFIX);
        GroupIdArtifactIdVersion expectedGroupIdArtifactIdVersion = expectedGroupIdArtifactIdVersionPrefix.addRevision(REVISION);
        Assert.assertTrue(mapping.contains(expectedGroupIdArtifactIdVersionPrefix));
        Assert.assertEquals(expectedGroupIdArtifactIdVersion, mapping.getFrozenArtifact(expectedGroupIdArtifactIdVersionPrefix));
    }
}

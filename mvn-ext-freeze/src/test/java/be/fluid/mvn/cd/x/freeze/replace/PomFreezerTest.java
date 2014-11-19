package be.fluid.mvn.cd.x.freeze.replace;

import be.fluid.mvn.cd.x.freeze.model.GroupIdArtifactIdVersion;
import be.fluid.mvn.cd.x.freeze.model.GroupIdArtifactIdVersionPrefix;
import be.fluid.mvn.cd.x.freeze.resolve.FrozenArtifactResolver;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;

public class PomFreezerTest {
    public static final String GROUP_ID = "be.fluid.tools.mvn.cd";
    public static final String ARTIFACT_ID = "sample-artifact-id";
    public static final String PARENT_ARTIFACT_ID = "parent-sample-artifact-id";
    public static final String VERSION_PREFIX = "0.1";
    public static final String VERSION = VERSION_PREFIX + "-SNAPSHOT";
    public static final String REVISION = "123";

    @Test
    public void testReadingPom() {
        String pom = "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "  xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                "  <modelVersion>4.0.0</modelVersion>\n" +
                "  <parent>\n" +
                "    <groupId>" + GROUP_ID + "</groupId>\n" +
                "    <artifactId>" + PARENT_ARTIFACT_ID + "</artifactId>\n" +
                "    <version>" + VERSION + "</version>\n" +
                "    <relativePath>../" + PARENT_ARTIFACT_ID + "/pom.xml</relativePath>\n" +
                "  </parent>\n" +
                "  <artifactId>" + ARTIFACT_ID + "</artifactId>\n" +
                "</project>";

        InputStream pomInputStream = new ByteArrayInputStream(pom.getBytes());

        final GroupIdArtifactIdVersionPrefix parentGroupIdArtifactIdVersionPrefix = new GroupIdArtifactIdVersionPrefix(GROUP_ID, PARENT_ARTIFACT_ID, VERSION_PREFIX);
        final GroupIdArtifactIdVersion expectedParentGroupIdArtifactIdVersion = parentGroupIdArtifactIdVersionPrefix.addRevision(REVISION);

        FrozenArtifactResolver frozenArtifactResolverDummy = new FrozenArtifactResolver() {
            @Override
            public GroupIdArtifactIdVersion getLatestFrozenVersion(GroupIdArtifactIdVersionPrefix groupIdArtifactIdVersionPrefix) {
                if (parentGroupIdArtifactIdVersionPrefix.equals(groupIdArtifactIdVersionPrefix)) {
                    return expectedParentGroupIdArtifactIdVersion;
                }
                throw new IllegalStateException("Not expected groupIdArtifactIdVersionPrefix");
            }
        };

        //StringWriter writer = new StringWriter();
        new DefaultPomFreezer(frozenArtifactResolverDummy).freeze(pomInputStream, System.out);
        //writer.getBuffer().toString()
    }
}

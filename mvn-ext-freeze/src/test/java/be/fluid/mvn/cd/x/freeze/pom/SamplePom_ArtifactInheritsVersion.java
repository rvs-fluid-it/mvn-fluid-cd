package be.fluid.mvn.cd.x.freeze.pom;

import be.fluid.mvn.cd.x.freeze.model.GroupIdArtifactIdVersion;
import be.fluid.mvn.cd.x.freeze.model.GroupIdArtifactIdVersionPrefix;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class SamplePom_ArtifactInheritsVersion {
    public static final String GROUP_ID = "be.fluid-it.tools.mvn.cd";
    public static final String ARTIFACT_ID = "sample-artifact-id";
    public static final String PARENT_ARTIFACT_ID = "parent-sample-artifact-id";
    public static final String VERSION_PREFIX = "0.1";
    public static final String VERSION = VERSION_PREFIX + "-SNAPSHOT";
    public static final String REVISION = "123";
    public static final String FROZEN_VERSION = VERSION_PREFIX + "-" + REVISION;
    public static final String LIBRARY_GROUP_ID = "be.fluid-it.tools.mvn.cd.library";
    public static final String LIBRARY_ARTIFACT_ID = "sample-library-artifact-id";
    public static final String LIBRARY_VERSION_PREFIX = "1.2";
    public static final String LIBRARY_VERSION = LIBRARY_VERSION_PREFIX + "-SNAPSHOT";
    public static final String LIBRARY_REVISION = "234";
    public static final String LIBRARY_FROZEN_VERSION = LIBRARY_VERSION_PREFIX + "-" + LIBRARY_REVISION;

    public static InputStream asStream() {
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
                "  <dependencies>\n" +
                "    <dependency>\n" +
                "      <groupId>" + LIBRARY_GROUP_ID + "</groupId>\n" +
                "      <artifactId>" + LIBRARY_ARTIFACT_ID + "</artifactId>\n" +
                "      <version>" + LIBRARY_VERSION + "</version>\n" +
                "    </dependency>\n" +
                "  </dependencies>\n" +
                "</project>";
        return new ByteArrayInputStream(pom.getBytes());
    }

    public static GroupIdArtifactIdVersionPrefix parentGroupIdArtifactIdVersionPrefix() {
        return new GroupIdArtifactIdVersionPrefix(GROUP_ID, PARENT_ARTIFACT_ID, VERSION_PREFIX);
    }

    public static GroupIdArtifactIdVersionPrefix groupIdArtifactIdVersionPrefix() {
        return new GroupIdArtifactIdVersionPrefix(GROUP_ID, ARTIFACT_ID, VERSION_PREFIX);
    }

    public static GroupIdArtifactIdVersionPrefix libraryGroupIdArtifactIdVersionPrefix() {
        return new GroupIdArtifactIdVersionPrefix(LIBRARY_GROUP_ID, LIBRARY_ARTIFACT_ID, LIBRARY_VERSION_PREFIX);
    }

    public static final GroupIdArtifactIdVersion frozenParentGroupIdArtifactIdVersion() {
        return parentGroupIdArtifactIdVersionPrefix().addRevision(REVISION);
    }

    public static final GroupIdArtifactIdVersion frozenGroupIdArtifactIdVersion() {
        return groupIdArtifactIdVersionPrefix().addRevision(REVISION);
    }

    public static final GroupIdArtifactIdVersion frozenLibraryGroupIdArtifactIdVersion() {
        return libraryGroupIdArtifactIdVersionPrefix().addRevision(LIBRARY_REVISION);
    }
}

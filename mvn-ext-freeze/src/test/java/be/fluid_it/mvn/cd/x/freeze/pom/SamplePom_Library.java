package be.fluid_it.mvn.cd.x.freeze.pom;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class SamplePom_Library extends SamplePomSupport {
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
                "  <name>Name of the artifact</name>\n" +
                "  <groupId>" + GROUP_ID + "</groupId>\n" +
                "  <artifactId>" + ARTIFACT_ID + "</artifactId>\n" +
                "  <version>" + VERSION + "</version>" +
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
}

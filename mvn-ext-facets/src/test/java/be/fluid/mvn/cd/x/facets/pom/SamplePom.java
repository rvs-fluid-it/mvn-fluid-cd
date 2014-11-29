package be.fluid.mvn.cd.x.facets.pom;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class SamplePom {
    public static InputStream asStream() {
        String pom =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "\txsi:schemaLocation=\"\n" +
                "\t\thttp://maven.apache.org/POM/4.0.0\n" +
                "\t\thttp://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                "  <modelVersion>4.0.0</modelVersion>\n" +
                "\n" +
                "  <groupId>be.fluid-it.mvn.cd.it</groupId>\n" +
                "  <artifactId>sample-pom-having-facet</artifactId>\n" +
                "  <version>1.0-SNAPSHOT</version>\n" +
                "  <packaging>jar</packaging>\n" +
                "\n" +
                "  <profiles>\n" +
                "    <profile>\n" +
                "      <id>sample-facet-id</id>\n" +
                "    </profile>\n" +
                "  </profiles>\n" +
                "\n" +
                "  <!-- ... -->\n" +
                "\n" +
                "</project>";
        return new ByteArrayInputStream(pom.getBytes());
    }
}

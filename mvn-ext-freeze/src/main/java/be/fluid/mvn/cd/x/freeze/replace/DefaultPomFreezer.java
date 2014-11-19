package be.fluid.mvn.cd.x.freeze.replace;

import be.fluid.mvn.cd.x.freeze.resolve.FrozenArtifactResolver;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;

import static be.fluid.mvn.cd.x.freeze.model.MavenConventions.FROZEN_POM_FILE;

@Component(role = PomFreezer.class)
public class DefaultPomFreezer implements PomFreezer {
    private final SAXParserFactory factory = SAXParserFactory.newInstance();

    @Requirement
    private Logger logger;

    @Requirement
    private FrozenArtifactResolver frozenArtifactResolver;

    public DefaultPomFreezer() {
    }

    public DefaultPomFreezer(FrozenArtifactResolver frozenArtifactResolver) {
        this.frozenArtifactResolver = frozenArtifactResolver;
    }

    public void freeze(InputStream pomInputStream, OutputStream outputStream) {
        try {
            SAXParser saxParser = factory.newSAXParser();
            DefaultHandler handler = new FreezeHandler(outputStream, frozenArtifactResolver);
            saxParser.parse(pomInputStream, handler);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public File freeze(File pomFile) {
        try {
            File frozenPomFile = new File(pomFile.getParentFile(), FROZEN_POM_FILE);
            freeze(new FileInputStream(pomFile), new FileOutputStream(frozenPomFile));
            return frozenPomFile;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

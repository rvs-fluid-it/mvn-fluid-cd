package be.fluid_it.mvn.cd.x.freeze.replace;

import be.fluid_it.mvn.cd.x.freeze.FreezeException;
import be.fluid_it.mvn.cd.x.freeze.mapping.ArtifactFreezeMapping;
import be.fluid_it.mvn.cd.x.freeze.mapping.DefaultArtifactFreezeMapping;
import be.fluid_it.mvn.cd.x.freeze.resolve.FrozenArtifactResolver;
import be.fluid_it.mvn.cd.x.freeze.stamp.Stamper;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;

import static be.fluid_it.mvn.cd.x.freeze.model.MavenConventions.FROZEN_POM_FILE;

@Component(role = PomFreezer.class)
public class DefaultPomFreezer implements PomFreezer {
    private final SAXParserFactory factory = SAXParserFactory.newInstance();

    @Requirement
    private Logger logger;

    @Requirement
    private FrozenArtifactResolver frozenArtifactResolver;

    @Requirement
    private ArtifactFreezeMapping artifactFreezeMapping;

    // Plexus
    public DefaultPomFreezer() {
    }
    // Testing
    DefaultPomFreezer(FrozenArtifactResolver frozenArtifactResolver,
                      Stamper stamper,
                      Logger logger) {
        this.frozenArtifactResolver = frozenArtifactResolver;
        this.logger = logger;
        this.artifactFreezeMapping = new DefaultArtifactFreezeMapping(stamper,logger);
    }

    public void freeze(InputStream pomInputStream, OutputStream outputStream) {
        try {
            SAXParser saxParser = factory.newSAXParser();
            DefaultHandler handler = new FreezeHandler(outputStream, frozenArtifactResolver, artifactFreezeMapping, logger);
            saxParser.parse(pomInputStream, handler);
        } catch (ParserConfigurationException e) {
            throw new FreezeException("Unable to parse pom", e);
        } catch (SAXException e) {
            throw new FreezeException("Unable to parse pom", e);
        } catch (IOException e) {
            throw new FreezeException("Unable to parse pom", e);
        }
    }

    @Override
    public File freeze(File pomFile) {
        logger.debug("[PomFreezer]: Freeze pom [" + pomFile.getAbsolutePath() + "] ...");
        try {
            File frozenPomFile = new File(pomFile.getParentFile(), FROZEN_POM_FILE);
            freeze(new FileInputStream(pomFile), new FileOutputStream(frozenPomFile));
            logger.info("[PomFreezer]: " +
                    pomFile.getName() +
                    " is frozen as " + FROZEN_POM_FILE);
            logger.debug("[PomFreezer]: Frozen pom location: [" + pomFile.getAbsolutePath() + "]");
            return frozenPomFile;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

package be.fluid.mvn.cd.x.facets.polish;

import be.fluid.mvn.cd.x.facets.PolishException;
import be.fluid.mvn.cd.x.facets.resolve.FacetResolver;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;

import static be.fluid.mvn.cd.x.facets.model.MavenConventions.*;

@Component(role = PomPolisher.class)
public class DefaultPomPolisher implements PomPolisher {
    private final SAXParserFactory factory = SAXParserFactory.newInstance();

    @Requirement
    private Logger logger;

    @Requirement
    private FacetResolver facetResolver;

    // Plexus
    public DefaultPomPolisher() {
    }
    // Testing
    DefaultPomPolisher(FacetResolver facetResolver, Logger logger) {
        this.facetResolver = facetResolver;
        this.logger = logger;
    }

    public void polish(InputStream pomInputStream, OutputStream outputStream) {
        try {
            SAXParser saxParser = factory.newSAXParser();
            DefaultHandler handler = new PolishHandler(outputStream, facetResolver, logger);
            saxParser.parse(pomInputStream, handler);
        } catch (ParserConfigurationException e) {
            throw new PolishException("Unable to parse pom", e);
        } catch (SAXException e) {
            throw new PolishException("Unable to parse pom", e);
        } catch (IOException e) {
            throw new PolishException("Unable to parse pom", e);
        }
    }

    @Override
    public File polish(File pomFile) {
        logger.debug("[PomPolisher]: Polish pom [" + pomFile.getAbsolutePath() + "] ...");
        try {
            File polishedPomFile = new File(pomFile.getParentFile(), POLISHED_POM_FILE);
            polish(new FileInputStream(pomFile), new FileOutputStream(polishedPomFile));
            logger.info("[PomPolisher]: " +
                    pomFile.getName() +
                    " is polished as " + POLISHED_POM_FILE);
            logger.debug("[PomPolisher]: Polished pom location: [" + pomFile.getAbsolutePath() + "]");
            return polishedPomFile;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

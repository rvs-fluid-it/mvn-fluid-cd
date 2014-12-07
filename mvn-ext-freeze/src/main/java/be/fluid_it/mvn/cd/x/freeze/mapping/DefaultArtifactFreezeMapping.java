package be.fluid_it.mvn.cd.x.freeze.mapping;

import be.fluid_it.mvn.cd.x.freeze.model.GroupIdArtifactIdVersion;
import be.fluid_it.mvn.cd.x.freeze.model.KnownElementNames;
import be.fluid_it.mvn.cd.x.freeze.stamp.Stamper;
import be.fluid_it.mvn.cd.x.freeze.stamp.StamperSwitch;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component(role = ArtifactFreezeMapping.class)
public class DefaultArtifactFreezeMapping implements ArtifactFreezeMapping {
    private final Map<GroupIdArtifactIdVersion, GroupIdArtifactIdVersion> mapping = new HashMap<GroupIdArtifactIdVersion, GroupIdArtifactIdVersion>();
    private boolean artifactInheritsVersionFromParent = false;
    private String artifactFrozenVersion;

    @Requirement
    private Logger logger;

    @Requirement
    private StamperSwitch stamper;

    // PLexus
    public DefaultArtifactFreezeMapping() {
    }
    // Testing
    public DefaultArtifactFreezeMapping(StamperSwitch stamper, Logger logger) {
        this.logger = logger;
        this.stamper = stamper;
    }

    @Override
    public boolean contains(GroupIdArtifactIdVersion snapshotGroupIdArtifactIdVersion) {
        return mapping.containsKey(snapshotGroupIdArtifactIdVersion);
    }

    @Override
    public GroupIdArtifactIdVersion getFrozenArtifact(GroupIdArtifactIdVersion snapshotGroupIdArtifactIdVersion) {
        return mapping.get(snapshotGroupIdArtifactIdVersion);
    }

    @Override
    public void put(GroupIdArtifactIdVersion snapshotGroupIdArtifactIdVersion, GroupIdArtifactIdVersion frozenGroupIdArtifactIdVersion) {
        mapping.put(snapshotGroupIdArtifactIdVersion, frozenGroupIdArtifactIdVersion);
        logger.debug("[ArtifactFreezeMapping]: Add " +
                snapshotGroupIdArtifactIdVersion +
                " -> " +
                frozenGroupIdArtifactIdVersion);
    }

    public void put(InputStream pomStream) {
        GroupIdArtifactIdVersion snapshotPomGroupIdArtifactIdVersion = extractGroupIdArtifactIdVersion(pomStream);
        GroupIdArtifactIdVersion frozenPomGroupIdArtifactIdVersion = snapshotPomGroupIdArtifactIdVersion.freeze(stamper);
        put(snapshotPomGroupIdArtifactIdVersion, frozenPomGroupIdArtifactIdVersion);
        logger.info("[ArtifactFreezeMapping]: Freeze the pom as artifact " + frozenPomGroupIdArtifactIdVersion);

    }

    @Override
    public void put(File pom) {
        try {
            put(new FileInputStream(pom));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private GroupIdArtifactIdVersion extractGroupIdArtifactIdVersion(InputStream pomStream) {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        try {
            XMLStreamReader reader =
                    factory.createXMLStreamReader(pomStream);
            ElementTree tree = new ElementTree(KnownElementNames.PROJECT);
            tree.root().addChild(KnownElementNames.MODEL_VERSION);
            tree.root().addChild(KnownElementNames.PARENT).addChild(new String[] {KnownElementNames.GROUP_ID, KnownElementNames.ARTIFACT_ID, KnownElementNames.VERSION, KnownElementNames.RELATIVE_PATH});
            tree.root().addChild(KnownElementNames.GROUP_ID, KnownElementNames.ARTIFACT_ID, KnownElementNames.VERSION, KnownElementNames.PACKAGING, KnownElementNames.NAME, KnownElementNames.DESCRIPTION);

            boolean elementFound = true;
            while (elementFound && reader.hasNext()) {
                int event = reader.next();

                switch(event){
                    case XMLStreamConstants.START_ELEMENT:
                        elementFound = tree.moveToChild(reader.getLocalName());
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        String trimmedText = reader.getText().trim();
                        if (!"".equals(trimmedText)) {
                            tree.handle(trimmedText);
                        }
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        tree.moveToParent();
                        break;
                    default:
                        break;
                }
            }
            artifactInheritsVersionFromParent = !tree.artifactOverridesVersionFromParent();
            return tree.groupIdArtifactIdVersion();
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean artifactInheritsVersionOfParent() {
        return artifactInheritsVersionFromParent;
    }

    @Override
    public String artifactFrozenVersion() {
        return artifactFrozenVersion;
    }

}

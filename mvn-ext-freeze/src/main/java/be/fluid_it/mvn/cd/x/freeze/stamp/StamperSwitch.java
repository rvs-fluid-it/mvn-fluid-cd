package be.fluid_it.mvn.cd.x.freeze.stamp;

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component(role = StamperSwitch.class)
public class StamperSwitch implements Stamper {
    @Requirement
    private Logger logger;

    @Requirement
    private Map<String,Stamper> stampers;

    private Stamper selectedStamper;

    // Plexus
    public StamperSwitch() {
    }
    // Testing
    public StamperSwitch(String hint, Stamper stamper, Logger logger) {
        this.stampers = new HashMap<String, Stamper>();
        this.stampers.put(hint, stamper);
        this.selectedStamper = stamper;
        this.logger = logger;
    }

    @Override
    public String stamp(String snapshotVersion) {
        return selectedStamper().stamp(snapshotVersion);
    }

    @Override
    public String stamp(String snapshotVersion, Properties props) {
        return selectedStamper().stamp(snapshotVersion, props);
    }

    @Override
    public Stamp createStamp() {
        return selectedStamper().createStamp();
    }

    @Override
    public Stamp createStamp(Properties props) {
        return selectedStamper().createStamp(props);
    }

    @Override
    public String asString(Stamp stamp) {
        return selectedStamper().asString(stamp);
    }

    @Override
    public String stamp(String snapshotVersion, Stamp stamp) {
        return selectedStamper().stamp(snapshotVersion, stamp);
    }

    @Override
    public Stamp extract(String frozenVersion) {
        return selectedStamper().extract(frozenVersion);
    }

    @Override
    public boolean isEnabled(Properties props) {
        return selectedStamper().isEnabled(props);
    }
    @Override
    public boolean isEnabled() {
        return selectedStamper() != null;
    }

    private Stamper selectedStamper() {
        if (selectedStamper == null) {
            int nrOfKeys = 0;
            String hintSelectedStamper = null;
            for (String hint : stampers.keySet()) {
                Stamper stamper = stampers.get(hint);
                if (stamper.isEnabled() && stamper instanceof TemplateStamper && ((TemplateStamper)stamper).keys().size() > nrOfKeys) {
                    this.selectedStamper = stamper;
                    hintSelectedStamper = hint;
                }
            }
            logger.info("[StamperSwitch]: Selected " + hintSelectedStamper + " as stamper.");
        }
        return selectedStamper;
    }
}

package be.fluid_it.mvn.cd.x.freeze.debug;

import org.apache.maven.eventspy.EventSpy;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

@Component( role = EventSpy.class, hint = EventDebugSpy.HINT)
public class EventDebugSpy implements EventSpy {
    public static final String HINT = "eventDebugSpy";

    @Requirement
    private Logger logger;

    @Override
    public void init(Context context) throws Exception {
        logger.debug("[EventDebugSpy]: Initialized");
    }

    @Override
    public void onEvent(Object event) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("[EventDebugSpy]: " + event.getClass().getSimpleName() + "...");
        }
    }

    @Override
    public void close() throws Exception {
    }
}

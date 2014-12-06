package be.fluid_it.mvn.cd.x.freeze.stamp;

import java.util.Map;
import java.util.Properties;

public interface Stamper<S extends Stamp> {
    String stamp(String snapshotVersion);
    String stamp(String snapshotVersion, Properties props);
    S createStamp();
    S createStamp(Properties properties);
    String asString(S stamp);
    String stamp(String snapshotVersion, S stamp);
    S extract(String frozenVersion);
    boolean isEnabled(Properties properties);
    boolean isEnabled();
}

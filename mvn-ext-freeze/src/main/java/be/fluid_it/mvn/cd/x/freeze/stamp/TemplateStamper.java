package be.fluid_it.mvn.cd.x.freeze.stamp;

import be.fluid_it.mvn.cd.x.freeze.model.MavenConventions;
import be.fluid_it.mvn.cd.x.freeze.util.LoggerProvider;
import org.codehaus.plexus.logging.Logger;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class TemplateStamper<S extends Stamp> implements Stamper<S>, LoggerProvider {
    public static final String PLACEHOLDER_START = "\\$\\{";
    public static final String PLACEHOLDER_END = "\\}";
    public final static String PLACEHOLDER_EXP = PLACEHOLDER_START + "(.+?)" + PLACEHOLDER_END;
    private final String template;
    private final Set<String> keys;

    public TemplateStamper(String template) {
        this.template = template;
        this.keys = extractKeys(template);
    }

    static Set<String> extractKeys(String template) {
        Pattern pattern = Pattern.compile(PLACEHOLDER_EXP);
        final Matcher matcher = pattern.matcher(template);
        final Set<String> keys = new LinkedHashSet<>();

        while (matcher.find()) {
            keys.add(matcher.group(1));
        }
        return keys;
    }

    public String asString(S stamp) {
        String result = template;
        for (String key : keys) {
             logger().info("[TemplateStamper]: result = " + result +
                     " ,key = " + key +
                      ", stamp = " + stamp);
             String value = stamp.value(key);
             logger().info("[TemplateStamper]: value = " + value);
             result = result.replaceAll(PLACEHOLDER_START + key + PLACEHOLDER_END, value);
        }
        return result;
    }

    @Override
    public S createStamp() {
        return createStamp(System.getProperties());
    }

    @Override
    public abstract S createStamp(Properties props);

    @Override
    public String stamp(String snapshotVersion) {
        return stamp(snapshotVersion, System.getProperties());
    }

    @Override
    public String stamp(String snapshotVersion, Properties props) {
        return stamp(snapshotVersion, createStamp(props));
    }

    @Override
    public String stamp(String snapshotVersion, S stamp) {
        if (snapshotVersion.endsWith(MavenConventions.SNAPSHOT)) {
            return snapshotVersion.split(MavenConventions.SNAPSHOT)[0] + asString(stamp);
        }
        return snapshotVersion;
    }

    @Override
    public S extract(String frozenVersion) {
        if (frozenVersion.endsWith(MavenConventions.SNAPSHOT)) return null;
        logger().info("[TemplateStamper]: Extracting stamp from " + frozenVersion);
        Properties props = new Properties();
        Pattern pattern = Pattern.compile("-" +
                template.replaceAll(PLACEHOLDER_EXP, "(.+?)") +
                "$");
        Matcher matcher = pattern.matcher(frozenVersion);

        int groupIndex = 1;
        while (matcher.find()) {
            for (String key : keys) {
                props.put(key, matcher.group(groupIndex));
                groupIndex++;
            }
        }
        return props.size() > 0 ? createStamp(props) : null;
    }

    @Override
    public String unfreeze(String frozenVersion) {
        S stamp = extract(frozenVersion);
        return stamp != null ? frozenVersion.replace(asString(stamp), MavenConventions.SNAPSHOT) : frozenVersion;
    }

    public Set<String> keys() {
        return keys;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled(System.getProperties());
    }

    @Override
    public boolean isEnabled(Properties properties) {
        for (String key : keys) {
            if (properties.containsKey(key)) continue;
            return false;
        }
        return true;
    }
}
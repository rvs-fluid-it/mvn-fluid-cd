package be.fluid.mvn.cd.x.multi;

import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component( role = ExtensionRegistry.class )
public class DefaultExtensionRegistry implements ExtensionRegistry {

    @Override
    public Set<String> asSet() {
        return extensionMap.keySet();
    }

    @Requirement (role = AbstractMavenLifecycleParticipant.class )
    public Map<String, AbstractMavenLifecycleParticipant> extensionMap;

    @Override
    public String asText() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        boolean first = true;
        for (String hint : extensionMap.keySet()) {
            if (first) {
                first = false;
            } else {
                buffer.append(", ");
            }
            buffer.append(hint);
        }
        buffer.append("]");
        return buffer.toString();
    }
}

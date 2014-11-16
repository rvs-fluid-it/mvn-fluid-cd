package be.fluid.mvn.cd.x.multi;

import org.codehaus.plexus.component.annotations.Component;

import java.util.LinkedList;
import java.util.List;

@Component( role = ExtensionRegistry.class )
public class DefaultExtensionRegistry implements ExtensionRegistry {
    private final List<String> hints = new LinkedList<String>();

    @Override
    public void register(String hint) {
        hints.add(hint);
    }

    @Override
    public List<String> asList() {
        return hints;
    }

    @Override
    public String asText() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        boolean first = true;
        for (String hint : hints) {
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

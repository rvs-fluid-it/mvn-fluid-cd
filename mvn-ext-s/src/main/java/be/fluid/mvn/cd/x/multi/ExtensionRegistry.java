package be.fluid.mvn.cd.x.multi;

import java.util.List;

public interface ExtensionRegistry {
    void register(String hint);
    List<String> asList();
    String asText();
}

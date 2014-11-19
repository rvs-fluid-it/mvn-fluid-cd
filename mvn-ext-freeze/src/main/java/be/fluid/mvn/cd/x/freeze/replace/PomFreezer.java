package be.fluid.mvn.cd.x.freeze.replace;

import java.io.File;

public interface PomFreezer {
    File freeze(File pomFile);
}

package be.fluid_it.mvn.cd.x.freeze.stamp.revision_buildnumber;

import be.fluid_it.mvn.cd.x.freeze.stamp.revision_buildnumber.RevisionBuildNumberStamp;

import static org.junit.Assert.*;

public class RevisionBuildNumberStampTest {

    @org.junit.Test
    public void testCompareTo() throws Exception {
        assertEquals(0, new RevisionBuildNumberStamp("2","8").compareTo(new RevisionBuildNumberStamp("2", "8")));
        assertTrue(new RevisionBuildNumberStamp("2","8").compareTo(new RevisionBuildNumberStamp("1", "9")) > 0);
        assertTrue(new RevisionBuildNumberStamp("1","9").compareTo(new RevisionBuildNumberStamp("2", "8")) < 0);
        assertTrue(new RevisionBuildNumberStamp("2","19").compareTo(new RevisionBuildNumberStamp("2", "8")) > 0);
        assertTrue(new RevisionBuildNumberStamp("2","8").compareTo(new RevisionBuildNumberStamp("2", null)) > 0);
        assertEquals(0, new RevisionBuildNumberStamp("bc","de").compareTo(new RevisionBuildNumberStamp("bc", "de")));
        assertTrue(new RevisionBuildNumberStamp("bcd","de").compareTo(new RevisionBuildNumberStamp("ab", "efg"))> 0);
    }
}
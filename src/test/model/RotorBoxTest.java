package model;

import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class RotorBoxTest {

    private RotorBox testRotorBox;

    @BeforeEach
    void setup() {
        LinkedHashSet<String> testIO = new LinkedHashSet<>();
        testIO.add("W");
        testIO.add("X");
        testIO.add("Y");
        testIO.add("Z");
        testRotorBox = new RotorBox(testIO);
    }

    //--------------------------------------------------------------
    // Test Constructor
    @Test
    void testConstructor() {
        // Test
        List<String> testRotorNames = testRotorBox.getRotorNames();
        assertEquals(1, testRotorNames.size());
        assertEquals("default", testRotorNames.get(0));
    }

    // --------------------------------------------------------------
    // Test addRotors
    // Add no rotors
    @Test
    void testAddRotorsNone() {
        // Test
        List<String> testRotorNames = testRotorBox.getRotorNames();
        assertEquals(1, testRotorNames.size());
        assertEquals("default", testRotorNames.get(0));
    }

    // Add one rotor
    @Test
    void testAddRotorsOne() {
        // Change
        LinkedHashSet<String> io = new LinkedHashSet<>();
        io.add("X");
        io.add("Z");
        io.add("Y");
        io.add("W");
        Rotor rotorA = new Rotor("A", io);
        testRotorBox.addRotors(rotorA);

        // Test
        List<String> testRotorNames = testRotorBox.getRotorNames();
        assertEquals(2, testRotorNames.size());
        assertEquals("default", testRotorNames.get(0));
        assertEquals("A", testRotorNames.get(1));
    }

    // ----------------------------------------------------------------
    // Test removeRotors
    // Test remove one rotor that is not the default rotor
    @Test
    void testRemoveRotorsNonDefault() {
        // Change
        LinkedHashSet<String> io = new LinkedHashSet<>();
        io.add("X");
        io.add("Z");
        io.add("Y");
        io.add("W");
        Rotor rotorA = new Rotor("A", io);
        Rotor rotorB = new Rotor("B", io);
        testRotorBox.addRotors(rotorA);
        testRotorBox.addRotors(rotorB);
        testRotorBox.removeRotors(rotorA);

        // Test
        List<String> testRotorNames = testRotorBox.getRotorNames();
        assertEquals(2, testRotorNames.size());
        assertEquals("default", testRotorNames.get(0));
        assertEquals("B", testRotorNames.get(1));
    }

    // Test removing the default rotor
    @Test
    void testRemoveRotorsDefault() {
        // Change
        List<Rotor> testRotors = testRotorBox.getRotors();
        Rotor testDefaultRotor = testRotors.get(0);
        testRotorBox.removeRotors(testDefaultRotor);

        // Test
        List<String> testRotorNames = testRotorBox.getRotorNames();
        assertEquals(1, testRotorNames.size());
        assertEquals("default", testRotorNames.get(0));
    }
}

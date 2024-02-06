package model;

import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class InverterTest {

    private Inverter testInverter;

    @BeforeEach
    void setup() {
        LinkedHashSet<String> io = new LinkedHashSet<>();
        io.add("W");
        io.add("X");
        io.add("Y");
        io.add("Z");
        testInverter = new Inverter(io);
    }

    // Test Constructor
    @Test
    void testConstructor() {
        // Test
        Map<String, String> testInversions = testInverter.getInversions();
        List<String> testIO = testInverter.getIO();
        // Correct possible inputs, correct size of inputs
        assertEquals(4, testInverter.getNumOfIO());
        assertEquals(4, testIO.size());
        // Correct mappings
        assertEquals("Z", testInversions.get("W"));
        assertEquals("Y", testInversions.get("X"));
        assertEquals("X", testInversions.get("Y"));
        assertEquals("W", testInversions.get("Z"));
    }

    // Test passThroughWire
    // No change in wiring
    @Test
    void testPassThroughWire() {
        // Test
        assertEquals(3, testInverter.passThroughWire(null, null, 0));
        assertEquals(2, testInverter.passThroughWire(null, null, 1));
        assertEquals(1, testInverter.passThroughWire(null, null, 2));
        assertEquals(0, testInverter.passThroughWire(null, null, 3));
    }
    // TODO: change in wiring; implement after changeElectricWiring method
}

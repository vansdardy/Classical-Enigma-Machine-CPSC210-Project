package model;

import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class PlugboardTest {

    private Plugboard testPlugboard;

    @BeforeEach
    void setup() {
        LinkedHashSet<String> io = new LinkedHashSet<>();
        io.add("W");
        io.add("X");
        io.add("Y");
        io.add("Z");
        testPlugboard = new Plugboard(io);
    }

    // Test constructor
    @Test
    void testConstructor() {
        Map<String, String> testConnections = testPlugboard.getConnections();
        List<String> testIO = testPlugboard.getIO();

        // Test
        assertEquals("W", testConnections.get("W"));
        assertEquals("X", testConnections.get("X"));
        assertEquals("Y", testConnections.get("Y"));
        assertEquals("Z", testConnections.get("Z"));
        // Test for input positions
        assertEquals(4, testIO.size());
    }

    // Test passThroughWire
    // No change in wiring
    @Test
    void testPassThroughWireNoWiring() {
        // Test
        assertEquals(0, testPlugboard.passThroughWire(null, null, 0));
        assertEquals(1, testPlugboard.passThroughWire(null, null, 1));
        assertEquals(2, testPlugboard.passThroughWire(null, null, 2));
        assertEquals(3, testPlugboard.passThroughWire(null, null, 3));
    }
    // Change in wiring
    // @Test
    // void testPassThroughWireChangeWiring() {
    // TODO: Test after implementing changeElectricWiring method
    // }
}

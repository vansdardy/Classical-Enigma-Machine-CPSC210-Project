package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.JsonSaver;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class RotorTest {
    Rotor testRotor;
    int fullRound;
    List<String> testRotorRight;
    List<String> testRotorLeft;
    Map<String, String> testWiring;

    @BeforeEach
    void setup() {
        LinkedHashSet<String> io = new LinkedHashSet<>();
        // Add inputs and outputs
        io.add("W");
        io.add("X");
        io.add("Y");
        io.add("Z");

        // Initialize Rotor
        testRotor = new Rotor("test", io);
        testRotorRight = testRotor.getRotorRight();
        testRotorLeft = testRotor.getRotorLeft();
        testWiring = testRotor.getRotorWiring();
        fullRound = testWiring.size();
    }

    // ---------------------------------------------------
    // Test Constructor
    // Rotor with 2 pairs of input and output only
    @Test
    void testRotorTwoPairs() {
        // Change
        LinkedHashSet<String> io = new LinkedHashSet<>();
        io.add("A");
        io.add("B");
        testRotor = new Rotor("test", io);

        // Test
        // Correct name
        assertEquals("test", testRotor.getName());
        // Correct right side
        assertEquals("A", testRotor.getRightWithIndex(0));
        assertEquals("B", testRotor.getRightWithIndex(1));
        // Correct left side
        assertEquals("A", testRotor.getLeftWithIndex(0));
        assertEquals("B", testRotor.getLeftWithIndex(1));
    }

    // Rotor with multiple pairs of input and output
    @Test
    void testRotorMultiplePairs() {
        // Test
        // Correct name
        assertEquals("test", testRotor.getName());
        // Correct input size
        assertEquals(4, testRotorRight.size());
        // Correct output size
        assertEquals(4, testRotorLeft.size());
        // Correct pairing size
        assertEquals(4, fullRound);
    }

    // Test overloaded constructor
    @Test
    void testOverloadRotor() {
        // Change
        Rotor copyRotor = new Rotor("copy", testRotorRight, testWiring);
        List<String> copyRotorRight = copyRotor.getRotorRight();
        List<String> copyRotorLeft = copyRotor.getRotorLeft();
        Map<String, String> copyMap = copyRotor.getRotorWiring();

        // Test
        // Correct name
        assertEquals("copy", copyRotor.getName());
        // Correct input size
        assertEquals(4, copyRotorRight.size());
        // Correct output size
        assertEquals(4, copyRotorLeft.size());
        // Correct pairing size
        assertEquals(4, copyMap.size());
    }

    // ----------------------------------------------------------
    // Test rotate
    // No rotation
    @Test
    void testRotateNoRotation() {
        // Test
        assertEquals(0, testRotor.getRotation());
    }

    // One rotation
    @Test
    void testRotateOnce() {
        // Test
        assertFalse(testRotor.rotate());
        assertEquals(1, testRotor.getRotation());
    }

    // Rotate to position before full round
    @Test
    void testRotateBeforeFullRound() {
        // Test
        assertFalse(testRotor.rotate());
        assertFalse(testRotor.rotate());
        assertFalse(testRotor.rotate());
        assertEquals(3, testRotor.getRotation());
    }

    // Rotate full round
    @Test
    void testRotateFullRound() {
        // Test
        assertFalse(testRotor.rotate());
        assertFalse(testRotor.rotate());
        assertFalse(testRotor.rotate());
        assertTrue(testRotor.rotate());
        assertEquals(0, testRotor.getRotation());
    }

    // Rotate more than a full round but less than 2 rounds
    @Test
    void testRotateMoreThanOneRoundLessThanTwoRounds() {
        // Change
        for (int i = 0; i < fullRound; i++) {
            testRotor.rotate();
        }

        // Test
        assertFalse(testRotor.rotate());
        assertEquals(1, testRotor.getRotation());
    }

    // Rotate multiple rounds
    @Test
    void testRotateMultipleRounds() {
        // Change
        for (int i = 0; i < 3 * fullRound - 1; i++) {
            testRotor.rotate();
        }

        // Test
        assertTrue(testRotor.rotate());
        assertEquals(0, testRotor.getRotation());
    }

    // Rotate multiple rounds and two extra times
    @Test
    void testRotateMultipleRoundsWithExtraRotations() {
        // Change
        for (int i = 0; i < 3 * fullRound + 1; i++) {
            testRotor.rotate();
        }

        // Test
        assertFalse(testRotor.rotate());
        assertEquals(2, testRotor.getRotation());
    }

    // -----------------------------------------------------
    // Test display
    // No rotation
    @Test
    void testDisplayNoRotation() {
        // Test
        assertEquals("W", testRotor.display());
    }

    // One rotation
    @Test
    void testDisplayOneRotation() {
        // Change
        testRotor.rotate();

        // Test
        assertEquals("X", testRotor.display());
    }

    // Rotate full rounds
    @Test
    void testDisplayMultipleFullRound() {
        // Change
        for (int i = 0; i < 3 * fullRound; i++) {
            testRotor.rotate();
        }

        // Test
        assertEquals("W", testRotor.display());
    }

    // Rotate full rounds with extra rotations
    @Test
    void testDisplayMultipleRoundsWithExtra() {
        // Change
        for (int i = 0; i < 3 * fullRound + 2; i++) {
            testRotor.rotate();
        }

        // Test
        assertEquals("Y", testRotor.display());
    }

    // ----------------------------------------------------
    // Test passThroughWire
    // No change in wiring
    // Right to left
    @Test
    void testPassThroughWireRightToLeft() {
        // Test
        assertEquals(0, testRotor.passThroughWire(testRotor.getRotorRight(),
                testRotor.getRotorLeft(), 0));
        assertEquals(1, testRotor.passThroughWire(testRotor.getRotorRight(),
                testRotor.getRotorLeft(), 1));
        assertEquals(2, testRotor.passThroughWire(testRotor.getRotorRight(),
                testRotor.getRotorLeft(), 2));
        assertEquals(3, testRotor.passThroughWire(testRotor.getRotorRight(),
                testRotor.getRotorLeft(), 3));
    }

    // Left to right
    @Test
    void testPassThroughWireLeftToRight() {
        // Test
        assertEquals(0, testRotor.passThroughWire(testRotor.getRotorLeft(),
                testRotor.getRotorRight(), 0));
        assertEquals(1, testRotor.passThroughWire(testRotor.getRotorLeft(),
                testRotor.getRotorRight(), 1));
        assertEquals(2, testRotor.passThroughWire(testRotor.getRotorLeft(),
                testRotor.getRotorRight(), 2));
        assertEquals(3, testRotor.passThroughWire(testRotor.getRotorLeft(),
                testRotor.getRotorRight(), 3));
    }
    // TODO: change in wiring; implement after implementing changeElectricWiring method
}

package model;

import org.junit.jupiter.api.*;
import persistence.JsonSaver;

import java.io.FileNotFoundException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class EnigmaTest {

    private Enigma testEnigma;
    private Inverter testInverter;
    private Plugboard testPlugboard;
    private RotorBox testRotorBox;
    private HashSet<String> testIO = new HashSet<>();

    @BeforeEach
    void setup() {
        testIO.add("W");
        testIO.add("X");
        testIO.add("Y");
        testIO.add("Z");

        testEnigma = new Enigma(testIO);
        testInverter = testEnigma.getInverter();
        testPlugboard = testEnigma.getPlugboard();
        testRotorBox = testEnigma.getRotorBox();
    }

    // Test Constructor
    @Test
    void testConstructor() {
        // Test Inverter
        assertEquals(4, testInverter.getNumOfIO());
        // Test RotorBox
        List<String> rotorNamesInBox = testRotorBox.getRotorNames();
        List<Rotor> rotorsInBox = testRotorBox.getRotors();
        assertEquals(1, rotorNamesInBox.size());
        assertEquals(1, rotorsInBox.size());
        assertEquals("default", rotorNamesInBox.get(0));
        // Test Rotors To Use
        List<Rotor> rotorsInEnigma = testEnigma.getRotorsToUse();
        assertEquals(0, rotorsInEnigma.size());
        // Test IO
        HashSet<String> availableIO = testEnigma.getIO();
        assertEquals(4, availableIO.size());
    }

    // Test retrieveRotor
    // Take one rotor
    @Test
    void testRetrieveRotorOne() {
        // Change
        testEnigma.retrieveRotor(1, "Rotor 1");

        // Test
        List<Rotor> currentRotorsToUse = testEnigma.getRotorsToUse();
        assertEquals(1, currentRotorsToUse.size());
        assertEquals("Rotor 1", currentRotorsToUse.get(0).getName());
    }

    // Take two rotor (from the same rotor in rotor box)
    @Test
    void testRetrieveRotorTwoSame() {
        // Change
        testEnigma.retrieveRotor(1, "Rotor 1");
        testEnigma.retrieveRotor(1, "Rotor 2");

        // Test
        List<Rotor> currentRotorsToUse = testEnigma.getRotorsToUse();
        assertEquals(2, currentRotorsToUse.size());
        assertEquals("Rotor 1", currentRotorsToUse.get(0).getName());
        assertEquals("Rotor 2", currentRotorsToUse.get(1).getName());
    }

    // Take two rotor (different rotors in rotor box)
    @Test
    void testRetrieveRotorTwoDifferent() {
        // Change
        LinkedHashSet<String> addedIO = new LinkedHashSet<>();
        addedIO.add("Z");
        addedIO.add("Y");
        addedIO.add("X");
        addedIO.add("W");
        Rotor addedRotor = new Rotor("A", addedIO);
        testRotorBox.addRotors(addedRotor);

        testEnigma.retrieveRotor(1, "Rotor Default");
        testEnigma.retrieveRotor(2, "Rotor A");

        // Test
        List<Rotor> currentRotorsToUse = testEnigma.getRotorsToUse();
        Rotor firstRotor = currentRotorsToUse.get(0);
        Rotor secondRotor = currentRotorsToUse.get(1);
        assertEquals(2, currentRotorsToUse.size());
        assertEquals("Rotor Default", firstRotor.getName());
        assertEquals("Rotor A", secondRotor.getName());
        assertEquals("W", firstRotor.getRotorRight().get(0));
        assertEquals("Z", secondRotor.getRotorRight().get(0));
    }

    // Test encrypt
    // Enigma only has plugboard and inverter
    // W - (W - W) - (W - Z) - (Z - Z) - Z
    // X - (X - X) - (X - Y) - (Y - Y) - Y
    // ...
    @Test
    void testEncryptNoRotor() {
        assertEquals("Z", testEnigma.encrypt("W"));
        assertEquals("Y", testEnigma.encrypt("X"));
        assertEquals("X", testEnigma.encrypt("Y"));
        assertEquals("W", testEnigma.encrypt("Z"));

        assertEquals("WXYZ", testEnigma.getOriginalMessage());
        assertEquals("ZYXW", testEnigma.getEncryptedMessage());
    }

    // Enigma has one rotor (default rotor from box)
    // W - (W - W) - (W - W) - (W - Z) - (Z - Z) - (Z - Z) - Z
    // X - (X - X) - (Y - Y) - (X - Y) - (Z - Z) - (Y - Y) - Y
    @Test
    void testEncryptOneDefault() {
        // Change
        testEnigma.retrieveRotor(1, "Default 1");

        // Test
        assertEquals("Z", testEnigma.encrypt("W"));
        assertEquals("Y", testEnigma.encrypt("X"));
        assertEquals("X", testEnigma.encrypt("Y"));
        assertEquals("W", testEnigma.encrypt("Z"));

        assertEquals("WXYZ", testEnigma.getOriginalMessage());
        assertEquals("ZYXW", testEnigma.getEncryptedMessage());
        assertEquals(0, testEnigma.getRotorsToUse().get(0).getRotation());
    }

    // Enigma has two rotors (default rotor from box)
    // W - (W - W) - (W - W) - (W - W) - (W - Z) - (Z - Z) - (Z - Z) - (Z - Z) - Z
    @Test
    void testEncryptTwoDefault() {
        // Change
        testEnigma.retrieveRotor(1, "Default 1");
        testEnigma.retrieveRotor(1, "Default 2");
        // Test
        assertEquals("Z", testEnigma.encrypt("W"));
        assertEquals("Y", testEnigma.encrypt("X"));
        assertEquals("X", testEnigma.encrypt("Y"));
        assertEquals("W", testEnigma.encrypt("Z"));

        assertEquals("WXYZ", testEnigma.getOriginalMessage());
        assertEquals("ZYXW", testEnigma.getEncryptedMessage());
        assertEquals(0, testEnigma.getRotorsToUse().get(0).getRotation());
        assertEquals(1, testEnigma.getRotorsToUse().get(1).getRotation());
    }
    // TODO: test two rotors manually-added
}
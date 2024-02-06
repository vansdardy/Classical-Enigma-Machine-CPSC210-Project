package persistence;

import model.Enigma;
import model.Inverter;
import model.Plugboard;
import model.RotorBox;
import org.junit.jupiter.api.*;
import persistence.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;


/**
 * This test class is modeled from JsonWriterTest class in
 * JsonSerializationDemo
 */
public class JsonSaverTest {

    private JsonSaver testJsonSaver;

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

    // Test invalid file
    @Test
    void testInvalidFileDestination() {
        try {
            testJsonSaver = new JsonSaver("./testJsonFiles/\0testFile.json");
            testJsonSaver.open();
            fail("Should not find file!");
        } catch (FileNotFoundException e) {
            System.out.println("Invalid File Destination!");
        }
    }

    // Test default
    @Test
    void testDefaultSetup() {
        try {
            final String FILE = "./data/testJsonFiles/testDefaultSave.json";
            testJsonSaver = new JsonSaver(FILE);
            testJsonSaver.open();
            testJsonSaver.write(testEnigma);
            testJsonSaver.close();

            // Test by reading it
            JsonLoader testJsonLoader = new JsonLoader(FILE);
            Enigma loadEnigma = testJsonLoader.read();
            assertEquals(testEnigma.getOriginalMessage(), loadEnigma.getOriginalMessage());
            assertEquals(testEnigma.getEncryptedMessage(), loadEnigma.getEncryptedMessage());
            assertEquals(testEnigma.getRotorBox().getRotorNames(),
                    loadEnigma.getRotorBox().getRotorNames());
            for (int i = 0; i < testEnigma.getRotorsToUse().size(); i += 1) {
                assertEquals(testEnigma.getRotorsToUse().get(i),
                        loadEnigma.getRotorsToUse().get(i));
            }
            assertEquals(4, loadEnigma.getInverter().getNumOfIO());
            assertEquals(0, loadEnigma.getRotorsToUse().size());
        } catch (FileNotFoundException e) {
            fail("Proper file exists, should not throw this exception!");
        } catch (IOException e) {
            fail("Proper IO, should not throw this exception!");
        }
    }

    // Test general case
    @Test
    void testGeneralSetup() {
        testEnigma.retrieveRotor(1, "Rotor_A");
        testEnigma.retrieveRotor(1, "ROTOR_DEFAULT");
        testEnigma.encrypt("W");
        testEnigma.encrypt("X");
        testEnigma.encrypt("Y");
        testEnigma.encrypt("Z");
        try {
            final String FILE = "./data/testJsonFiles/testGeneralSave.json";
            testJsonSaver = new JsonSaver(FILE);
            testJsonSaver.open();
            testJsonSaver.write(testEnigma);
            testJsonSaver.close();

            // Test by reading it
            JsonLoader testJsonLoader = new JsonLoader(FILE);
            Enigma loadEnigma = testJsonLoader.read();
            assertEquals(testEnigma.getOriginalMessage(), loadEnigma.getOriginalMessage());
            assertEquals(testEnigma.getEncryptedMessage(), loadEnigma.getEncryptedMessage());
            assertEquals(testEnigma.getRotorBox().getRotorNames(),
                    loadEnigma.getRotorBox().getRotorNames());
            for (int i = 0; i < testEnigma.getRotorsToUse().size(); i += 1) {
                assertEquals(testEnigma.getRotorsToUse().get(i).getName(),
                        loadEnigma.getRotorsToUse().get(i).getName());
            }
            assertEquals(4, loadEnigma.getInverter().getNumOfIO());
            assertEquals(testEnigma.getRotorsToUse().size(), loadEnigma.getRotorsToUse().size());
            assertEquals(testEnigma.getRotorsToUse().get(0).getRotation(),
                    loadEnigma.getRotorsToUse().get(0).getRotation());
            assertEquals(testEnigma.getRotorsToUse().get(1).getRotation(),
                    loadEnigma.getRotorsToUse().get(1).getRotation());
        } catch (FileNotFoundException e) {
            fail("Proper file exists, should not throw this exception!");
        } catch (IOException e) {
            fail("Proper IO, should not throw this exception!");
        }
    }
}

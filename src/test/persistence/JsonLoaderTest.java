package persistence;

import model.Enigma;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.JsonLoader;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonLoaderTest {
    private JsonLoader testJsonLoader;

    // Test invalid file source
    @Test
    void testInvalidSource() {
        try {
            final String FILE = "./data/testJsonFiles/testFile.json";
            testJsonLoader = new JsonLoader(FILE);
            testJsonLoader.read();
            fail("Should not pass!");
        } catch (IOException e) {
            System.out.println("Invalid file source!");
        }
    }

    // Test Default file
    @Test
    void testDefaultFile() {
        try {
            final String FILE = "./data/testJsonFiles/testDefaultLoad.json";
            JsonLoader testJsonLoader = new JsonLoader(FILE);
            Enigma loadEnigma = testJsonLoader.read();

            // Test
            assertEquals("", loadEnigma.getOriginalMessage());
            assertEquals("", loadEnigma.getEncryptedMessage());
            assertEquals(0, loadEnigma.getRotorsToUse().size());
            assertEquals(4, loadEnigma.getInverter().getNumOfIO());
            assertEquals("default", loadEnigma.getRotorBox().getRotorNames().get(0));
            assertEquals(1, loadEnigma.getRotorBox().getRotors().size());
        } catch (IOException e) {
            fail("IO normal, should not throw this exception!");
        }
    }

    // Test general file
    @Test
    void testGeneralFile() {
        try {
            final String FILE = "./data/testJsonFiles/testGeneralLoad.json";
            JsonLoader testJsonLoader = new JsonLoader(FILE);
            Enigma loadEnigma = testJsonLoader.read();

            // Test
            assertEquals("WXYZ", loadEnigma.getOriginalMessage());
            assertEquals("ZYXW", loadEnigma.getEncryptedMessage());
            assertEquals(2, loadEnigma.getRotorsToUse().size());
            assertEquals(4, loadEnigma.getInverter().getNumOfIO());
            assertEquals("default", loadEnigma.getRotorBox().getRotorNames().get(0));
            assertEquals("Rotor_A", loadEnigma.getRotorsToUse().get(0).getName());
            assertEquals("ROTOR_DEFAULT", loadEnigma.getRotorsToUse().get(1).getName());
            assertEquals(1, loadEnigma.getRotorBox().getRotors().size());
            assertEquals(0, loadEnigma.getRotorsToUse().get(0).getRotation());
            assertEquals(1, loadEnigma.getRotorsToUse().get(1).getRotation());
        } catch (IOException e) {
            fail("IO normal, should not throw this exception!");
        }
    }
}

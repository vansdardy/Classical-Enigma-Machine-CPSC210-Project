package persistence;

import model.*;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Provides a way to store the state of the program in a .json file
 * to be loaded later
 * Citation: the general structure is modeled upon the project -
 * JsonSerializationDemo
 */
public class JsonSaver {
    private static final int TAB = 4;
    private String fileDestination = "";
    private PrintWriter writer;
    private EventLog eventLogger = EventLog.getInstance();

    // EFFECTS: construct a JsonSaver to save the state of Enigma to a json file
    public JsonSaver(String fileDestination) {
        this.fileDestination = fileDestination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(this.fileDestination));
    }

    // MODIFIES: this
    // EFFECTS: save state of Enigma into .json to the proper destination
    public void write(Enigma enigma) {
        JSONObject enigmaJson = enigma.toJson();
        saveToFile(enigmaJson.toString(TAB));
        eventLogger.logEvent(new Event("The current Enigma is saved!"));
    }

    // MODIFIES: this
    // EFFECTS: writes a string to the destination file
    public void saveToFile(String json) {
        writer.print(json);
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }
}

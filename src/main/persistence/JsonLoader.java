package persistence;

import model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * Provides a way to load a previously saved .json file for current use
 * Citation: the general structure is modeled upon the project -
 * JsonSerializationDemo
 */
public class JsonLoader {

    private String fileSource = "";

    private EventLog eventLogger = EventLog.getInstance();


    // EFFECTS: constructs a reader to read source file
    public JsonLoader(String fileSource) {
        this.fileSource = fileSource;
    }

    // EFFECTS: read an enigma .json file and returns it
    // throws IOException if an error occurs reading data from file
    public Enigma read() throws IOException {
        String enigmaData = readFile(this.fileSource);
        JSONObject enigmaJson = new JSONObject(enigmaData);
        return parseEnigma(enigmaJson);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String fileSource) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(fileSource), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses Enigma from JSONObject and returns it
    private Enigma parseEnigma(JSONObject enigmaJson) {
        String originalMessage = enigmaJson.getString("original");
        String encryptedMessage = enigmaJson.getString("encrypted");
        // Components for further parsing
        JSONObject inverterJson = enigmaJson.getJSONObject("inverter");
        Inverter loadInverter = parseInverter(inverterJson);

        JSONArray ioJson = enigmaJson.getJSONArray("io");
        HashSet<String> loadIO = parseIO(ioJson);

        JSONObject plugboardJson = enigmaJson.getJSONObject("plugboard");
        Plugboard loadPlugboard = parsePlugboard(plugboardJson);

        JSONArray rotorBoxJson = enigmaJson.getJSONArray("rotorBox");
        RotorBox loadRotorBox = parseRotorBox(rotorBoxJson);

        JSONArray rotorsToUseJson = enigmaJson.getJSONArray("rotorsToUse");
        List<Rotor> loadRotorsToUse = parseRotorsToUse(rotorsToUseJson);

        eventLogger.logEvent(new Event("The Enigma Machine of previous session is loaded."));
        return new Enigma(loadPlugboard, loadInverter, loadRotorBox, loadRotorsToUse, loadIO,
                originalMessage, encryptedMessage);
    }

    /**
     * Below is to parse everything
     */

    // REQUIRES: inverterJson describes the state of an inverter
    // EFFECTS: parse JSONObject into an inverter
    private Inverter parseInverter(JSONObject inverterJson) {
        // parse inversions
        JSONObject inversionsJson = inverterJson.getJSONObject("inversions");
        Map<String, Object> inversionsJsonMap = inversionsJson.toMap();
        List<String> inversionsKeys = new ArrayList<>();
        inversionsKeys.addAll(inversionsJsonMap.keySet());
        Map<String, String> inverterInversions = new HashMap<>();
        for (String key : inversionsKeys) {
            inverterInversions.put(key, inversionsJsonMap.get(key).toString());
        }

        // parse IO
        JSONArray ioJson = inverterJson.getJSONArray("io");
        HashSet<String> inverterIO = new HashSet<>();
        for (Object io : ioJson) {
            inverterIO.add(io.toString());
        }

        Inverter loadInverter = new Inverter(inverterIO);
        return loadInverter;
    }

    // REQUIRES: plugboardJson describes the state of a plugboard
    // EFFECTS: parse JSONObject into a plugboard
    private Plugboard parsePlugboard(JSONObject plugboardJson) {
        // parse connections
        JSONObject connectionsJson = plugboardJson.getJSONObject("connections");
        Map<String, Object> connectionsJsonMap = connectionsJson.toMap();
        List<String> connectionsKeys = new ArrayList<>();
        connectionsKeys.addAll(connectionsJsonMap.keySet());
        Map<String, String> plugboardConnections = new HashMap<>();
        for (String key : connectionsKeys) {
            plugboardConnections.put(key, connectionsJsonMap.get(key).toString());
        }

        // parse IO
        JSONArray ioJson = plugboardJson.getJSONArray("io");
        HashSet<String> plugboardIO = new HashSet<>();
        for (Object io : ioJson) {
            plugboardIO.add(io.toString());
        }

        Plugboard loadPlugboard = new Plugboard(plugboardIO);
        return loadPlugboard;
    }

    // REQUIRES: ioJson describes a list of even, no-duplicate IOs.
    // EFFECTS: parse JSONArray into the Enigma IOs
    private HashSet<String> parseIO(JSONArray ioJson) {
        HashSet<String> io = new HashSet<>();
        for (Object inputOutput : ioJson) {
            io.add(inputOutput.toString());
        }

        return io;
    }

    // REQUIRES: rotorBoxJson describes the state of a rotor box
    // EFFECTS: parse JSONArray into the Enigma's rotor box
    private RotorBox parseRotorBox(JSONArray rotorBoxJson) {
        List<Rotor> rotorsInBox = new ArrayList<>();
        for (Object rotor : rotorBoxJson) {
            JSONObject rotorJson = (JSONObject) rotor;
            rotorsInBox.add(parseRotor(rotorJson));
        }

        RotorBox loadRotorBox = new RotorBox(rotorsInBox);
        return loadRotorBox; // stub
    }


    // REQUIRES: rotorsToUseJson describes the state of a list of rotors
    // EFFECTS: parse JSONArray into the Enigma's rotors to use list
    private List<Rotor> parseRotorsToUse(JSONArray rotorsToUseJson) {
        List<Rotor> rotorsToUse = new ArrayList<>();
        for (Object rotor : rotorsToUseJson) {
            JSONObject rotorJson = (JSONObject) rotor;
            rotorsToUse.add(parseRotor(rotorJson));
        }

        return rotorsToUse; // stub
    }

    // REQUIRES: rotorJson describes the state of a rotor
    // EFFECTS: parse JSONObject into a rotor
    private Rotor parseRotor(JSONObject rotorJson) {
        // parse name
        String name = rotorJson.getString("name");
        // parse rotation
        int rotation = rotorJson.getInt("rotation");
        // parse rotorRight for io
        JSONArray rotorRightJson = rotorJson.getJSONArray("rotorRight");
        List<String> rotorIOs = new ArrayList<>();
        for (Object io : rotorRightJson) {
            rotorIOs.add(io.toString());
        }
        // parse rotorWiring
        JSONObject rotorWiringJson = rotorJson.getJSONObject("rotorWiring");
        Map<String, Object> rotorWiringJsonMap = rotorWiringJson.toMap();
        List<String> rotorWiringKeys = new ArrayList<>();
        rotorWiringKeys.addAll(rotorWiringJsonMap.keySet());
        Map<String, String> rotorWiring = new HashMap<>();
        for (String key : rotorWiringKeys) {
            rotorWiring.put(key, rotorWiringJsonMap.get(key).toString());
        }

        Rotor loadRotor = new Rotor(name, rotorIOs, rotorWiring);
        for (int i = 0; i < rotation; i += 1) {
            loadRotor.rotate();
        }

        return loadRotor; // stub
    }
}

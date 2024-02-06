package model;

import org.json.JSONObject;

import java.util.*;

/*
The plugboard is an extra layer of encryption.
This class should contain:
    1. Mapping between two inputs
    2. Position of each input on the plugboard
Notice: The positions of the inputs are irrelevant to encryption,
        the default setup is no mappings between inputs.
 */
public class Plugboard extends JsonObjectFile implements Gadget {

    // Mappings between inputs
    private Map<String, String> connections = new HashMap<>();
    // Positions of inputs
    private List<String> io = new ArrayList<>();

    // REQUIRES: io.size() >= 2, and is even
    // MODIFIES: this
    // EFFECTS: make a plugboard with default setup
    public Plugboard(HashSet<String> io) {
        super("Plugboard");
        for (String s : io) {
            connections.put(s, s);
        }
        this.io.addAll(io);
    }

    // REQUIRES: 0 <= i < io.size()
    // EFFECTS: First re-refer inputSide and outputSide to io
    //          return the mapping of the string at index i
    @Override
    public int passThroughWire(List<String> inputSide, List<String> outputSide, int i) {
        inputSide = io;
        outputSide = io;
        String input = inputSide.get(i);
        String output = connections.get(input);
        return outputSide.indexOf(output);
    }

    // TODO: add changeElectricWiring method

    // EFFECTS: return mapping between two io
    public Map<String, String> getConnections() {
        return this.connections;
    }

    // EFFECTS: return the list of io
    public List<String> getIO() {
        return this.io;
    }

    // EFFECTS: return a JSONObject that represents a plugboard
    public JSONObject toJson() {
        JSONObject plugboardJson = new JSONObject();
        plugboardJson.put("connections", this.connections);
        plugboardJson.put("io", this.io);
        return plugboardJson;
    }
}

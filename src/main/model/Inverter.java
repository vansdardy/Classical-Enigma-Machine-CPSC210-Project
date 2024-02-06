package model;

import org.json.*;

import java.util.*;

/*
This inverter class maps half of the possible inputs to the other half
to create additional encryption.
This class should include the following information:
    1. Mapping between possible inputs
    2. Position of each input
Notice: this inverter does NOT rotate
        the default inversion rule is symmetric
 */
public class Inverter extends JsonObjectFile implements Gadget {

    // Mapping between possible inputs
    private Map<String, String> inversions = new HashMap<>();
    // Position of each possible input and output
    private List<String> io = new ArrayList<>();
    // Number of possible inputs/outputs
    private int numOfIO;

    // REQUIRES: io.size() >= 2, and io.size() is even
    // MODIFIES: this
    // EFFECTS: Initializes the inversion wiring, and initializes io positions
    public Inverter(HashSet<String> io) {
        super("Inverter");
        this.io.addAll(io);
        numOfIO = this.io.size();
        for (int i = 0; i < numOfIO; i++) {
            inversions.put(this.io.get(i), this.io.get(numOfIO - 1 - i));
        }
    }

    // REQUIRES: 0 <= i < io.size()
    // EFFECTS: inputSide and outputSide will first be re-referred to this.io
    //          return the index of output (inverted input) given the input's index
    @Override
    public int passThroughWire(List<String> inputSide, List<String> outputSide, int i) {
        inputSide = io;
        outputSide = io;
        String input = inputSide.get(i);
        String output = inversions.get(input);
        return outputSide.indexOf(output);
    }

    // TODO: add changeElectricWiring method

    // EFFECTS: return the inversion map
    public Map<String, String> getInversions() {
        return this.inversions;
    }

    // EFFECTS: return the list of possible inputs
    public List<String> getIO() {
        return this.io;
    }

    // EFFECTS: return the number of possible inputs
    public int getNumOfIO() {
        return this.numOfIO;
    }

    // EFFECTS: return a JSONObject that represents an Inverter
    public JSONObject toJson() {
        JSONObject inverterJson = new JSONObject();
        inverterJson.put("inversions", this.inversions);
        inverterJson.put("io", this.io);
        return inverterJson;
    }
}

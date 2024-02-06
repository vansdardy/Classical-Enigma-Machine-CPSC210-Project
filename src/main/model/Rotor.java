package model;

import org.json.JSONObject;

import java.util.*;

/*
 * This Rotor class is the most important in the encryption process of the Enigma Machine,
 * as each rotor will rotate to change the wiring to yield different results each time.
 * This class will comprise 2 major piece of information:
 *   1. The position of each input
 *   2. The wiring between a pair of input and output.
 * All methods in this class will act upon and modify these two fields.
 * Requires the number of inputs/outputs to be an even number.
 * */
public class Rotor extends JsonObjectFile implements Gadget {

    // Name of Rotor
    private String name;
    // Number of rotations from starting position
    private int rotation = 0;
    // Starting position of each input and output
    private List<String> rotorRight = new ArrayList<>();
    private List<String> rotorLeft = new ArrayList<>();
    // Wiring of the rotor
    private Map<String, String> rotorWiring = new HashMap<>();

    // REQUIRES: io.size() >= 2, the number of elements is even.
    //           0 <= contact < io.size()
    //           A pair of input and output should be bijective
    // MODIFIES: this
    // EFFECTS: 1. Set wiring contact position
    //          2. Initialize the accessible inputs and outputs
    //          3. Initialize the wiring between a pair of input and output
    public Rotor(String name, LinkedHashSet<String> io) {
        super("Rotor");
        this.name = name;
        this.rotorRight.addAll(io);
        this.rotorLeft.addAll(io);
        for (int i = 0; i < io.size(); i++) {
            this.rotorWiring.put(rotorRight.get(i), rotorLeft.get(i));
        }
    }

    // REQUIRES: io has an even number of elements, no duplicate elements;
    //           rotorWiring has the same elements as io
    // MODIFIES: this
    // EFFECTS: Serves as a copy constructor to copy an existing rotor's specification to a new
    //          Rotor object
    public Rotor(String name, List<String> io,
                 Map<String, String> rotorWiring) {
        super("Rotor");
        this.name = name;
        this.rotorRight = io;
        this.rotorLeft = io;
        this.rotorWiring = rotorWiring;
    }

    // TODO: Add changeElectricInputs, changeElectricOutputs and changeElectricWiring methods

    // MODIFIES: this
    // EFFECTS: Move every element in rotorRight and rotorLeft backward by one position,
    //          The first element in the list becomes the last;
    //          Increment the number of rotations by 1:
    //              If the result is equal to the size of inputs, reset to 0, return True;
    //              else, return False;
    public boolean rotate() {
        this.rotorRight.add(this.rotorRight.get(0));
        this.rotorRight.remove(0);
        this.rotorLeft.add(this.rotorLeft.get(0));
        this.rotorLeft.remove(0);
        this.rotation += 1;
        if (this.rotation == this.rotorRight.size()) {
            this.rotation = 0;
            return true;
        } else {
            return false;
        }
    }

    // REQUIRES: 0 <= i < this.electricInputs.size()
    //           inputSide is equal to one of rotorRight or rotorLeft
    //           outputSide is equal to one of rotorLeft or rotorRight
    //           inputSide and outputSide must be one of each from rotor's
    //           two sides
    // EFFECTS: return the output's index given the input's index
    public int passThroughWire(List<String> inputSide, List<String> outputSide, int i) {
        String input = inputSide.get(i);
        String output = rotorWiring.get(input);
        return outputSide.indexOf(output);
    }

    // EFFECTS: return the input at index 0
    public String display() {
        return this.rotorRight.get(0);
    }

    // EFFECTS: return the name of the rotor
    public String getName() {
        return this.name;
    }

    // EFFECTS: return the number of rotations
    public int getRotation() {
        return this.rotation;
    }

    // EFFECTS: return the electricInputs list
    public List<String> getRotorRight() {
        return this.rotorRight;
    }

    // EFFECTS: return the electricOutputs list
    public List<String> getRotorLeft() {
        return this.rotorLeft;
    }

    // EFFECTS: return the electricWiring map
    public Map<String, String> getRotorWiring() {
        return this.rotorWiring;
    }

    // REQUIRES: 0 <= i < electricInputs.size()
    // EFFECTS: return the input at some index
    public String getRightWithIndex(int i) {
        return this.rotorRight.get(i);
    }

    // REQUIRES: 0 <= i < electricOutputs.size()
    // EFFECTS: return the output at some index
    public String getLeftWithIndex(int i) {
        return this.rotorLeft.get(i);
    }

    // EFFECTS: return a JSONObject that represents a Rotor
    public JSONObject toJson() {
        JSONObject rotorJson = new JSONObject();
        rotorJson.put("name", this.name);
        rotorJson.put("rotation", this.rotation);
        rotorJson.put("rotorRight", this.rotorRight);
        rotorJson.put("rotorLeft", this.rotorLeft);
        rotorJson.put("rotorWiring", this.rotorWiring);
        return rotorJson;
    }
}

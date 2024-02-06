package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/*
This class contains the main body of the machine, and includes methods
that allow users have a more direct setup and use of the machine.
This class should contain the following information:
    1. A plugboard
    2. An inverter
    3. A rotor box to choose rotors from
    4. A list of rotors to store what rotors are chosen
       so that the original rotors in the rotor box would
       not be modified
This class should be the one for user to access adding rotors to the
rotor box.
 */
public class Enigma {

    // One plugboard
    private Plugboard plugboard;
    // One inverter (reflector)
    private Inverter inverter;
    // One rotor box to choose rotors from
    private RotorBox rotorBox;
    // Store used rotors
    private List<Rotor> rotorsToUse = new ArrayList<>();
    // Available inputs and outputs
    private HashSet<String> io;
    // Original Message
    private String originalMessage = "";
    // Encrypted Message
    private String encryptedMessage = "";

    // Accesses the EventLog singleton
    private EventLog eventLogger = EventLog.getInstance();


    // MODIFIES: this
    // EFFECTS: make a new Enigma machine with a plugboard
    //          an inverter, a rotor box, and some intended inputs
    public Enigma(HashSet<String> io) {
        this.io = io;
        initializeEnigma(io);
    }

    // REQUIRES: plugboard, inverter, rotorBox, and rotorsToUse have same inputs and outputs
    // MODIFIES: this
    // EFFECTS: load in an Enigma machine from previous states
    public Enigma(Plugboard plugboard, Inverter inverter, RotorBox rotorBox, List<Rotor> rotorsToUse,
                  HashSet<String> io, String originalMessage, String encryptedMessage) {
        this.plugboard = plugboard;
        this.inverter = inverter;
        this.rotorBox = rotorBox;
        this.rotorsToUse = rotorsToUse;
        this.io = io;
        this.originalMessage = originalMessage;
        this.encryptedMessage = encryptedMessage;
    }

    // MODIFIES: this
    // EFFECTS: initializes the Enigma machine with
    //          one default plugboard, one default inverter,
    //          and one default rotor box
    private void initializeEnigma(HashSet<String> io) {
        plugboard = new Plugboard(io);
        inverter = new Inverter(io);
        rotorBox = new RotorBox(io);
        eventLogger.logEvent(new Event("A new Enigma Machine with valid inputs of "
                                        + io + " is initialized."));
    }

    // REQUIRES: 1 <= i <= rotorBox.getRotors().size()
    // MODIFIES: this
    // EFFECTS: add copies of rotors from the rotorBox
    //          to the rotorsToUse to start encryption
    public void retrieveRotor(int i, String name) {
        // i is index of desired rotor + 1
        Rotor rotorToUse = this.copyRotor(this.rotorBox.getRotors().get(i - 1), name);
        rotorsToUse.add(rotorToUse);
        eventLogger.logEvent(new Event("A rotor named: " + rotorToUse.getName()
                + ", is added to the Enigma Machine, with the same setting as index "
                + Integer.toString(i) + " from the rotor box."));
    }

    // REQUIRES: rotorInBox in initialized rotorBox
    // EFFECTS: returns a copy of the desired rotor in the box
    private Rotor copyRotor(Rotor rotorInBox, String name) {
        String rotorName = name;
        List<String> rotorIO = rotorInBox.getRotorRight();
        Map<String, String> rotorWiring = rotorInBox.getRotorWiring();

        Rotor rotorCopy = new Rotor(rotorName, rotorIO, rotorWiring);
        return rotorCopy; // stub
    }

    // REQUIRES: s is one character in the possible inputs
    // EFFECTS: encrypt the character through the Enigma into a
    //          different character
    public String encrypt(String s) {
        originalMessage += s;
        String encrypted = "";
        boolean emptyRotors = (this.rotorsToUse.size() == 0);

        int indexOfS = this.plugboard.getIO().indexOf(s);
        int plugboardOutputAIndex = passThroughPlugboard(indexOfS);
        if (emptyRotors) {
            int plugboardOutputBIndex = emptyRotorEncryption(plugboardOutputAIndex);
            encrypted = this.plugboard.getIO().get(plugboardOutputBIndex);
        } else {
            int plugboardOutputBIndex = withRotorEncryption(plugboardOutputAIndex);
            encrypted = this.plugboard.getIO().get(plugboardOutputBIndex);
            if (this.rotorsToUse.size() > 1) {
                for (int i = 0; i < this.rotorsToUse.size() - 1; i += 1) {
                    if (rotorsToUse.get(i).rotate()) {
                        rotorsToUse.get(i + 1).rotate();
                    }
                }
            } else {
                rotorsToUse.get(0).rotate();
            }
        }

        encryptedMessage += encrypted;
        return encrypted; // stub
    }

    // EFFECTS: return the plugboard of the Enigma machine
    public Plugboard getPlugboard() {
        return this.plugboard;
    }

    // EFFECTS: return the inverter
    public Inverter getInverter() {
        return this.inverter;
    }

    // EFFECTS: return the list of rotors
    public List<Rotor> getRotorsToUse() {
        eventLogger.logEvent(new Event("User viewed the rotors in use of the Enigma Machine."));
        return this.rotorsToUse;
    }

    // EFFECTS: return the rotorBox
    public RotorBox getRotorBox() {
        return this.rotorBox;
    }

    // EFFECTS: return available inputs/outputs
    public HashSet<String> getIO() {
        return this.io;
    }

    // EFFECTS: return originalMessage
    public String getOriginalMessage() {
        return this.originalMessage;
    }

    // EFFECTS: return encryptedMessage
    public String getEncryptedMessage() {
        return this.encryptedMessage;
    }

    // REQUIRES: 0 <= input < plugboard.size()
    // EFFECTS: return the index after passing through plugboard
    private int passThroughPlugboard(int input) {
        return this.plugboard.passThroughWire(null, null, input);
    }

    // REQUIRES: 0 <= input < inverter.size()
    // EFFECTS: return the index after passing through inverter
    private int passThroughInverter(int input) {
        return this.inverter.passThroughWire(null, null, input);
    }

    // REQUIRES: 0 <= i < inverter.size()
    // EFFECTS: return the index of plugboard output after inverter encryption
    private int emptyRotorEncryption(int i) {
        int inverterOutputIndex = passThroughInverter(i);
        return passThroughPlugboard(inverterOutputIndex);
    }

    // REQUIRES: 0 <= i < inverter.size()
    // EFFECTS: return the index of plugboard output after rotor-inverter encryption
    private int withRotorEncryption(int i) {
        int inputIndexRight = i;
        for (int rotorIndex = 0; rotorIndex < rotorsToUse.size(); rotorIndex += 1) {
            Rotor currentRotor = rotorsToUse.get(rotorIndex);
            inputIndexRight = currentRotor.passThroughWire(currentRotor.getRotorRight(),
                    currentRotor.getRotorLeft(), inputIndexRight);
        }
        int inverterOutput = passThroughInverter(inputIndexRight);
        int inputIndexLeft = inverterOutput;
        for (int rotorIndex = rotorsToUse.size() - 1; rotorIndex >= 0; rotorIndex -= 1) {
            Rotor currentRotor = rotorsToUse.get(rotorIndex);
            inputIndexLeft = currentRotor.passThroughWire(currentRotor.getRotorLeft(),
                    currentRotor.getRotorRight(), inputIndexLeft);
        }
        return passThroughPlugboard(inputIndexLeft);
    }

    // EFFECTS: return a JSONArray to represent the rotorsToUse
    public JSONArray rotorToUseToJson() {
        JSONArray rotorsToUseJson = new JSONArray();
        for (Rotor rotor : this.rotorsToUse) {
            JSONObject rotorJson = rotor.toJson();
            rotorsToUseJson.put(rotorJson);
        }
        return rotorsToUseJson;
    }

    // EFFECTS: return a JSONObject to represent the Enigma
    public JSONObject toJson() {
        JSONObject enigmaJson = new JSONObject();
        enigmaJson.put("plugboard", this.plugboard.toJson());
        enigmaJson.put("inverter", this.inverter.toJson());
        enigmaJson.put("rotorBox", this.rotorBox.toJson());
        enigmaJson.put("rotorsToUse", this.rotorToUseToJson());
        enigmaJson.put("io", this.io);
        enigmaJson.put("original", this.originalMessage);
        enigmaJson.put("encrypted", this.encryptedMessage);
        return enigmaJson;
    }
}

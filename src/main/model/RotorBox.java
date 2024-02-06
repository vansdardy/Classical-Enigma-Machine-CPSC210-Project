package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

/*
The rotor box will consist of one default rotor (which can be modified
at user's will). Then the rotor box can also add more rotors for future
selections.
This class consists of two pieces of information:
    1. A default rotor with its setup (provided by developer)
    2. List of rotors including user-added rotors
 */
public class RotorBox {

    // Default rotor
    private Rotor defaultRotor;
    // List of rotors
    private final List<Rotor> rotors = new ArrayList<>();

    // Access the EventLog
    private EventLog eventLogger = EventLog.getInstance();

    // MODIFIES: this
    // EFFECTS: setup a rotor box with one default rotor and
    //          add this default rotor to the rotor list
    public RotorBox(HashSet<String> io) {
        LinkedHashSet<String> rotorIO = new LinkedHashSet<>();
        rotorIO.addAll(io);
        defaultRotor = new Rotor("default", rotorIO);

        // Add default rotor to rotor list
        rotors.add(defaultRotor);
    }

    // MODIFIES: this
    // EFFECTS: setup a rotor box based on a given list of rotors
    public RotorBox(List<Rotor> rotors) {
        this.rotors.addAll(rotors);
    }

    // REQUIRES: rotor has the rotor.rotorRight, rotor.rotorLeft
    //           to have the same elements as user's indicated input
    // MODIFIES: this
    // EFFECTS: add a new rotor to the rotor box
    public void addRotors(Rotor rotor) {
        this.rotors.add(rotor);
        eventLogger.logEvent(new Event("A new rotor named: " + rotor.getName()
                                        + ", is added to the rotor box of the Enigma Machine."));
    }

    // REQUIRES: rotor exists in the list of rotors
    // MODIFIES: this
    // EFFECTS: remove an existing rotor from the rotor box,
    //          unless the rotor is the defaultRotor, then do nothing
    public void removeRotors(Rotor rotor) {
        if (rotor != defaultRotor) {
            this.rotors.remove(rotor);
        }
    }

    // EFFECTS: return the name of rotors in the order
    //          of the rotors being added
    public List<String> getRotorNames() {
        List<String> rotorNames = new ArrayList<>();
        for (Rotor rotor : this.rotors) {
            rotorNames.add(rotor.getName());
        }
        eventLogger.logEvent(new Event("User viewed rotors in the rotor box of the Enigma."));
        return rotorNames;
    }

    // EFFECTS: return the list of rotors in the order
    //          of the rotors being added
    public List<Rotor> getRotors() {
        return this.rotors;
    }

    // TODO
    // EFFECTS: return a JSONArray to represent a RotorBox
    public JSONArray toJson() {
        JSONArray rotorBoxJson = new JSONArray();
        for (Rotor rotor : rotors) {
            JSONObject rotorJson = rotor.toJson();
            rotorBoxJson.put(rotorJson);
        }
        return rotorBoxJson;
    }
}

package ui;

import model.*;
import persistence.JsonLoader;
import persistence.JsonSaver;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static java.lang.System.exit;
import static java.lang.System.load;

/*
Main method where users can interact with the Enigma machine in console.
 */
public class ClassicalEnigmaMachine {
    private static final String JSON_STORE = "./data/enigma.json";

    public static void main(String[] args) {
        Scanner userInputs = new Scanner(System.in);
        menu(userInputs);
    }

    // EFFECTS: shows a start up menu
    private static void menu(Scanner sc) {
        System.out.println("Welcome to using The Classical Enigma Machine!");
        System.out.println("A. Use the Enigma machine");
        System.out.println("B. Load previous session");
        System.out.println("C. Quit");
        String input = sc.next();
        switch (input) {
            case "A":
                enigma(sc);
            case "B":
                Enigma loadedEnigma = loadEnigma(sc);
                if (loadedEnigma != null) {
                    enigmaUserInteraction(sc, loadedEnigma);
                } else {
                    menu(sc);
                }
            case "C":
                exit(0);
        }
    }

    // EFFECTS: start Enigma initialization process indication
    private static void enigma(Scanner sc) {
        System.out.println("\nInitializing Enigma Machine in process...");
        System.out.println("What characters do you wish to interact with?");
        System.out.println("Please enter even number of different characters (>= 2). Enter characters 1 by 1.");
        System.out.println("Your inputs will be ordered automatically.");
        System.out.println("Enter STOP to stop inputting.");
        Enigma classicalEnigmaMachine = initializeEnigma(sc);
        System.out.println("Input completed! \n");
        enigmaUserInteraction(sc, classicalEnigmaMachine);
    }

    // EFFECTS: initializes a new Enigma to use
    private static Enigma initializeEnigma(Scanner sc) {
        HashSet<String> enigmaIO = new HashSet<>();
        boolean endInput = false;
        String userInput = "";
        while (!endInput) {
            userInput = sc.next();
            if (!userInput.equals("STOP")) {
                enigmaIO.add(userInput);
            } else {
                endInput = true;
            }
        }
        Enigma enigma = new Enigma(enigmaIO);
        return enigma;
    }

    // EFFECTS: shows a menu to interact with the Enigma machine
    private static void enigmaUserInteraction(Scanner sc, Enigma enigma) {
        enigmaUserInteractionMenu();
        String userInput = "";
        userInput = sc.next();
        switch (userInput) {
            case "A":
                // TODO: update to incorporate change wiring
                System.out.println(enigma.getRotorBox().getRotorNames() + "\n");
                enigmaUserInteraction(sc, enigma);
            case "B":
                Rotor addedRotor = addRotor(sc, enigma);
                enigma.getRotorBox().addRotors(addedRotor);
                System.out.println("You may view your newly added rotor now! \n");
                enigmaUserInteraction(sc, enigma);
            case "C":
                enigmaEncrypt(sc, enigma);
            case "D":
                // TODO: Update later
                saveEnigma(sc, enigma);
                enigmaUserInteraction(sc, enigma);
            case "E":
                menu(sc);
        }
    }

    // EFFECTS: actual Enigma ui menu
    private static void enigmaUserInteractionMenu() {
        System.out.println("What do you wish to do next?");
        System.out.println("A. View available rotors in the rotor box");
        System.out.println("B. Add a new rotor to the current rotor box");
        System.out.println("C. Encrypt a message");
        System.out.println("D. Save current state of the machine");
        System.out.println("E. Return to menu");
    }

    // EFFECTS: allow users to add a rotor to the rotor box
    private static Rotor addRotor(Scanner sc, Enigma enigma) {
        LinkedHashSet<String> rotorIO = new LinkedHashSet<>();
        String rotorName = "";
        String userInput = "";
        boolean endInput = false;

        System.out.println("\nGive your rotor a name!");
        Scanner nameInput = new Scanner(System.in).useDelimiter("\n");
        rotorName = nameInput.next();

        System.out.println("Enter the inputs of the rotor in the ORDER you desire.");
        System.out.println("Your inputs should match with the inputs initialized in Enigma.");
        System.out.println("Enter characters 1 by 1, enter STOP to stop inputting.");

        while (!endInput) {
            userInput = sc.next();
            if (!userInput.equals("STOP")) {
                rotorIO.add(userInput);
            } else {
                endInput = true;
            }
        }

        Rotor newRotor = new Rotor(rotorName, rotorIO);
        return newRotor;
    }

    // EFFECTS: allow users to choose which encryption mode they desire
    private static void enigmaEncrypt(Scanner sc, Enigma enigma) {
        System.out.println("\nWhich mode do you wish to use?");
        System.out.println("A. No rotors (Very limited security) / No more rotors, if loaded from previous session");
        System.out.println("B. With rotors / With more rotors, if loaded from previous session");
        System.out.println("C. Return to previous menu");
        String userInput = sc.next();
        switch (userInput) {
            case "A":
                noRotor(sc, enigma);
            case "B":
                withRotor(sc, enigma);
            case "C":
                enigmaUserInteraction(sc, enigma);
        }
    }

    // EFFECTS: no rotor mode encryption
    private static void noRotor(Scanner sc, Enigma enigma) {
        boolean endInput = false;
        String userInput = "";

        System.out.println("Start entering your message character by character.");
        System.out.println("Enter STOP to stop encryption.");
        while (!endInput) {
            userInput = sc.next();
            if (!userInput.equals("STOP")) {
                String encrypted = enigma.encrypt(userInput);
                System.out.println("Your encrypted character is " + encrypted);
            } else {
                endInput = true;
            }
        }
        System.out.println(enigma.getOriginalMessage() + " is encrypted to " + enigma.getEncryptedMessage());
        System.out.println("Message encrypted, return to Enigma menu. \n");
        enigmaUserInteraction(sc, enigma);
    }

    // EFFECTS: has rotor mode encryption
    private static void withRotor(Scanner sc, Enigma enigma) {
        System.out.println("\nHere is a list of rotors in the rotor box " + enigma.getRotorBox().getRotorNames());
        System.out.println("Choose the rotor you need by inputting the index of the rotor.");
        System.out.println("You may retrieve the same rotor multiple times, but the order is preserved.");
        System.out.println("Enter -1 to stop rotor retrieval");
        boolean endInput = false;
        int userInput = -1;
        while (!endInput) {
            System.out.println("Retrieving rotor...");
            userInput = sc.nextInt();
            if (userInput != -1) {
                System.out.println("Give it a name as well.");
                String rotorName = (new Scanner(System.in)).useDelimiter("\n").next();
                enigma.retrieveRotor(userInput, rotorName);
            } else {
                endInput = true;
            }
        }
        noRotor(sc, enigma);
    }

    // EFFECTS: save the state of the Enigma machine as a .json file
    private static void saveEnigma(Scanner sc, Enigma enigma) {
        System.out.println("Saving current state of Enigma to .json file...");
        JsonSaver saveEnigma = new JsonSaver(JSON_STORE);
        try {
            saveEnigma.open();
            saveEnigma.write(enigma);
            saveEnigma.close();
        } catch (FileNotFoundException e) {
            System.out.println("File destination not found, returning to previous menu...\n");
            enigmaUserInteraction(sc, enigma);
        }
        System.out.println("Your state of Enigma is saved at " + JSON_STORE + "\n");
    }

    // REQUIRES: the .json file describes the state of an Enigma
    // EFFECTS: load the saved state of the Enigma machine from a .json file
    private static Enigma loadEnigma(Scanner sc) {
        System.out.println("Loading the state of Enigma from previous session...");
        JsonLoader loadEnigma = new JsonLoader(JSON_STORE);
        Enigma enigma;
        try {
            enigma = loadEnigma.read();
            System.out.println("Previous state of Enigma loaded!\n");
            return enigma;
        } catch (IOException e) {
            System.out.println("Fail to read file!");
            return null;
        }
    }
}

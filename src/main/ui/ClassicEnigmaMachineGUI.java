package ui;

import model.*;
import model.Event;
import persistence.JsonLoader;
import persistence.JsonSaver;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

import static java.lang.System.*;

/**
 * Main method used to run GUI
 */
public class ClassicEnigmaMachineGUI implements ActionListener {

    private static final int WIDTH = 960;
    private static final int HEIGHT = 540;
    private static JFrame APPFRAME = new JFrame("Classic Enigma Machine");

    private JTextField textInput = new JTextField();
    private JButton confirm = new JButton("Confirm");
    private JDialog msgDialog = new JDialog(APPFRAME);

    private HashSet<String> enigmaIO = new HashSet<>();
    private LinkedHashSet<String> rotorIO = new LinkedHashSet<>();

    private static final String ENIGMA_LOGO_PATH = "./data/images/enigma-logo-scaled.png";
    private static final String JSON_STORE = "./data/enigma.json";

    private Enigma enigma;
    private String newRotorName = "";

    private EventLog eventLogger = EventLog.getInstance();

    // EFFECTS: run the GUI of the Enigma machine
    public static void main(String[] args) {
        new ClassicEnigmaMachineGUI();
    }

    // EFFECTS: show up welcome screen
    public ClassicEnigmaMachineGUI() {
        welcomeScreen(APPFRAME);
    }

    /**
     * ******************************************************************
     * Welcome Scene
     */

    // MODIFIES: this
    // EFFECTS: set up welcome screen and display
    private void welcomeScreen(JFrame frame) {
        clear(frame);

        frame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridLayout(3, 1));

        frame.add(setupWelcomeScreenButton());
        frame.add(setupWelcomeScreenLogo(frame));
        frame.add(setupWelcomeScreenTextArea(frame));

        frame.setVisible(true);
    }

    // EFFECTS: returns panel for buttons on the welcome screen
    private JPanel setupWelcomeScreenButton() {
        JButton encrypt = new JButton("Encrypt!");
        encrypt.addActionListener(this);
        encrypt.setActionCommand("encrypt");

        JButton load = new JButton("Load Previous Session");
        load.addActionListener(this);
        load.setActionCommand("load");

        JButton exit = new JButton("Exit");
        exit.addActionListener(this);
        exit.setActionCommand("exit");

        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new GridLayout(1, 3));
        welcomePanel.add(encrypt);
        welcomePanel.add(load);
        welcomePanel.add(exit);

        return welcomePanel;
    }

    // EFFECTS: return panel for text on welcome screen
    private JPanel setupWelcomeScreenTextArea(JFrame frame) {
        JTextArea title = new JTextArea("Classic Enigma Machine");
        title.setFont(new Font("Courier", Font.BOLD, 36));
        title.setBackground(frame.getBackground());
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new GridBagLayout());
        titlePanel.add(title);

        return titlePanel;
    }

    // EFFECTS: return label for image on welcome screen
    private JLabel setupWelcomeScreenLogo(JFrame frame) {
        BufferedImage enigmaLogo;
        ImageIcon enigmaIcon = new ImageIcon();
        try {
            enigmaLogo = ImageIO.read(new File(ENIGMA_LOGO_PATH));
            enigmaIcon = new ImageIcon(enigmaLogo);
        } catch (IOException e) {
            out.println("PATH NOT FOUND!");
            welcomeScreen(frame);
        }

        return new JLabel(enigmaIcon);
    }

    // EFFECTS: Switch to different scenes from welcome screen
    private void welcomeScreenButtonCommands(String command) {
        switch (command) {
            case "encrypt":
                encryptSetupScreen(APPFRAME);
                break;
            case "load":
                enigma = loadEnigma();
                encryptActionScreen(APPFRAME);
                break;
            case "exit":
                for (Event e : eventLogger) {
                    out.println(e.toString() + "\n");
                }
                exit(0);
        }
    }

    /**
     * ******************************************************************
     * Scene to setup a new Enigma machine
     */

    // MODIFIES: this
    // EFFECT: setup and display screen to setup new Enigma Machine
    private void encryptSetupScreen(JFrame frame) {
        clear(frame);

        frame.setLayout(new GridLayout(2, 1));
        JTextArea encryptPreparation = new JTextArea();

        frame.add(encryptPreparation);
        frame.setVisible(true);

        setEncryptTextArea(encryptPreparation);

        encryptSetup(frame);
    }

    // EFFECTS: set up instructions in text on how to setup Enigma
    private void setEncryptTextArea(JTextArea textArea) {
        textArea.append("Initializing Enigma Machine in process...\n");
        textArea.append("What characters do you wish to interact with?\n");
        textArea.append("Please enter even number of different characters (>= 2). "
                + "Enter characters 1 by 1.\n");
        textArea.append("Your inputs will be ordered automatically.\n");
        textArea.append("Enter STOP to stop inputting.\n");

        textArea.setEditable(false);
        textArea.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
    }

    // MODIFIES: this
    // EFFECTS: setup text field and buttons to allow users to set up Enigma
    private void encryptSetup(JFrame frame) {
        JPanel encryptSetupPanel = new JPanel();
        encryptSetupPanel.setLayout(new GridLayout(1, 2));

        textInput.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        confirm.addActionListener(this);
        confirm.setActionCommand("encrypt-setup");

        encryptSetupPanel.add(textInput);
        encryptSetupPanel.add(confirm);

        frame.add(encryptSetupPanel);
    }

    /**
     * ******************************************************************
     * Scene with buttons to start encryption
     */

    // MODIFIES: this
    // EFFECTS: setup screen for actions after setting up Enigma / loading from previous session
    private void encryptActionScreen(JFrame frame) {
        clear(frame);

        frame.setLayout(new FlowLayout());
        frame.add(setupEncryptActionButtons());

        frame.setVisible(true);
    }

    // EFFECTS: returns a panel that has buttons for actions
    private JPanel setupEncryptActionButtons() {
        JButton availRotor = new JButton("View Available Rotors");
        JButton addRotor = new JButton("Add New Rotor to Rotor Box");
        JButton encrypt = new JButton("Encrypt Message");
        JButton save = new JButton("Save current state");
        JButton returnToMenu = new JButton("Return to Home Menu");

        availRotor.addActionListener(this);
        addRotor.addActionListener(this);
        encrypt.addActionListener(this);
        save.addActionListener(this);
        returnToMenu.addActionListener(this);

        availRotor.setActionCommand("view-rotor");
        addRotor.setActionCommand("add-rotor");
        encrypt.setActionCommand("encrypt-msg");
        save.setActionCommand("save");
        returnToMenu.setActionCommand("return-to-menu");

        JPanel encryptionActionPanel = new JPanel();
        encryptionActionPanel.setLayout(new GridLayout(1, 5));
        encryptionActionPanel.add(availRotor);
        encryptionActionPanel.add(addRotor);
        encryptionActionPanel.add(encrypt);
        encryptionActionPanel.add(save);
        encryptionActionPanel.add(returnToMenu);

        return encryptionActionPanel;
    }

    // EFFECTS: switch screen depending on which action users decide to interact with
    private void encryptActionButtonCommands(String command) {
        switch (command) {
            case "view-rotor":
                chooseView();
                break;
            case "add-rotor":
                chooseAddView();
                break;
            case "encrypt-msg":
                break;
            case "save":
                saveState(enigma);
                encryptActionScreen(APPFRAME);
                break;
            case "return-to-menu":
                welcomeScreen(APPFRAME);
                break;
        }
    }

    /**
     * ******************************************************************
     * Scene to choose to view available rotors in the Rotor Box / Enigma Machine
     */

    // MODIFIES: this
    // EFFECTS: set up dialog to allow users to choose which type of rotors to view
    private void chooseView() {
        updateMsgDialog();
        msgDialog.setLayout(new GridLayout(2, 1));

        JPanel choiceTextPanel = new JPanel();
        choiceTextPanel.setLayout(new GridBagLayout());
        JTextArea choiceText = new JTextArea("Choose From Below");
        choiceText.setFont(new Font("Courier", Font.BOLD, 24));
        choiceText.setBackground(choiceTextPanel.getBackground());
        choiceTextPanel.add(choiceText);
        msgDialog.add(choiceTextPanel);

        JPanel choice = new JPanel();
        choice.setLayout(new GridLayout(2, 1));
        JButton viewRotorBox = new JButton("View Rotors in Rotor Box");
        JButton viewRotorToUse = new JButton("View Rotors in Enigma Machine (Chosen from Rotor Box)");
        viewRotorBox.addActionListener(this);
        viewRotorToUse.addActionListener(this);
        viewRotorBox.setActionCommand("view-rotor-box");
        viewRotorToUse.setActionCommand("view-rotor-in-use");
        choice.add(viewRotorBox);
        choice.add(viewRotorToUse);
        msgDialog.add(choice);

        msgDialog.setVisible(true);
    }

    // EFFECTS: switch screens depending on which choice users choose
    private void viewChoiceButtonCommand(String command) {
        switch (command) {
            case "view-rotor-box":
                msgDialog.removeAll();
                msgDialog.setVisible(false);
                viewRotorsInRotorBox();
                break;
            case "view-rotor-in-use":
                msgDialog.removeAll();
                msgDialog.setVisible(false);
                viewRotorsInRotorToUse();
                break;
        }
    }

    /**
     * ******************************************************************
     * Scene to view rotors
     */

    // MODIFIES: this
    // EFFECTS: screen that shows rotors in the rotor box
    private void viewRotorsInRotorBox() {
        updateMsgDialog();

        List<String> rotorsInRotorBox = enigma.getRotorBox().getRotorNames();
        int numOfRotors = rotorsInRotorBox.size();

        msgDialog.setLayout(new GridLayout(numOfRotors + 1, 1));
        for (int i = 0; i < numOfRotors; i += 1) {
            JPanel tempPanel = new JPanel();
            tempPanel.setLayout(new GridBagLayout());
            JLabel rotorLabel = new JLabel(rotorsInRotorBox.get(i));
            rotorLabel.setFont(new Font("Times New Roman", Font.PLAIN, 24));
            tempPanel.add(rotorLabel);
            msgDialog.add(tempPanel);
        }
        JButton close = new JButton("Close");
        close.addActionListener(this);
        close.setActionCommand("close-dialog");
        msgDialog.add(close);

        msgDialog.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: screen that shows rotors in the Enigma Machine
    private void viewRotorsInRotorToUse() {
        updateMsgDialog();

        List<Rotor> rotorsInRotorToUse = enigma.getRotorsToUse();
        int numOfRotors = rotorsInRotorToUse.size();

        msgDialog.setLayout(new GridLayout(numOfRotors + 1, 1));
        for (int i = 0; i < numOfRotors; i += 1) {
            JPanel tempPanel = new JPanel();
            tempPanel.setLayout(new GridBagLayout());
            JLabel rotorLabel = new JLabel(rotorsInRotorToUse.get(i).getName());
            rotorLabel.setFont(new Font("Times New Roman", Font.PLAIN, 24));
            tempPanel.add(rotorLabel);
            msgDialog.add(tempPanel);
        }
        JButton close = new JButton("Close");
        close.addActionListener(this);
        close.setActionCommand("close-dialog");
        msgDialog.add(close);

        msgDialog.setVisible(true);
    }

    /**
     * ******************************************************************
     * Scene to choose where to add rotors
     */

    // MODIFIES: this
    // EFFECTS: screen where users have choices to add rotors to rotor box or the machine
    private void chooseAddView() {
        updateMsgDialog();
        msgDialog.setLayout(new GridLayout(2, 1));

        JPanel choiceTextPanel = new JPanel();
        choiceTextPanel.setLayout(new GridBagLayout());
        JTextArea choiceText = new JTextArea("Choose From Below");
        choiceText.setFont(new Font("Courier", Font.BOLD, 24));
        choiceText.setBackground(choiceTextPanel.getBackground());
        choiceTextPanel.add(choiceText);
        msgDialog.add(choiceTextPanel);

        JPanel choice = new JPanel();
        choice.setLayout(new GridLayout(2, 1));
        JButton addToRotorBox = new JButton("Add Rotors to Rotor Box");
        JButton addToRotorToUse = new JButton("Add Rotors to Enigma Machine (Chosen from Rotor Box)");
        addToRotorBox.addActionListener(this);
        addToRotorToUse.addActionListener(this);
        addToRotorBox.setActionCommand("add-to-rotor-box");
        addToRotorToUse.setActionCommand("add-to-rotor-in-use");
        choice.add(addToRotorBox);
        choice.add(addToRotorToUse);
        msgDialog.add(choice);

        msgDialog.setVisible(true);
    }

    // EFFECTS: switch screens according to users' choices on adding rotors
    private void addChoiceButtonCommand(String command) {
        switch (command) {
            case "add-to-rotor-box":
                msgDialog.removeAll();
                msgDialog.setVisible(false);
                addRotorsToRotorBox();
                break;
            case "add-to-rotor-in-use":
                msgDialog.removeAll();
                msgDialog.setVisible(false);
                addRotorsToRotorsToUse();
                break;
        }
    }

    /**
     * ******************************************************************
     * Scene to add rotors
     */

    // MODIFIES: this
    // EFFECTS: screen that allows user to give name to a new rotor about to be added to the rotor box
    private void addRotorsToRotorBox() {
        updateMsgDialog();
        msgDialog.setLayout(new GridLayout(2, 1));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridBagLayout());
        JTextArea nameText = new JTextArea("Give the new rotor a name!");
        nameText.setFont(new Font("Times New Roman", Font.BOLD, 24));
        nameText.setBackground(textPanel.getBackground());
        nameText.setEditable(false);
        textPanel.add(nameText);
        msgDialog.add(textPanel);

        JPanel namePanel = new JPanel();
        namePanel.setLayout(new GridBagLayout());
        textInput = new JTextField(20);
        JButton confirm = new JButton("Confirm");
        confirm.addActionListener(this);
        confirm.setActionCommand("confirm-rotor-name");
        namePanel.add(textInput);
        namePanel.add(confirm);
        msgDialog.add(namePanel);
    }

    // MODIFIES: this
    // EFFECTS: screen where users choose rotors from the rotor box to put into the machine
    private void addRotorsToRotorsToUse() {
        updateMsgDialog();
        msgDialog.setLayout(new GridLayout(3, 1));

        addRotorChooseFromBoxText();
        addRotorNameText();
        addIndexPanel();

        msgDialog.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: setup text instructions for user choosing to add rotors to the rotor box
    private void addRotorChooseFromBoxText() {
        JTextArea rotorChooseFromBoxText = new JTextArea("Choose from the rotors in the rotor box by index!\n");
        rotorChooseFromBoxText.append("You may retrieve the same rotor multiple times, but the order is preserved.\n");
        rotorChooseFromBoxText.append("Enter -1 to stop rotor retrieval\n");
        rotorChooseFromBoxText.setFont(new Font("Times New Roman", Font.BOLD, 16));
        rotorChooseFromBoxText.setEditable(false);
        rotorChooseFromBoxText.setBackground(msgDialog.getBackground());
        msgDialog.add(rotorChooseFromBoxText);
    }

    // MODIFIES: this
    // EFFECTS: List out all the rotors in the rotor box so far
    private void addRotorNameText() {
        JTextArea rotorNameText = new JTextArea();
        rotorNameText.setFont(new Font("Times New Roman", Font.BOLD, 16));
        rotorNameText.setEditable(false);
        rotorNameText.setBackground(msgDialog.getBackground());
//        for (String rotorName : enigma.getRotorBox().getRotorNames()) {
//            rotorNameText.append(rotorName + "\n");
//        }
        for (Rotor rotor : enigma.getRotorBox().getRotors()) {
            rotorNameText.append(rotor.getName() + "\n");
        }
        msgDialog.add(rotorNameText);
    }

    // MODIFIES: this
    // EFFECTS: have a panel where users can input index of the rotor they wish to choose
    private void addIndexPanel() {
        JPanel indexPanel = new JPanel();
        indexPanel.setLayout(new GridBagLayout());
        textInput = new JTextField(20);
        JButton confirm = new JButton("Confirm");
        confirm.addActionListener(this);
        confirm.setActionCommand("confirm-rotor-index");
        indexPanel.add(textInput);
        indexPanel.add(confirm);
        msgDialog.add(indexPanel);
    }

    /**
     * ******************************************************************
     * Scene to set up new rotor's input
     */

    // MODIFIES: this
    // EFFECTS: set up screen for users to input the desired inputs for the new rotor
    private void addNewRotorInput(JFrame frame) {
        clear(frame);

        frame.setLayout(new GridLayout(2, 1));
        JTextArea inputInstructions = new JTextArea();

        frame.add(inputInstructions);
        frame.setVisible(true);

        setInputInstructions(inputInstructions);

        rotorSetup(frame);
    }

    // EFFECTS: gives text instruction on how to add inputs for the new rotor
    private void setInputInstructions(JTextArea textArea) {
        textArea.append("Enter the inputs of the rotor in the ORDER you desire.\n");
        textArea.append("Your inputs should match with the inputs initialized in Enigma.\n");
        textArea.append("Enter characters 1 by 1, enter STOP to stop inputting.\n");

        textArea.setEditable(false);
        textArea.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
    }

    // MODIFIES: this
    // EFFECTS: gives an panel where users can interact with inputting and confirming the inputs
    private void rotorSetup(JFrame frame) {
        JPanel rotorSetupPanel = new JPanel();
        rotorSetupPanel.setLayout(new GridLayout(1, 2));

        textInput = new JTextField();
        textInput.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        confirm.addActionListener(this);
        confirm.setActionCommand("rotor-setup");

        rotorSetupPanel.add(textInput);
        rotorSetupPanel.add(confirm);

        frame.add(rotorSetupPanel);
    }

    private void setupInputInstructionsText(JTextArea textArea) {

    }

    /**
     * ******************************************************************
     * Dynamic Update of the Frame
     */

    // EFFECTS: determine which command to run based on the button click
    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        welcomeScreenCommand(actionCommand);
        encryptSetupCommand(actionCommand);
        encryptActionCommand(actionCommand);
        saveCommand(actionCommand);
        viewChoiceCommand(actionCommand);
        dialogCommand(actionCommand);
        addChoiceCommand(actionCommand);
        addRotorCommand(actionCommand);
        newRotorInputCommand(actionCommand);
    }

    // MODIFIES: this
    // EFFECTS: clear the frame, so that frame can be switched to new functionalities
    private void clear(JFrame frame) {
        frame.getContentPane().removeAll();
        frame.setVisible(false);
    }

    // MODIFIES: this
    // EFFECTS: refresh a new dialog with no components for new functionalities
    private void updateMsgDialog() {
        msgDialog = new JDialog(APPFRAME);
        msgDialog.setMinimumSize(new Dimension(WIDTH / 2, HEIGHT / 2));
        msgDialog.setLocationRelativeTo(null);
        msgDialog.setVisible(true);
    }

    /**
     * ******************************************************************
     * Save and Load
     */

    // MODIFIES: this
    // EFFECTS: saves the current state of Enigma to the designated file path
    private void saveState(Enigma enigma) {
        updateMsgDialog();
        msgDialog.setLayout(new GridBagLayout());

        JsonSaver saveEnigma = new JsonSaver(JSON_STORE);
        try {
            saveEnigma.open();
            saveEnigma.write(enigma);
            saveEnigma.close();
        } catch (FileNotFoundException e) {
            msgDialog.add(new JTextArea("File Path Not Found!"));
            JButton retry = new JButton("Retry");
            retry.addActionListener(this);
            retry.setActionCommand("retry-save");
            msgDialog.add(retry);
        }
        msgDialog.add(new JTextArea("File Saved at " + JSON_STORE));
        JButton ok = new JButton("OK");
        ok.addActionListener(this);
        ok.setActionCommand("ok-save");
        msgDialog.add(ok);

        msgDialog.setVisible(true);
    }

    // EFFECTS: load an Enigma from previously saved file
    private Enigma loadEnigma() {
        JsonLoader loadEnigma = new JsonLoader(JSON_STORE);
        Enigma enigma;
        try {
            enigma = loadEnigma.read();
            return enigma;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * ******************************************************************
     * Action Performed Helper Functions
     */

    // EFFECTS: commands for welcome screen button
    private void welcomeScreenCommand(String actionCommand) {
        if (actionCommand.equals("encrypt") || actionCommand.equals("load") || actionCommand.equals("exit")) {
            welcomeScreenButtonCommands(actionCommand);
        }
    }

    // EFFECTS: commands for confirm button at setting up new Enigma
    private void encryptSetupCommand(String actionCommand) {
        if (actionCommand.equals("encrypt-setup")) {
            String textFieldInput = textInput.getText();
            if (!textFieldInput.equals("STOP")) {
                enigmaIO.add(textFieldInput);
                textInput.setText("");
            } else {
                enigma = new Enigma(enigmaIO);
                encryptActionScreen(APPFRAME);
            }
        }
    }

    // EFFECTS: commands for buttons after loading or setup
    private void encryptActionCommand(String actionCommand) {
        if (actionCommand.equals("view-rotor") || actionCommand.equals("add-rotor")
                || actionCommand.equals("encrypt-msg") || actionCommand.equals("save")
                || actionCommand.equals("return-to-menu")) {
            encryptActionButtonCommands(actionCommand);
        }
    }

    // EFFECTS: commands for saving the current state
    private void saveCommand(String actionCommand) {
        if (actionCommand.equals("retry-save") || actionCommand.equals("ok-save")) {
            if (actionCommand.equals("retry-save")) {
                encryptActionScreen(APPFRAME);
            }
            msgDialog.removeAll();
            msgDialog.setVisible(false);
        }
    }

    // EFFECTS: commands for choosing which criterion of rotors to look at
    private void viewChoiceCommand(String actionCommand) {
        if (actionCommand.equals("view-rotor-box") || actionCommand.equals("view-rotor-in-use")) {
            viewChoiceButtonCommand(actionCommand);
        }
    }

    // EFFECTS: commands to close a dialog window
    private void dialogCommand(String actionCommand) {
        if (actionCommand.equals("close-dialog")) {
            msgDialog.removeAll();
            msgDialog.setVisible(false);
        }
    }

    // EFFECTS: commands for choosing which location to add rotors to
    private void addChoiceCommand(String actionCommand) {
        if (actionCommand.equals("add-to-rotor-box") || actionCommand.equals("add-to-rotor-in-use")) {
            addChoiceButtonCommand(actionCommand);
        }
    }

    // EFFECTS: commands for initializing a new rotor's name, or adding a rotor from box to machine with index
    private void addRotorCommand(String actionCommand) {
        if (actionCommand.equals("confirm-rotor-name") || actionCommand.equals("confirm-rotor-index")) {
            switch (actionCommand) {
                case "confirm-rotor-name":
                    newRotorName = textInput.getText();
                    msgDialog.removeAll();
                    msgDialog.setVisible(false);
                    addNewRotorInput(APPFRAME);
                    break;
                case "confirm-rotor-index":
                    String rotorIndexString = textInput.getText();
                    int rotorIndex = Integer.parseInt(rotorIndexString);
                    if (rotorIndex != -1) {
                        enigma.retrieveRotor(rotorIndex,
                                enigma.getRotorBox().getRotors().get(rotorIndex - 1).getName());
                        textInput.setText("");
                    } else {
                        msgDialog.removeAll();
                        msgDialog.setVisible(false);
                    }
                    break;
            }
        }
    }

    // EFFECTS: commands for setting up input/output for the new rotor
    private void newRotorInputCommand(String actionCommand) {
        if (actionCommand.equals("rotor-setup")) {
            String textFieldInput = textInput.getText();
            if (!textFieldInput.equals("STOP")) {
                rotorIO.add(textFieldInput);
            } else {
                LinkedHashSet<String> newRotorIO = new LinkedHashSet<>();
                newRotorIO.addAll(rotorIO);
                Rotor newRotor = new Rotor(newRotorName, newRotorIO);
                enigma.getRotorBox().addRotors(newRotor);
                encryptActionScreen(APPFRAME);
            }
            textInput.setText("");
        }
    }
}

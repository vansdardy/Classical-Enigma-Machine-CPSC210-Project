# My Personal Project - The Classical Enigma Machine

## A Tribute to the brave Allies cryptologists, and Alan Turing

### Table of Contents:

- Project Proposal
- User Stories

### Project Proposal

An Enigma machine application will allow users to encrypt messages for communications that they wish to remain
somewhat private between two ends, reducing the risk of the message being intercepted or being exposed to unwanted
third-parties. The primary target users of this application are the people who likes code cracking as a hobby, the 
amateur cryptologists, and the people who want to maintain a low-level security for their communications.

The primary reason why this application interests me is the historical background of this machine.
The Enigma machine was primarily used by Nazi Germany during World War II to encrypt messages between the headquarters
and the local commanders on the front line. Even though it has indirectly led to many brutalities and much bloodshed,
the level at which the machine could encrypt a message was still a breakthrough to the discipline of cryptography.
The story of the machine being defeated during the war was a eulogy of wisdom and courage, thus, a tribute to these
warriors as well.

### User Stories
- Phase 0
  - As a user, I want to be able to build an Enigma machine with 1 plugboard, some rotors, and 1 inverter
    - Plugboard (Quantity: 1)
      - I want to be able to add a plugboard to my Enigma machine.
      - I want to be able to configure my plugboard to pair up alphabets.
    - Rotors (Quantity: 1 to 5)
      - I want to be able to add already configured rotors to my Enigma machine.
      - I want to be able to add already configured rotors to a "Rotor Box"
      - I want to be able to view a list of already configured rotors from the "Rotor Box".
      - I want to be able to configure the rotors at my liberty - wiring the rotors.
      - I want to be able to add DIY-configured rotors to the "Rotor Box"
    - Inverter (Quantity: 1)
      - I want to be able to add an inverter to my Enigma machine.
      - I want to be able to configure the inverter at my liberty - wiring the inverter.
    - Enigma
      - I want to encrypt message with this enigma machine.
    - Log
      - I want to be able to view the encryption process of my message.
- Phase 1
  - User Stories implemented
    - I want to be able to add already configured rotors to a "Rotor Box".
      - RotorBox.addRotor(Rotor rotor);
    - I want to be able to view a list of already configured rotors from the "Rotor Box".
      - RotorBox.getRotorNames();
      - RotorBox.getRotors();
    - I want to encrypt message with this enigma machine.
      - Enigma.encrypt(String s);
    - I want to be able to add a plugboard to my Enigma machine; I want to be able to add 
      already configured rotors to my Enigma machine; I want to be able to add an inverter to my Enigma machine.
      - Enigma.Enigma();
- Phase 2
  - Fixed incompleteness from Phase 1, all the user stories mentioned above are not fully accessible from the console.
  - New User Stories
    - I want to be able to save the entire state of the machine before I return to main menu from Enigma interaction
    menu
      - State of Enigma machine
        - Plugboard
          - io
          - Wiring
        - RotorsToUse
          - Each rotor
            - name, rotation, rotorRight, rotorLeft, rotorWiring
        - Inverter
          - io
          - Wiring
        - RotorBox
          - Each rotor
            - name, rotation, rotorRight, rotorLeft, rotorWiring
      - Original message and Encrypted message
    - I want to be able to load the entire state of the machine after I reuse Enigma interaction menu
      - Build the Enigma machine with previously saved Plugboard, RotorsToUse, Inverter, and RotorBox
      - Load previously encrypted message with its original message.
  - Implemented User stories
    - JsonSaver.write(): saves the state of current Enigma machine
    - JsonLoader.read(): loads previously saved state of the Enigma machine


- Phase 3 (GUI design)
### Instructions for Grader
- User Stories implemented in GUI
  - Instructions before use
    - Since most implementations are on the screen for encryption actions, one should access there first. This can be
achieved in two ways
      - Click "Encrypt!" after running GUI, follow setup instructions
        - Input one letter each time, and then press "Confirm"
        - Input at least 2 letters, the total number of inputs should be even
        - For testing, enter either ABCD, or WXYZ.
        - Input "STOP" to stop
      - Click "Load Previous Session"
  - Visuals
    - Enigma logo on the welcome screen
  - View rotors in rotor box
    - Click "View Rotors ...", then a popup screen should appear. Choose whichever you wish.
    - The **special** criteria is where users choose to view rotors in the machine. 
  - View rotors in machine
    - Same as above
  - Add new rotors to rotor box
    - Click "Add Rotors ...", then a popup screen should appear. Choose "... to rotor box"
      - Input one letter each time, and then press "Confirm"
      - All the inputs should match with Enigma setup
      - The inputs **do not** have to be in order
      - For testing, enter either ABCD, or WXYZ (in whatever order).
      - Input "STOP" to stop
  - Add new rotors to machine from rotor box
    - Click "Add Rotors ...", then a popup screen should appear. Choose "... to machine"
      - A list of available rotors will be given (the ones in the rotor box)
      - Input the index of the rotors, one at a time, and then press "Confirm"
      - The index is 1-indexed
      - The index inputs can **repeat**, and **maintains order**.
      - Input "-1" to stop
  - Save state of the machine
    - Click "Save", then a popup screen should appear telling user where the file is saved
  - Load previous state of the machine
    - Click "Load Previous Session" on Home/Welcome Screen (immediately after running GUI)

- Phase 4
  - Task 2
    Thu Nov 30 14:44:53 PST 2023
    The Enigma Machine of previous session is loaded.

    Thu Nov 30 14:45:14 PST 2023
    A new rotor named: ROTOR_A, is added to the rotor box of the Enigma Machine.
    
    Thu Nov 30 14:45:16 PST 2023
    User viewed rotors in the rotor box of the Enigma.
    
    Thu Nov 30 14:45:33 PST 2023
    A new rotor named: ROTOR_B, is added to the rotor box of the Enigma Machine.
    
    Thu Nov 30 14:45:39 PST 2023
    User viewed the rotors in use of the Enigma Machine.
    
    Thu Nov 30 14:45:44 PST 2023
    A rotor named: ROTOR_B, is added to the Enigma Machine, with the same setting as index 3 from the rotor box.
    
    Thu Nov 30 14:45:45 PST 2023
    A rotor named: default, is added to the Enigma Machine, with the same setting as index 1 from the rotor box.
    
    Thu Nov 30 14:45:46 PST 2023
    A rotor named: ROTOR_A, is added to the Enigma Machine, with the same setting as index 2 from the rotor box.
    
    Thu Nov 30 14:45:52 PST 2023
    User viewed the rotors in use of the Enigma Machine.
    
    Thu Nov 30 14:45:54 PST 2023
    User viewed rotors in the rotor box of the Enigma.
    
    Thu Nov 30 14:45:56 PST 2023
    The current Enigma is saved!
  
  - Task 3: Reflection and Ideas for Refactoring
    - After drawing out the UML diagram, there are certain things I instantly noticed that can be improved.
      - Let Inverter, Rotor, and Plugboard implement Gadget; but let Gadget extend JsonObjectFile. This is more logical
      as we expect the gadgets in the Enigma Machine are objects that can be stored in .json format, regardless of what
      the gadgets really are. This also reduces messy inheritance in class declarations.
      - Both persistence classes could be implementing/extending the same interface/abstract class, as both of them will
      be interacting with EventLog class.
      - In the actual program, the Inverter, Plugboard, Rotor Box, and Rotors heavily depends on the valid input/output
      for the Enigma machine itself. This is heavy coupling. This may be refactored into an Observer pattern where the
      components of the Enigma are Observers, and if the Enigma machine changes its input/output, the components would
      be causing errors/bugs everywhere.
      - The components/gadgets of the Enigma can be refactored to be robust. So far, none of the gadgets can always work
      completely without running into an exception.


package model;

import java.util.*;

/*
Gadget is an interface that has two main methods for its subtypes to implement.
These two methods are shared commonly across Rotor class and Inverter class.
 */
public interface Gadget {
    // TODO: add changeElectricWiring method
    int passThroughWire(List<String> input, List<String> output, int i);
}

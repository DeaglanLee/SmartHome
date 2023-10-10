package Household.Device.Lighting;

import java.time.LocalTime;

import Household.Device.State;


public class ChristmasMode extends Mode {

    public ChristmasMode(String name, LocalTime time, int brightness, State state){
        super(name, time, brightness, state);
    }
}

package Household.Device.Lighting;

import Household.Device.Device;
import Household.Device.DeviceType;
import Household.Device.State;
import Household.Profile.ProfileType;

/**
 * A representation of a Light that extends from {@code Device}
 */
public class Lighting extends Device {
    private static final DeviceType deviceType = DeviceType.LIGHTING;
    Mode mode = new Mode();
    byte brightness = 0;

    public Lighting(String name, String location, State state, ProfileType profileType, Mode mode, byte brightness) {
        super(name, location, deviceType, state, profileType);
        setMode(mode);
        setBrightness(brightness);
    }

    public Lighting(String name, String location, State state, ProfileType profileType) {
        super(name, location, deviceType, state, profileType);
    }

    public Lighting(String name, String location, State state) {
        super(name, location, deviceType, state);
    }

    /**
     * Should only be used when accessing csv
     * 
     * @param name
     * @param location
     * @param status
     * @param profileType
     */
    public Lighting(String name, String location, String status, ProfileType profileType) {
        super(name, location, deviceType, status, profileType);
    }

    // getters
    public Mode getMode() {
        return mode;
    }

    public int getBrightness() {
        return brightness;
    }

    // setters
    public void setMode(Mode mode) {
        if (mode.getName().equals("Christmas") || mode.getName().equals("Default")) {
            this.mode = mode;
        } else {
            throw new IllegalArgumentException("Mode Must Be \bChristmas or Default");
        }
    }

    public void setBrightness(byte brightness) {
        if (brightness > 0 && brightness < 100) {
            this.brightness = brightness;
        } else {
            throw new IllegalArgumentException("Brightness Levels Must Be Between 0 And 100");
        }

    }

    public void setState(State state) {
        if (this.brightness > 0) {
            if (state == State.ON) {
                super.status = state;
            } else {
                throw new IllegalArgumentException("State Can Only Be ON When Brightness Level is above 0");
            }
        } else if (brightness == 0) {
            super.status = state;
        } else {
            throw new IllegalArgumentException("Brightness level is at 0 State cannot be on");
        }

    }

}

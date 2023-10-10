package Household.Device.Lighting;

import java.time.LocalTime;

import Household.Device.State;

public class Mode {
    private String name;
    private LocalTime time;
    private int brightnessLevel;
    private State state;

    public Mode(String name, LocalTime time, int brightnessLevel, State state) {
        setName(name);
        setTime(time);
        setBrightness(brightnessLevel);
        setState(state);
    }

    public Mode() {

    }

    // getters
    public String getName() {
        return name;
    }

    public LocalTime getTime() {
        return time;
    }

    public int getBrightnessLevel() {
        return brightnessLevel;
    }

    public State getState() {
        return state;
    }

    // setters
    public void setName(String name) {
        if (name.length() > 8 && name.length() < 30) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Name Of The Mode should be between 8 and 30 Characters Long");
        }
    }

    public void setTime(LocalTime time) {
        try {

        } catch (Exception e) {
            throw e;
        }
        this.time = time;
    }

    public void setTime(String time) {
        setTime(LocalTime.parse(time));
    }

    public void setBrightness(int brightness) {
        if (brightness > 0 && brightness < 100) {
            this.brightnessLevel = brightness;
        } else {
            throw new IllegalArgumentException("Brightness Levels Must Be Between 0 And 100");
        }
    }

    public void setState(State state) {
        if (this.brightnessLevel > 0) {
            if (state == State.ON) {
                this.state = state;
            } else {
                throw new IllegalArgumentException("State Can Only Be ON When Brightness Level is above 0");
            }
        } else if (brightnessLevel == 0) {
            this.state = state;
        } else {
            throw new IllegalArgumentException("Brightness level is at 0 State cannot be on");
        }

    }
}

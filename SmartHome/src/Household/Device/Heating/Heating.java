package Household.Device.Heating;

import Household.Device.Device;
import Household.Device.DeviceType;
import Household.Device.State;
import Household.Profile.ProfileType;

/**
 * Heating Device Created from an Abstract Device
 */
public class Heating extends Device {
    private static DeviceType deviceType = DeviceType.HEATING;
    private HeatingSched schedule;
    private byte temp;

    /**
     * Create a Heating device that should require a profile type to interact with
     * 
     * @param name        of the Heating Device
     * @param location    of the Heating Device
     * @param status      of the Heating Device
     * @param profileType to interact with the Heating Device
     */
    public Heating(String name, String location, State status, ProfileType profileType) {
        super(name, location, deviceType, status, profileType);
    }

    /**
     * Should only be used when accessing csv
     * 
     * @param name
     * @param location
     * @param status
     * @param profileType
     */
    public Heating(String name, String location, String status, ProfileType profileType) {
        super(name, location, deviceType, status, profileType);
    }

    public Heating(String name, String location, State status) {
        super(name, location, deviceType, status);
    }

    // getters
    public HeatingSched getSchedule() {
        return schedule;
    }

    public byte getTemp() {
        return temp;
    }

    // setters
    public void setSchedule(HeatingSched schedule) {
        this.schedule = schedule;
    }

    @Override
    public void setState(State status) {
        if (status == State.ON || status == State.OFF) {
            this.status = status;
        } else {
            throw new IllegalArgumentException("State For Heating Must Be: On OR Off");
        }
    }

    public void setTemp(byte temp) {
        if (temp >= -5 && temp <= 30) {
            this.temp = temp;
        } else {
            throw new IllegalArgumentException("Temperature Must Be Between 5 and 30");
        }
    }

    /**
     * Used for converting csv to int
     * 
     * @param temp
     */
    public void setTemp(String temp) {
        setTemp(Byte.parseByte(temp));
    }
}

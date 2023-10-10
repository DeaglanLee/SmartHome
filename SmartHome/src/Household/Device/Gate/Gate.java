package Household.Device.Gate;

import Application.CustomExceptions.DeviceException;
import Household.Device.Device;
import Household.Device.DeviceType;
import Household.Device.State;
import Household.Profile.ProfileType;

public class Gate extends Device {
    private static DeviceType deviceType = DeviceType.GATE;

    public Gate(String name, String location, State status, ProfileType profileType) {
        super(name, location, deviceType, status, profileType);
    }

    public Gate(String name, String location, State status) {
        super(name, location, deviceType, status);
    }

    /**
     * Should only be used when accessing csv
     * 
     * @param name
     * @param location
     * @param status
     * @param profileType
     */
    public Gate(String name, String location, String status, ProfileType profileType) {
        super(name, location, deviceType, status, profileType);
    }

    @Override
    public void setState(State status) {
        if (status == State.OPEN || status == State.CLOSE) {
            this.status = status;
            return;
        }
        throw new DeviceException("State For A Gate Must Be: Open OR Closed");

    }
}

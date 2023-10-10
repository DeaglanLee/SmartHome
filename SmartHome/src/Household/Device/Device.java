package Household.Device;

import java.io.File;
import java.util.ArrayList;

import Application.CSVAccess;
import Application.CustomExceptions.DeviceException;
import Application.CustomExceptions.ProfileException;
import Household.Profile.Profile;
import Household.Profile.ProfileType;

/**
 * Abstract {@code Device} used for creating other types of devices. It uses
 * {@code CSVAccess} to create a csv file called <i>Devices.csv</i> and
 * {@code ProfileType} with
 * {@code Profile} to determine which users can access what device depending on
 * how you created a {@code Device}
 * 
 * <p>
 * Can add a {@code Device} by using {@code addDevice(Device, Profile)}
 * 
 */
public abstract class Device {
    private String name;
    private String location;
    private DeviceType deviceType;
    protected State status;
    private ProfileType profileType;
    static CSVAccess csvAccess = new CSVAccess();

    /**
     * Creates a {@code Device} that needs a profileType to interact with it.
     * <p>
     * Acts the exact same as
     * {@code Device(String, String, DeviceType, String, ProfileType)}
     * 
     * <p>
     * Acts the same as {@code Device(String, String, DeviceType, State)} and
     * {@code Device(String, String, DeviceType, String)} but this
     * adds a profileType and only that profileType can interact with this Device.
     * 
     * @param name        The name of the Device
     * @param location    The location of the Device
     * @param deviceType  The DeviceType of the Device
     * @param status      The State fo the Device
     * @param profileType The only ProfileType that can interact with it
     * @throws DeviceException If any of the parameters setters fails
     * @see Device#Device(String, String, DeviceType, String, ProfileType)
     * @see Device#Device(String, String, DeviceType, State)
     * @see Device#Device(String, String, DeviceType, String)
     */
    protected Device(String name, String location, DeviceType deviceType, State status, ProfileType profileType) {
        setName(name);
        setLocation(location);
        setDeviceType(deviceType);
        setState(status);
        setProfileType(profileType);
    }

    /**
     * Creates a {@code Device} that needs a profileType to interact with it.
     * <p>
     * Acts the exact same as
     * {@code Device(String, String, DeviceType, State, ProfileType)}
     * <p>
     * Acts the same as
     * {@code Device(String, String, DeviceType, State, ProfileType)} and
     * {@code Device(String, String, DeviceType, String)} but this
     * adds a profileType and only that profileType can interact with this Device.
     * 
     * 
     * @param name        The name of the Device
     * @param location    The location of the Device
     * @param deviceType  The DeviceType of the Device
     * @param status      The State fo the Device
     * @param profileType The only ProfileType that can interact with it
     * @throws DeviceException If any of the parameters setters fails
     * @see Device#Device(String, String, DeviceType, State, ProfileType)
     * @see Device#Device(String, String, DeviceType, State)
     * @see Device#Device(String, String, DeviceType, String)
     */
    protected Device(String name, String location, DeviceType deviceType, String status, ProfileType profileType) {
        setName(name);
        setLocation(location);
        setDeviceType(deviceType);
        setState(status);
        setProfileType(profileType);
    }

    /**
     * Creates a {@code Device} that by default anyone can interact with
     * 
     * <p>
     * Acts in the exact same way as {@code String, String, DeviceType, State}
     * 
     * @param name       The name of the Device
     * @param location   The location of the Device
     * @param deviceType The DeviceType of the Device
     * @param status     The State fo the Device
     * @throws DeviceException If any of the setters fail
     * @see Device#Device(String, String, DeviceType, State)
     */
    protected Device(String name, String location, DeviceType deviceType, String status) {
        setName(name);
        setLocation(location);
        setDeviceType(deviceType);
        setState(status);
    }

    /**
     * Creates a {@code Device} that by default anyone can interact with
     * 
     * <p>
     * Acts in the exact same way as {@code String, String, DeviceType, String}
     * 
     * @param name       The name of the Device
     * @param location   The location of the Device
     * @param deviceType The DeviceType of the Device
     * @param status     The State fo the Device
     * @throws DeviceException If any of the setters fail
     * @see Device#Device(String, String, DeviceType, String)
     */
    protected Device(String name, String location, DeviceType deviceType, State status) {
        setName(name);
        setLocation(location);
        setDeviceType(deviceType);
        setState(status);
    }

    /**
     * Adds a {@code Device} to a csv file called Devices.csv by using
     * {@code CSVAccess} for it to be in non volatile storage
     * <p>
     * If the file {@code Devices.csv} does not exist it makes one for you along
     * with the headers for each column
     * 
     * @param device
     * @param profile
     * @throws ProfileException
     * @throws IllegalArgumentException
     * @see CSVAccess
     */
    public void addDevice(Device device, Profile userProfile) {
        if (userProfile.getProfileType().equals(ProfileType.PARENT)) {
            ArrayList<String> outputList = new ArrayList<>();
            CSVAccess csvAccess = new CSVAccess();
            outputList.add(csvAccess.convertToCSV(device));
            csvAccess.writeToCSV(new File("Devices.csv"), outputList);
            return;
        }
        throw new ProfileException("This Profile cannot add devices");
    }

    public void editDevice(Device device, Profile userProfile) {
        if (userProfile.getProfileType().equals(ProfileType.PARENT)) {
            CSVAccess csvAccess = new CSVAccess();
            ArrayList<Device> devices = csvAccess.convertCSVToDevice();

            for (Device deviceInCSV : devices) {
                // if the name is the same as the one being stored
                if (deviceInCSV.getName().equals(device.getName())) {
                    // update the device which is in the csv with the device that is going to replace it
                    deviceInCSV = device;
                    //write the string convertion of devices to Devices.csv
                    csvAccess.writeToCSV(new File("Devices.csv"), csvAccess.devicesCSV(devices));
                    return;
                }
            }

            throw new DeviceException("Device was not found!!!");

        }
        throw new ProfileException("This Profile cannot add devices");
    }

    /**
     * Remove a stored Device from the csv
     * 
     * @param userProfile   is the Profile they currently are logged in to
     * @param delDeviceInt is the Device they are trying to remove
     * @throws ProfileException if the profile is <b>not</b> a parent profile
     * @throws IllegalArgumentException if the file <b>isn't</b> what is hard coded 
     * @see CSVAccess#deleteRow(int, File) For the HardCoded Filenames that you can use
     */
    public void remove(Profile userProfile, int delDeviceInt) {
        if (userProfile.getProfileType().equals(ProfileType.PARENT)) {
            CSVAccess csvAccess = new CSVAccess();
            // delete the Device
            csvAccess.deleteRow(delDeviceInt, new File("Devices.csv"));
            return;
        }
        throw new ProfileException(userProfile + " is not able to delete a Device");
    }

    // setters

    /**
     * Set the State of a Device using State (enum)
     * 
     * @param status
     * @throws DeviceException
     */
    public void setState(State status) {
        if (status == State.ON || status == State.OFF || status == State.OPEN || status == State.CLOSE) {
            this.status = status;
            return;
        }
        throw new DeviceException("State Must Be: On, Off, Open OR Closed");

    }

    /**
     * Set the State of a Device using String (enum)
     * <p>
     * Calls the setState(State) method
     * 
     * @param status
     * @throws DeviceException
     */
    public void setState(String state) {
        String[] statearr = { "Open", "Close", "On", "Off" };
        if (state != null) {
            if (state.equalsIgnoreCase(statearr[0])) {
                setState(State.OPEN);
            } else if (state.equalsIgnoreCase(statearr[1])) {
                setState(State.CLOSE);
            } else if (state.equalsIgnoreCase(statearr[2])) {
                setState(State.ON);
            } else if (state.equalsIgnoreCase(statearr[3])) {
                setState(State.OFF);
            }
            return;
        }
        throw new DeviceException("State Must Be: Open, Closed, On OR Off!");
    }

    /**
     * Set the DeviceType of a Device
     * 
     * @param deviceType
     * @throws DeviceException
     */
    public void setDeviceType(DeviceType deviceType) {
        if (deviceType == DeviceType.CAMERA || deviceType == DeviceType.HEATING || deviceType == DeviceType.LIGHTING
                || deviceType == DeviceType.GATE) {
            this.deviceType = deviceType;
            return;
        }
        throw new DeviceException("Device Type Must Be: Gate, Camera, Heating OR Lighting");

    }

    /**
     * Set the Device Type of a Device using String. Should only be used to convert
     * a CSV String Device into Device
     * <p>
     * Calls the setDeviceType(DeviceType) method
     * 
     * @param deviceType
     * @throws DeviceException
     */
    public void setDeviceType(String deviceType) {
        String[] deviceTypes = { "Camera", "heating", "blinds", "gate", "lighting" };
        if (deviceType != null) {
            if (deviceType.equalsIgnoreCase(deviceTypes[0])) {
                setDeviceType(DeviceType.CAMERA);
            } else if (deviceType.equalsIgnoreCase(deviceTypes[1])) {
                setDeviceType(DeviceType.HEATING);
            } else if (deviceType.equalsIgnoreCase(deviceTypes[2])) {
                setDeviceType(DeviceType.BLINDS);
            } else if (deviceType.equalsIgnoreCase(deviceTypes[3])) {
                setDeviceType(DeviceType.GATE);
            } else if (deviceType.equalsIgnoreCase(deviceTypes[4])) {
                setDeviceType(DeviceType.LIGHTING);
            }
            return;
        }
        throw new DeviceException(
                "Device Should Have A Type Of: Camera, Heating, Blinds, Gate OR Lighting ");
    }

    /**
     * Set the Location of a Device using a String
     * 
     * @param location
     * @throws DeviceException
     */
    public void setLocation(String location) {
        if (location.length() >= 5 && location.length() <= 30) {
            this.location = location;
            return;
        }
        throw new DeviceException("Location Must Be Between 5 And 30 Characters Long");

    }

    public void setName(String name) {
        if (name.length() >= 5 && name.length() <= 20) {
            this.name = name;
            return;
        }
        throw new DeviceException("Name Must Be Between 5 And 20 Characters Long");
    }

    public void setProfileType(ProfileType profileType) {
        this.profileType = profileType;
    }

    public void setProfileType(String profileType) {
        if (profileType.equalsIgnoreCase("Parent")) {
            setProfileType(ProfileType.PARENT);
            return;
        } else if (profileType.equalsIgnoreCase("Child")) {
            setProfileType(ProfileType.CHILD);
            return;
        } else if (profileType.equalsIgnoreCase("All")) {
            setProfileType(ProfileType.ALL);
            return;
        }
        throw new DeviceException("Profile Must Be Either Parent Or Child");

    }

    // getters
    public String getName() {
        return this.name;
    }

    public String getLocation() {
        return this.location;
    }

    public DeviceType getDeviceType() {
        return this.deviceType;
    }

    public State getState() {
        return this.status;
    }

    public ProfileType getProfileType() {
        return profileType;
    }

    public String toString() {
        String result = "";
        result += "\nDevice Name: " + getName() + "\n";
        result += "Device Location: " + getLocation() + "\n";
        result += "Device Type: " + getDeviceType() + "\n";
        result += "Device State: " + getState();
        return result;
    }

}

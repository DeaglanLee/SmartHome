package Application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Application.CustomExceptions.DeviceException;
import Household.Device.*;
import Household.Device.Heating.*;
import Household.Device.Lighting.*;
import Household.Device.Gate.Gate;
import Household.Device.Camera.Camera;
import Household.Profile.*;

/**
 * The {@code CSVAccess} class represents the interaction between the stored
 * information and the user
 * 
 * @author Deaglan Lee
 */
public class CSVAccess {

    public CSVAccess() {
    }

    /**
     * Write to a csv using a File and a String array
     * 
     * @param fileName
     * @param contents
     * @throws IllegalArgumentException If the File isn't a file that should exist
     * @see CSVAccess#firstLineCSV(File)
     */
    public void writeToCSV(File fileName, ArrayList<String> contents) {
        try {
            // check if the file exists
            if (fileName.exists()) {
                // writing string array to the csv
                csvWrite(fileName, contents);
            } else {
                // create the first line make it easier to read
                if (firstLineCSV(fileName)) {
                    // writing string array to the csv
                    csvWrite(fileName, contents);
                }
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("File Should Not Exist");
        }
    }

    /**
     * Create the first line of the csv to make it easier to understand
     * 
     * @param fileName
     * @return
     */
    public boolean firstLineCSV(File fileName) {
        FileWriter fw;
        try {
            if (fileName.getName() == "Devices.csv") {
                fileName.createNewFile();
                fw = new FileWriter(fileName, true);
                fw.append("Name,Location,Device Type,State,Profile Access Type");
                fw.close();
                return true;
            } else if (fileName.getName() == "Profiles.csv") {
                fileName.createNewFile();
                fw = new FileWriter(fileName, true);
                fw.append("Username,Password,Permission,Acccount Type");
                fw.close();
                return true;
            } else if (fileName.getName() == "HeatingSchedule.csv") {
                fileName.createNewFile();
                fw = new FileWriter(fileName, true);
                fw.append("Name,On Time,Off Time,Temperature");
                fw.close();
                return true;

            }
        } catch (IOException e) {
            throw new IllegalArgumentException("File Should Not Exist");
        }
        return false;
    }

    /**
     * Write string array to a file
     * 
     * @param fileName
     * @param contents
     */
    private void csvWrite(File fileName, ArrayList<String> contents) {
        try {
            FileWriter fw = new FileWriter(fileName, true);
            for (int i = 0; i < contents.size(); i++) {
                fw.append("\n" + contents.get(i));
            }
            fw.close();
        } catch (Exception e) {

        }
    }

    /**
     * delete a row in a csv file
     * 
     * @param rowNumber that the user sees (corrected in this method)
     * @param filName
     */
    public void deleteRow(int rowNumber, File fileName) {
        // Store CSV contents in arraylist

        if (fileName.getName() == "Devices.csv") {
            ArrayList<Device> csvDevices = convertCSVToDevice();
            csvDevices.remove(rowNumber - 1);

            fileName.delete();
            firstLineCSV(fileName);
            writeToCSV(fileName, devicesCSV(csvDevices));

        } else if (fileName.getName() == "Profiles.csv") {
            ArrayList<Profile> csvProfiles = convertCSVToProfile();
            csvProfiles.remove(rowNumber - 1);

            fileName.delete();
            firstLineCSV(fileName);
            writeToCSV(fileName, profilesCSV(csvProfiles));

        } else if (fileName.getName() == "HeatingSchedule.csv") {
            ArrayList<HeatingSched> csvHeatingScheds = convertCSVToHeatingScheds();
            csvHeatingScheds.remove(rowNumber - 1);

            fileName.delete();
            firstLineCSV(fileName);
            writeToCSV(fileName, heatingSchedCSV(csvHeatingScheds));
        } else {
            throw new IllegalArgumentException("File Name doesnt exist");
        }
    }

    /**
     * reads a csv file into a String array
     * (used some code from stackoverflow)
     * 
     * @param fileName
     * @return
     */
    private ArrayList<String> readFromCSV(File fileName) {
        int count = 0;
        ArrayList<String> csvStringarrlist = new ArrayList<String>();
        String[] csvContents = new String[count];// used to return the contents of the csv
        String line = "";
        try {
            // read in a line from a file
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            line = br.readLine();
            // check if the line has data in it and skip first line
            while ((line = br.readLine()) != null) {
                count++;
                csvContents = line.split(",");
                // temporary list that is used to store a line from a csv
                List<String> temp = new ArrayList<String>();
                temp = Arrays.asList(csvContents);
                // add the temp list to another list
                csvStringarrlist.addAll(temp);
            }
            br.close();
        } catch (IOException e) {

        }
        // convert the list to a string array
        return csvStringarrlist;
    }

    /**
     * Converts a String array into a Device array
     * 
     * @param csvContents
     * @return
     */
    public ArrayList<Device> convertCSVToDevice() {
        CSVAccess csvAccess = new CSVAccess();
        ArrayList<String> csvContents = csvAccess.readFromCSV(new File("Devices.csv"));
        ArrayList<Device> device = new ArrayList<Device>();// used to add each device later
        // go through each line of the Sting array
        for (int i = 0; i < csvContents.size() - 1; i++) {
            try {
                String name = csvContents.get(i);
                String location = csvContents.get(i + 1);
                String deviceType = csvContents.get(i + 2);
                String state = csvContents.get(i + 3);
                String profileTypeCSV = csvContents.get(i + 4);

                // Convert String to ProfileType
                ProfileType profileType = ProfileType.INITIALISE;
                switch (profileTypeCSV) {
                    case "Parent":
                        profileType = ProfileType.PARENT;
                        break;
                    case "Child":
                        profileType = ProfileType.CHILD;
                        break;
                    case "All":
                        profileType = ProfileType.ALL;
                        break;
                }
                // Check if its a camera, heating, lighting or gate
                if (deviceType.equals("Camera")) {
                    // create a Camera then add it to return device
                    Device cameraDevice = new Camera(name, location, state, profileType);
                    device.add(cameraDevice);

                } else if (deviceType.equals("Heating")) {
                    // create a Heating then add it to return device
                    Device cameraDevice = new Heating(name, location, state, profileType);
                    device.add(cameraDevice);
                } else if (deviceType.equals("Lighting")) {
                    // create a Lighting then add it to return device
                    Device cameraDevice = new Lighting(name, location, state, profileType);
                    device.add(cameraDevice);
                } else if (deviceType.equals("Gate")) {
                    // create a Gate then add it to return device
                    Device cameraDevice = new Gate(name, location, state, profileType);
                    device.add(cameraDevice);
                } else {
                    throw new DeviceException("Not a valid device type in csv to convert");
                }
                i = i + 4;
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new ArrayIndexOutOfBoundsException("Array out of bounds");
            }
        }
        return device;
    }

    /**
     * Reads in HeatingSchedules.csv then converts the Strings into HeatingSchedules
     * 
     * @param csvContents
     * @return What is stored in HeatingScheds.csv that has been converted to the
     *         object HeatingScheds.
     */
    public ArrayList<HeatingSched> convertCSVToHeatingScheds() {
        CSVAccess csvAccess = new CSVAccess();
        ArrayList<String> csvContents = csvAccess.readFromCSV(new File("HeatingSchedule.csv"));
        ArrayList<HeatingSched> schedules = new ArrayList<HeatingSched>();// used to add each Schedule later
        // go through each line of the String array
        for (int i = 0; i < csvContents.size() - 1; i++) {
            try {

                // Start time
                String startTime = csvContents.get(i + 1);
                LocalDateTime startParsed = LocalDateTime.parse(startTime);

                // End time
                String endTime = csvContents.get(i + 2);
                LocalDateTime endParsed = LocalDateTime.parse(endTime);

                ArrayList<Device> devices = csvAccess.convertCSVToDevice();

                // Get the Heating device
                String heatingName = "";
                String heatingLocation = "";
                State heatingState = State.INITIALISE;

                for (Device device : devices) {
                    if (device.getName().equals(csvContents.get(i))) {
                        heatingName = device.getName();
                        heatingLocation = device.getLocation();
                        heatingState = device.getState();
                        break;
                    }
                }
                Heating heatingFromCSV = new Heating(heatingName, heatingLocation, heatingState);

                // get the temperature and convert it to byte from string
                byte temperature = Byte.parseByte(csvContents.get(i + 3));

                // create a Schedule
                HeatingSched tempHeatingScheds = new HeatingSched(heatingFromCSV, startParsed, endParsed, temperature);

                // store tempHeatingSchedule into an arraylist
                schedules.add(tempHeatingScheds);
                // goes to the next schedule
                i = i + 3;
            } catch (ArrayIndexOutOfBoundsException e) {

            }
        }
        return schedules;
    }

    /**
     * Read the Profiles.csv and convert it to Profile
     * 
     * @return
     */
    public ArrayList<Profile> convertCSVToProfile() {
        ArrayList<String> csvContents = readFromCSV(new File("Profiles.csv"));
        ArrayList<Profile> profiles = new ArrayList<Profile>();// used to add each Profile later
        // go through each line of the String array
        for (int i = 0; i < csvContents.size() - 1; i++) {
            try {
                String username = csvContents.get(i);
                String password = csvContents.get(i + 1);
                String permission = csvContents.get(i + 2);
                String profileType = csvContents.get(i + 3);

                if (profileType.equals("Parent")) {
                    // create a parent profile
                    Profile parentProfile = new Parent(username, password, profileType, permission);
                    profiles.add(parentProfile);
                } else {
                    // create a child profile
                    Profile childProfile = new Child(username, password, profileType, permission);
                    profiles.add(childProfile);
                }
                // goes to the next Profile
                i = i + 3;
            } catch (ArrayIndexOutOfBoundsException e) {

            }
        }
        return profiles;
    }

    // Converting OBJECT To CSV

    /**
     * Convert SINGLE Device to CSV
     * 
     * @param devices
     * @return
     */
    public String convertToCSV(Device devices) {
        String res = "";
        res += devices.getName() + "," + devices.getLocation() + "," + devices.getDeviceType() + ","
                + devices.getState() + "," + devices.getProfileType();
        return res;
    }

    /**
     * Convert a SINGLE Profile to CSV
     * 
     * @param profile
     * @return
     */
    public String convertToCSV(Profile profile) {
        String res = "";
        res += profile.getUsername() + "," + profile.getPassword() + ","
                + profile.getPermission() + "," + profile.getProfileType();
        return res;
    }

    /**
     * Convert SINGLE Schedule to CSV
     * 
     * @param heatingScheds
     * @return
     */
    public String convertToCSV(HeatingSched heatingScheds) {
        String res = "";
        res += heatingScheds.getHeating().getName() + "," + heatingScheds.getOnTime() + "," + heatingScheds.getOffTime()
                + ","
                + heatingScheds.getTemp();
        return res;
    }

    public ArrayList<String> profilesCSV(ArrayList<Profile> profiles) {
        // return arraylist
        ArrayList<String> profileToCSV = new ArrayList<>();
        // convert each profile to csv
        for (Profile profile : profiles) {
            String res = "";
            res += profile.getUsername() + "," + profile.getPassword() + "," + profile.getPermission() + ","
                    + profile.getProfileType();
            profileToCSV.add(res);
        }
        return profileToCSV;
    }

    public ArrayList<String> devicesCSV(ArrayList<Device> devices) {
        // return arraylist
        ArrayList<String> deviceToCSV = new ArrayList<>();
        // convert each profile to csv
        for (Device device : devices) {
            String res = "";
            res += device.getName() + "," + device.getLocation() + "," + device.getDeviceType() + ","
                    + device.getState() + "," + device.getProfileType();
            deviceToCSV.add(res);
        }
        return deviceToCSV;
    }

    public ArrayList<String> heatingSchedCSV(ArrayList<HeatingSched> heatingScheds) {
        // return arraylist
        ArrayList<String> heatingSchedToCSV = new ArrayList<>();
        // convert each profile to csv
        for (HeatingSched heatingSched : heatingScheds) {
            String res = "";
            res += heatingSched.getHeating().getName() + "," + heatingSched.getOnTime() + ","
                    + heatingSched.getOffTime() + ","
                    + heatingSched.getTemp();
            heatingSchedToCSV.add(res);
        }
        return heatingSchedToCSV;
    }

}

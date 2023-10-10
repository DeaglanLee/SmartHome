package Household.Device;

import java.io.File;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;


import Application.CSVAccess;
import Application.Menu;
import Application.CustomExceptions.DeviceException;
import Application.CustomExceptions.ProfileException;
import Household.Device.Camera.Camera;
import Household.Device.Gate.Gate;
import Household.Device.Heating.Heating;
import Household.Device.Lighting.Lighting;
import Household.Profile.*;

public class DeviceTester {
    // Misc
    private static CSVAccess csvAccess = new CSVAccess();
    private static Scanner input = new Scanner(System.in);

    // Files
    private static File devicesFile = new File("Devices.csv");
    private static File profilesFile = new File("Profiles.csv");

    // Profile
    private static Profile parentProfile = new Parent("Parent", "ParentsPassword");
    private static Profile childProfile = new Child("Child", "ChildPassword");
    private static Profile userProfile;

    // Text Colour
    public static final String RESETTEXTCOLOUR = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String REDTEXT = "\u001B[31m";
    public static final String GREENTEXT = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    // Messages
    private static final String USERERRORENTERNUMBER = "Please Enter A Valid Number!";
    private static final String DEVICEADDED = "The Device has been added!!!";

    public static void main(String[] args) {
        // deviceSettingsParent();
        // deviceSettingsChild();
        // lightingSettings();
        // deviceSettingsChild();
        // heatingSettings();

        // deviceSettingsChild();
        // addLight();
        userProfile = childProfile;
        addCamera();
    }

    // -----------------------------START OF DEVICES---------------------------- \\

    /**
     * Device menu system for a {@code Child Profile}
     */
    private static void deviceSettingsChild() {
        String options[] = { "Show All Devices", "Lighting Settings", "Heating Settings", "Camera Settings",
                "Gate Settings", "Exit Program" };
        Menu menu = new Menu("Device Menu", options);

        boolean finished = false;

        do {
            int option = menu.getUserChoice();
            switch (option) {
                case 1:
                    showDevices();
                    break;
                case 2:
                    lightingSettings();
                    break;
                case 3:
                    heatingSettings();
                    break;
                case 4:
                    cameraSettings();
                    break;
                case 5:
                    gateSettings();
                    break;
                case 6:
                    finished = true;
                    break;
                default:
                    System.out.println(REDTEXT + "Not a valid option." + RESETTEXTCOLOUR);
            }
        } while (!finished);
    }

    /**
     * Device menu system for a parent profile
     */
    private static void deviceSettingsParent() {
        String options[] = { "Add a Device", "Remove a Device", "Show All Devices", "Lighting Settings",
                "Heating Settings",
                "Camera Settings", "Gate Settings" };
        Menu menu = new Menu("Device Menu", options);

        boolean finished = false;

        do {
            int option = menu.getUserChoice();
            switch (option) {
                case 1:
                    addDevice();
                    break;
                case 2:
                    deleteDevice();
                    break;
                case 3:
                    showDevices();
                    break;
                case 4:
                    lightingSettings();
                    break;
                case 5:
                    heatingSettings();
                    break;
                case 6:
                    cameraSettings();
                    break;
                case 7:
                    gateSettings();
                    break;
                case 8:
                    finished = true;
                    break;
                default:
                    System.out.println(REDTEXT + "Not a valid option." + RESETTEXTCOLOUR);
            }
        } while (!finished);
    }

    /**
     * Add a {@code Device} to the home
     * <p>
     * Terminal output is about create a {@code Device} (generalised, asks the user
     * which type of device they want to create and add)
     */
    public static void addDevice() {
        boolean sectionFinished = false;

        // DEVICE NAME
        String name = "";

        // loop until the user enters a valid name
        do {
            // display message for user to let them know to input name
            System.out.print("\nWhat is the Device called?\nEnter Device Name: ");
            name = input.nextLine().trim();
            // Check if the user enters a Name
            if (name != "") {
                // exit the loop
                sectionFinished = true;
            } else {
                // display a message of what went wrong
                negativeMessage("Please enter a Name");
            }
        } while (!sectionFinished);
        // reset sectionFinished
        sectionFinished = false;

        // DEVICE LOCATION
        String location = "";

        // loop until the user enters a valid name
        do {
            // display message for user to let them know to input name
            System.out.print("\nWhere is the Device?\nEnter Location: ");
            location = input.nextLine().trim();
            // Check if the user enters a Location
            if (location != "") {
                // exit the loop
                sectionFinished = true;
            } else {
                // display a message of what went wrong
                negativeMessage("Please enter a Location");
            }

        } while (!sectionFinished);
        // reset sectionFinished
        sectionFinished = false;

        // DEVICE TYPE
        DeviceType deviceType = DeviceType.INITIALISE;

        do {
            // display message for user to let them know to input name
            System.out.print(
                    "\nWhat is the Device?\n1.Camera\n2.Gate\n3.Lighting\n4.Heating\n\nEnter Number: ");
            try {
                // User Input is a Number
                byte deviceTypeInt = input.nextByte();
                // Check if the User Enters a Valid DeviceType
                // Only Numbers 1,2,3 and 4 Allowed
                switch (deviceTypeInt) {
                    case 1:
                        // set the deviceType to Camera then exit the loop
                        deviceType = DeviceType.CAMERA;
                        sectionFinished = true;
                        break;
                    case 2:
                        // set the deviceType to Camera then exit the loop
                        deviceType = DeviceType.GATE;
                        sectionFinished = true;
                        break;
                    case 3:
                        // set the deviceType to Camera then exit the loop
                        deviceType = DeviceType.LIGHTING;
                        sectionFinished = true;
                        break;
                    case 4:
                        // set the deviceType to Camera then exit the loop
                        deviceType = DeviceType.HEATING;
                        sectionFinished = true;
                        break;
                    default:
                        // display a message of what went wrong
                        negativeMessage(USERERRORENTERNUMBER);
                        break;
                }
            } catch (InputMismatchException e) {
                // display a message of what went wrong
                negativeMessage(USERERRORENTERNUMBER);
            }
        } while (!sectionFinished);
        // reset sectionFinished
        sectionFinished = false;

        // DEVICE STATE
        State state = State.INITIALISE;
        // While the user enters an INVALID response
        do {
            if (deviceType == DeviceType.CAMERA || deviceType == DeviceType.LIGHTING
                    || deviceType == DeviceType.HEATING) {
                // display message for user to let them know to input name
                System.out.print("\nWhat is the Device State?\n1.On\n2.Off\n\nEnter Number: ");
                try {
                    // Check if the user enters a valid number
                    byte stateint = input.nextByte();
                    switch (stateint) {
                        case 1:
                            // set the state to On then exit the loop
                            state = State.ON;
                            sectionFinished = true;
                            break;
                        case 2:
                            // set the state to Off then exit the loop
                            state = State.OFF;
                            sectionFinished = true;
                            break;

                        default:
                            // display a message of what went wrong
                            negativeMessage(USERERRORENTERNUMBER);
                            break;
                    }
                } catch (InputMismatchException e) {
                    // display a message of what went wrong
                    negativeMessage(USERERRORENTERNUMBER);
                }

            } else {
                // display message for user to let them know to input name
                System.out.print("\nWhat is the Device State?\n1.Open\n2.Closed\n\nEnter Number: ");
                try {
                    // Check if the user enters a valid number
                    byte stateint = input.nextByte();
                    switch (stateint) {
                        case 1:
                            // set the state to Open then exit the loop
                            state = State.OPEN;
                            sectionFinished = true;
                            break;
                        case 2:
                            // set the state to Closed then exit the loop
                            state = State.CLOSE;
                            sectionFinished = true;
                            break;
                        default:
                            // display a message of what went wrong
                            negativeMessage(USERERRORENTERNUMBER);
                            break;
                    }
                } catch (InputMismatchException e) {
                    // display a message of what went wrong
                    negativeMessage(USERERRORENTERNUMBER);
                }
            }
        } while (!sectionFinished);
        sectionFinished = false;

        // DEVICE PROFILETYPE
        do {
            // display message for user to let them know to input name
            System.out.print(
                    "\nDo you want to limit access to by profile type?\n1. Yes\n2. No\nEnter Number: ");
            try {
                // Check if the user enters a valid number
                byte profileLimitRequest = input.nextByte();
                if (profileLimitRequest == 1) {
                    // create the Device and add it to the csv (Devices.csv)
                    // then exit the loop
                    createDeviceByDeviceType(name, location, deviceType, state, ProfileType.PARENT);
                    sectionFinished = true;
                } else if (profileLimitRequest == 2) {
                    // create the Device and add it to the csv (Devices.csv)
                    createDeviceByDeviceType(name, location, deviceType, state, ProfileType.ALL);
                    // then exit the loop
                    sectionFinished = true;

                } else {
                    negativeMessage(USERERRORENTERNUMBER);
                }
            } catch (ProfileException e) {
                negativeMessage(e.getMessage());
            } catch (InputMismatchException e) {
                negativeMessage(USERERRORENTERNUMBER);
            }
        } while (!sectionFinished);
    }

    /**
     * Creates a {@code Devices} and adds it to the {@code Devices.csv} by calling
     * {@code addDevice(Device)} which
     * 
     * @param name
     * @param location
     * @param deviceType
     * @param state
     * @param profileType
     * @throws ProfileException If the {@code Profile} that is being used to create
     *                          the device is NOT a {@code PARENT}
     */
    private static void createDeviceByDeviceType(String name, String location, DeviceType deviceType, State state,
            ProfileType profileType) {
        try {
            if (deviceType.equals(DeviceType.CAMERA)) {
                Device cameraDevice = new Camera(name, location, state, profileType);
                addDevice(cameraDevice);
            } else if (deviceType.equals(DeviceType.LIGHTING)) {
                Device lightingDevice = new Camera(name, location, state, profileType);
                addDevice(lightingDevice);
            } else if (deviceType.equals(DeviceType.HEATING)) {
                Device heatingDevice = new Camera(name, location, state, profileType);
                addDevice(heatingDevice);
            } else if (deviceType.equals(DeviceType.GATE)) {
                Device gateDevice = new Camera(name, location, state, profileType);
                addDevice(gateDevice);
            }
        } catch (DeviceException e) {
            negativeMessage(e.getMessage());
        }
    }

    /**
     * Add a device to the csv Devices.csv
     * 
     * @param heatingDevice
     * @throws ProfileExeption if the user is not a {@code Parent}
     */
    private static void addDevice(Device Device) {
        Device.addDevice(Device, userProfile);
        // display to the user that the device was added
        positiveMessage(DEVICEADDED);
    }

    /**
     * Delete a device from the Devices.csv
     */
    private static void deleteDevice() {
        // store the Devices from the CSV
        ArrayList<Device> devices = csvAccess.convertCSVToDevice();

        if (!devices.isEmpty()) {
            byte count = 1;
            for (Device device : devices) {
                System.out.println("\nDevice Number: " + count + device);
            }

            System.out.print("\nWhich Device do you want to delete?\nEnter Number: ");
            try {
                // check if they entered a number
                byte userDeleteDevice = input.nextByte();
                // check if they entered a valid number
                if (userDeleteDevice - 1 <= devices.size() && userDeleteDevice - 1 >= 0) {
                    // delete the device
                    csvAccess.deleteRow(userDeleteDevice, devicesFile);
                } else {
                    negativeMessage("\nPlease enter a number between 0 and " + devices.size());
                }
            } catch (IllegalArgumentException | InputMismatchException e) {
                negativeMessage(USERERRORENTERNUMBER);
            }
        } else {
            negativeMessage("\nThere are no devices to display");
        }
    }

    /**
     * Show the devices in the home
     */
    public static void showDevices() {
        // store the csv contents into devices
        ArrayList<Device> devices = csvAccess.convertCSVToDevice();

        byte count = 1;
        // check if the devices arraylist is empty
        if (devices.isEmpty()) {
            negativeMessage("There are no Devices in the household");
        } else {
            for (Device device : devices) {
                System.out.println("\nDevice Number: " + count + device);
                count++;
            }

            // boolean sectionFinished = false;
            // do {
            //     System.out.print("\n\nDo you want to change any of the Devices States?\n1. Yes\n2. No\nEnter Number: ");
            //     try {
            //         byte addDeviceOptions = input.nextByte();
            //         if (addDeviceOptions == 2) {
            //             return;
            //         }

            //         System.out.println("\nWhich Device would you like to change?\nEnter Device Number: ");
            //         addDeviceOptions = input.nextByte();
            //         if (addDeviceOptions <= devices.size() && addDeviceOptions > 0) {
            //             if (devices.get(addDeviceOptions + 1).getState().equals(State.ON)) {
            //                 devices.get(addDeviceOptions + 1).setState(State.OFF);
            //                 sectionFinished = true;
            //             } else if (devices.get(addDeviceOptions + 1).getState().equals(State.OFF)) {
            //                 devices.get(addDeviceOptions + 1).setState(State.ON);
            //                 sectionFinished = true;
            //             } else if (devices.get(addDeviceOptions + 1).getState().equals(State.OPEN)) {
            //                 devices.get(addDeviceOptions + 1).setState(State.CLOSE);
            //                 sectionFinished = true;
            //             } else if (devices.get(addDeviceOptions + 1).getState().equals(State.CLOSE)) {
            //                 devices.get(addDeviceOptions + 1).setState(State.OPEN);
            //                 sectionFinished = true;
            //             } else {

            //             }
            //         } else {
            //             negativeMessage(USERERRORENTERNUMBER);
            //         }
            //     } catch (InputMismatchException e) {
            //         negativeMessage(USERERRORENTERNUMBER);
            //         input.nextLine();
            //     }
            // } while (!sectionFinished);
        }
    }

    private static Device createDeviceByUserInput(DeviceType deviceType) {
        boolean sectionFinished = false;

        // restart if the user entered invalid details when it tried creating the Device
        do {
            // NAME
            String name = "";
            do {
                // display message for user to let them know to input name
                System.out.print("\n--------Add a " + deviceType + " Device--------\nPlease Enter the " + deviceType
                        + "s' Name: ");
                name = input.nextLine().trim();
                // check if the name is empty
                if (name != "") {
                    // name is set and exit this section
                    sectionFinished = true;
                } else {
                    // display a message of what went wrong
                    negativeMessage("Please enter a Name");
                }
            } while (!sectionFinished);
            // reset sectionFinished
            sectionFinished = false;

            // LOCATION
            String location = "";
            do {
                // display message for user to let them know to input location
                System.out.print("\nWhere is the Heating?\nEnter the " + deviceType + "s' Location: ");
                location = input.nextLine().trim();
                // check if the location is empty
                if (location != "") {
                    // location is set and exit this section
                    sectionFinished = true;
                } else {
                    // display a message of what went wrong
                    negativeMessage("Please enter a Location");
                }

            } while (!sectionFinished);
            // reset sectionFinished
            sectionFinished = false;

            // State
            State state = State.INITIALISE;
            do {
                // display message for user to let them know to input state
                System.out.print("\nWhat is the state of the " + deviceType + "?\n1. On\n2. Off\nEnter Number: ");
                try {
                    // Check if the user input is 1 or 2
                    // also make sure the program doesnt crash when they enter a string for example
                    byte userInputState = input.nextByte();
                    switch (userInputState) {
                        case 1:
                            // state is set to On and exit this section
                            state = State.ON;
                            sectionFinished = true;
                            break;
                        case 2:
                            // state is set to Off and exit this section
                            state = State.OFF;
                            sectionFinished = true;
                            break;
                        default:
                            // display a message of what went wrong
                            negativeMessage("\n" + USERERRORENTERNUMBER + " 1 OR 2");
                            break;
                    }
                } catch (InputMismatchException e) {
                    // display a message of what went wrong
                    negativeMessage(USERERRORENTERNUMBER);
                }
            } while (!sectionFinished);
            // reset sectionFinished
            sectionFinished = false;

            // ProfileType
            ProfileType profileType = ProfileType.INITIALISE;
            do {
                // display message for user to let them know to input profile type
                System.out.print("\nDo you want a profile type to access this " + deviceType
                        + "?\n1. Yes\n2. No\nEnter Number: ");
                try {
                    // Check if the user input is 1 or 2
                    // also make sure the program doesnt crash when they enter a string for example
                    byte userInput = input.nextByte();
                    switch (userInput) {
                        case 1:
                            // profileType is set to PARENT and exit this section
                            profileType = ProfileType.PARENT;
                            sectionFinished = true;
                            break;
                        case 2:
                            // profileType is set to ALL and exit this section
                            profileType = ProfileType.ALL;
                            sectionFinished = true;
                            break;
                        default:
                            // display a message of what went wrong
                            negativeMessage(USERERRORENTERNUMBER + "1 OR 2");
                            break;
                    }
                } catch (InputMismatchException e) {
                    // display a message of what went wrong
                    negativeMessage(USERERRORENTERNUMBER);
                } finally {
                    input.nextLine();
                }
            } while (!sectionFinished);
            // reset sectionFinished
            sectionFinished = false;

            // Create the Device and add it to storage
            //
            // make sure it doesnt crash if the profile they are signed into doesnt meet the
            // requirements to add a Device
            //
            // make sure it doesnt crash if the user inputs do not qualify to create a
            // Device
            try {
                // check which one the device is
                if (deviceType.equals(DeviceType.CAMERA)) { // Camera
                    // try and create the device
                    Device cameraDevice = new Camera(name, location, state, profileType);
                    // return it
                    return cameraDevice;
                } else if (deviceType.equals(DeviceType.HEATING)) { // Heating
                    // try and create the device
                    Device heatingDevice = new Heating(name, location, state, profileType);
                    // return it
                    return heatingDevice;
                } else if (deviceType.equals(DeviceType.LIGHTING)) { // Ligthing
                    // try and create the device
                    Device lightingDevice = new Lighting(name, location, state, profileType);
                    // return it
                    return lightingDevice;
                } else { // Gate
                    // try and create the device
                    Device gateDevice = new Gate(name, location, state, profileType);
                    // return it
                    return gateDevice;
                }
            } catch (DeviceException e) {
                // user input has a Device spec creation error
                negativeMessage(e.getMessage());
            }
            return null;
        } while (!sectionFinished);
    }

    private static void deleteDeviceType(DeviceType deviceType) {
        try {
            if (showDeviceType(deviceType)) {
                System.out.println("Which " + deviceType + "do you want to delete");
                try {
                    Byte delDeviceInt = input.nextByte();
                    ArrayList<Device> devices = csvAccess.convertCSVToDevice();
                    if (devices.get(delDeviceInt + 1).getDeviceType().equals(deviceType)) {
                        csvAccess.deleteRow(delDeviceInt + 1, devicesFile);
                    } else {
                        negativeMessage("Sorry the device with this ID isnt a " + deviceType + "!!");
                    }
                } catch (InputMismatchException e) {
                    negativeMessage("Please enter a number");
                }
            } else {
                negativeMessage("Can't delete anything because there are no " + deviceType + "s");
            }
        } catch (IllegalArgumentException | InputMismatchException e) {
            negativeMessage(e.getMessage());
        }
    }

    /**
     * Display the {@code Devices} which match the parameter that are being stored
     * in {@code Devices.csv}
     * 
     * @param deviceType
     * @return true or false depending on if there are any devices with the same
     *         DeviceType as the one as the parameter
     * @throws IllegalArgumentException if there are more than 127 devices stored in
     *                                  {@code Devices.csv}
     */
    private static Boolean showDeviceType(DeviceType deviceType) {
        // store the csv contents into devices
        ArrayList<Device> devices = csvAccess.convertCSVToDevice();
        if (!devices.isEmpty()) {
            boolean displayed = false;// used to check if a device was display (meaning there is at least one device
                                      // that has the same DeviceType)
            // loop through each device
            for (byte i = 0; i < devices.size(); i++) {
                if (i == -128) {
                    throw new IllegalArgumentException("There are more than 127 Devices being stored");
                }
                // check if that Device has the same DeviceType as the parameter
                // then display the Device
                if (devices.get(i).getDeviceType() == deviceType) {
                    System.out.println("\nDevice Number: " + (i + 1) + devices.get(i));
                    displayed = true;
                }
            }

            // check if there has been a device that has been displayed
            if (displayed == false) {
                negativeMessage("There are no " + deviceType + "s to display");
                // return false (No Devices with that DeviceType)
                return false;
            } else {
                // return true (There is/are Devices with that DeviceType)
                return true;
            }
        } else {
            // return false (there are no Devices)
            return false;
        }
    }
    // ------------------------------END OF DEVICES----------------------------- \\

    // -----------------------------START OF LIGHTING---------------------------- \\

    /**
     * Lighting Menu System
     */
    private static void lightingSettings() {
        String options[] = { "Show Lights", "Delete Light", "Add Light",
                "Back to Device Menu" };// add requests
        Menu menu = new Menu("Lighting Menu", options);

        boolean finished = false;

        do {
            byte option = menu.getUserChoice();
            switch (option) {
                case 1:
                    try {
                        showDeviceType(DeviceType.LIGHTING);
                    } catch (IllegalArgumentException e) {
                        negativeMessage(e.getMessage());
                    }
                    break;
                case 2:
                    deleteDeviceType(DeviceType.LIGHTING);
                    break;
                case 3:
                    addLight();
                    break;
                case 4:
                    finished = true;
                    break;
                default:
                    System.out.println("Not a valid option.");
            }
        } while (!finished);
    }

    private static void addLight() {
        try {
            addDevice(createDeviceByUserInput(DeviceType.LIGHTING));
        } catch (ProfileException e) {
            negativeMessage(e.getMessage());
        }
    }

    // ------------------------------END OF LIGHTING----------------------------- \\

    // -----------------------------START OF HEATING---------------------------- \\
    /**
     * Heating Menu System
     */
    private static void heatingSettings() {
        String options[] = { "Show Heating", "Delete Heating", "Add a Heating",
                "Back to Device Menu" };
        Menu menu = new Menu("Heating Menu", options);

        boolean finished = false;

        do {
            byte option = menu.getUserChoice();
            switch (option) {
                case 1:
                    try {
                        showDeviceType(DeviceType.LIGHTING);
                    } catch (IllegalArgumentException e) {
                        negativeMessage(e.getMessage());
                    }
                    break;
                case 2:
                    deleteDeviceType(DeviceType.HEATING);
                    break;
                case 3:
                    addHeating();
                    break;
                case 4:
                    finished = true;
                    break;
                default:
                    System.out.println("Not a valid option.");
            }
        } while (!finished);
    }

    private static void addHeating() {
        try {
            createDeviceByUserInput(DeviceType.HEATING);
        } catch (ProfileException e) {
            negativeMessage(e.getMessage());
        }
    }
    // ------------------------------END OF HEATING----------------------------- \\

    // ------------------------------START OF CAMERA----------------------------- \\

    /**
     * Camera Menu System
     */
    private static void cameraSettings() {
        String options[] = { "Show Cameras", "Delete Camera", "Add Camera",
                "Back to Device Menu" };
        Menu menu = new Menu("Camera Menu", options);

        boolean finished = false;

        do {
            byte option = menu.getUserChoice();
            switch (option) {
                case 1:
                    try {
                        showDeviceType(DeviceType.LIGHTING);
                    } catch (IllegalArgumentException e) {
                        negativeMessage(e.getMessage());
                    }
                    break;
                case 2:
                    deleteDeviceType(DeviceType.CAMERA);
                    break;
                case 3:
                    addCamera();
                    break;
                case 4:
                    finished = true;
                    break;
                default:
                    System.out.println("Not a valid option.");
            }
        } while (!finished);
    }

    private static void addCamera() {
        try {
            addDevice(createDeviceByUserInput(DeviceType.CAMERA));
        } catch (ProfileException e) {
            negativeMessage(e.getMessage());
        }
    }

    // -------------------------------END OF CAMERA------------------------------ \\

    // -------------------------------START OF GATE------------------------------ \\

    /**
     * Gate Menu System
     */
    private static void gateSettings() {
        String options[] = { "Show Gates", "Delete Gate", "Add Gate",
                "Back to Device Menu" };
        Menu menu = new Menu("Gate Menu", options);

        boolean finished = false;

        do {
            byte option = menu.getUserChoice();
            switch (option) {
                case 1:
                    try {
                        showDeviceType(DeviceType.LIGHTING);
                    } catch (IllegalArgumentException e) {
                        negativeMessage(e.getMessage());
                    }
                    break;
                case 2:
                    deleteDeviceType(DeviceType.GATE);
                    break;
                case 3:
                    addGate();
                    break;
                case 4:
                    finished = true;
                    break;
                default:
                    System.out.println("Not a valid option.");
            }
        } while (!finished);
    }

    private static void addGate() {
        try {
            addDevice(createDeviceByUserInput(DeviceType.GATE));
        } catch (ProfileException e) {
            negativeMessage(e.getMessage());
        }
    }

    // --------------------------------END OF GATE------------------------------- \\

    // -----------------------------START OF MESSAGES--------------------------- \\
    /**
     * Prints the message to the system in green text
     * 
     * @param message
     */
    private static void positiveMessage(String message) {
        System.out.println(GREENTEXT + message + RESETTEXTCOLOUR);
    }

    /**
     * Prints the message to the system in red text
     * 
     * @param message
     */
    private static void negativeMessage(String message) {
        System.out.println(REDTEXT + message + RESETTEXTCOLOUR);
    }
    // ------------------------------END OF MESSAGES---------------------------- \\
}

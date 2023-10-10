package Application;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import Application.CustomExceptions.DeviceException;
import Application.CustomExceptions.ProfileException;
import Household.Device.Device;
import Household.Device.DeviceType;
import Household.Device.State;
import Household.Device.Camera.Camera;
import Household.Device.Gate.Gate;
import Household.Device.Heating.Heating;
import Household.Device.Heating.HeatingSched;
import Household.Device.Lighting.Lighting;
import Household.Profile.Child;
import Household.Profile.Parent;
import Household.Profile.Profile;
import Household.Profile.ProfileType;

/**
 * @author Deaglan Lee
 */
public class App {

    // Misc
    private static Scanner input = new Scanner(System.in);
    private static CSVAccess csvAccess = new CSVAccess();

    // Files
    private static File devicesFile = new File("Devices.csv");
    private static File profilesFile = new File("Profiles.csv");
    // private static File heatingSchedules = new File("HeatingSchedules.csv");

    // Profile
    static Profile userProfile;

    // Text Colour
    private static final String RESETTEXTCOLOUR = "\u001B[0m";
    // private static final String ANSI_BLACK = "\u001B[30m";
    private static final String REDTEXT = "\u001B[31m";
    private static final String GREENTEXT = "\u001B[32m";
    // private static final String ANSI_YELLOW = "\u001B[33m";
    // private static final String ANSI_BLUE = "\u001B[34m";
    // private static final String ANSI_PURPLE = "\u001B[35m";
    // private static final String ANSI_CYAN = "\u001B[36m";
    // private static final String ANSI_WHITE = "\u001B[37m";

    // Messages
    private static final String USERERRORENTERNUMBER = "Please Enter A Valid Number!";
    private static final String DEVICEADDED = "The Device has been added!!!";

    public static void main(String[] args) {
        MultiThread testMultiThread = new MultiThread(1);
        Thread thread1 = new Thread(testMultiThread);
        thread1.start();

        welcome();

        if (userProfile.getProfileType() == ProfileType.PARENT) {
            parentProfileSettings();
        } else {
            childProfileSettings();
        }

        input.close();
        thread1.interrupt();
    }

    /**
     * Welcome Screen First Thing You See
     */
    private static void welcome() {
        // Read from the csv and store contents
        ArrayList<Profile> profiles = csvAccess.convertCSVToProfile();

        System.out.println("Welcome to this App! \n:D / !");

        // check if there are profiles in the csv
        if (profiles.isEmpty()) {
            userProfile = new Parent("TempAccount", "TempAccountPassword");
            System.out.println(userProfile.getUsername());
            addProfile();
            System.out.println(userProfile.getUsername());
            return;
        }

        // display the profiles
        for (int i = 0; i < profiles.size(); i++) {
            System.out.println("\nID: " + (i + 1) + profiles.get(i));
        }

        System.out.print("\nPlease Select a Profile\nEnter ID: ");

        try {
            // get user input and check its valid
            int userProfileint = input.nextInt();
            if (userProfileint <= profiles.size() && userProfileint > 0) {
                input.nextLine();

                // temp store the profile they selected
                Profile temp;
                temp = profiles.get(userProfileint - 1);
                int retry = 3;// retry attempts

                // while the user has not guessed the correct password and is within 3 attempts
                while (retry != 1) {
                    System.out.print(
                            "\nPlease Enter The Password for: " + temp.getUsername() + "\nPassword: ");
                    String inputstr = input.nextLine();
                    if (inputstr.equals(temp.getPassword())) {
                        userProfile = temp;
                        positiveMessage("Signing In");
                        TimeUnit.SECONDS.sleep(5);
                        return;
                    } else {
                        retry--;
                    }
                }
            } else {
                negativeMessage("\nPlease Enter A Valid Number!");
            }
        } catch (InputMismatchException e) {
            negativeMessage("\nPlease Enter A Number!");
            input.nextLine();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    // -------------------------START OF PARENT PROFILES-------------------------\\
    /**
     * Profile Settings Menu viewed as a parent
     */
    private static void parentProfileSettings() {
        String options[] = { "Add a Profile", "Remove a Profile", "List Child Profiles", "List Parent Profiles",
                "List All Profiles", "Edit Profile", "Device Settings",
                "Back to Main Menu" };
        Menu menu = new Menu("Profiles Menu", options);

        boolean finished = false;

        do {
            int option = menu.getUserChoice();
            switch (option) {
                case 1:
                    addProfile();
                    break;
                case 2:
                    removeProfile();
                    break;
                case 3:
                    listProfiles(ProfileType.CHILD);
                    break;
                case 4:
                    listProfiles(ProfileType.PARENT);
                    break;
                case 5:
                    listAllProfiles();
                    break;
                case 6:
                    // editProfile();
                    break;
                case 7:
                    deviceSettingsParent();
                    break;
                case 8:
                    finished = true;
                    break;
                default:
                    System.out.println("Not a valid option.");
            }
        } while (!finished);
    }

    /**
     * User enters information of a profile which is added to the profile csv
     */
    private static void addProfile() {
        // username
        System.out.print("\n\nCreating Profile\n\nPlease Enter a Username: ");
        String username = input.nextLine().trim();

        // password
        System.out.print("\nPlease Enter a Password: ");
        String password = input.nextLine().trim();

        // profile type
        ProfileType profileType = ProfileType.INITIALISE;
        boolean sectionFinished = false;
        do {
            System.out.print("\nPlease enter a Profile Type\n1. Parent\n2. Child\nEnter Number: ");
            try {
                int profileTypeint = input.nextInt();
                switch (profileTypeint) {
                    case 1:
                        profileType = ProfileType.PARENT;
                        sectionFinished = true;
                        break;

                    case 2:
                        profileType = ProfileType.CHILD;
                        sectionFinished = true;
                        break;

                    default:
                        negativeMessage("Please enter 1 or 2!");
                        break;
                }
            } catch (InputMismatchException e) {
                negativeMessage("Please enter a number!! 1 OR 2");
                input.nextLine();
            }
            input.nextLine();
        } while (!sectionFinished);

        // create the profile then add it
        try {
            if (profileType == ProfileType.PARENT) {
                Profile parentProfile = new Parent(username, password);
                parentProfile.add(parentProfile, userProfile);
                positiveMessage("Profile Successfully Added");
                userProfile = parentProfile;
                return;
            } else {
                Profile childProfile = new Child(username, password);
                childProfile.add(childProfile, userProfile);
                positiveMessage("Profile Successfully Added");
                userProfile = childProfile;
                return;
            }
        } catch (ProfileException e) {
            negativeMessage(e.getMessage());
        }
    }

    /**
     * Delete a Profile from the Profiles.csv
     */
    private static void removeProfile() {
        boolean profileRemoved = false;
        do {
            try {
                // if something was displayed
                if (listAllProfiles()) {
                    // get profile number that the user wants to delete
                    System.out.print("\nWhich Profile do you want to remove?\nEnter Number: ");
                    int delProfileInt = input.nextInt();
                    input.nextLine();

                    userProfile.remove(userProfile, delProfileInt);

                    // tell the user it was deleted
                    positiveMessage("Profile Deleted");

                }
            } catch (ProfileException e) {
                negativeMessage(e.getMessage());
            } catch (IndexOutOfBoundsException | InputMismatchException e) {
                negativeMessage("Please enter a valid number");
            }
        } while (!profileRemoved);
    }

    /**
     * Display profiles given a certain profile type
     * 
     * @param profileType
     */
    private static void listProfiles(ProfileType profileType) {
        // read and store each profile
        ArrayList<Profile> profiles = csvAccess.convertCSVToProfile();

        if (!profiles.isEmpty()) {
            // profiles is not empty
            boolean displayed = false;
            for (int i = 0; i < profiles.size(); i++) {
                if (profiles.get(i).getProfileType() == profileType) {
                    System.out.println("\nProfile Number: " + (i + 1) + profiles.get(i));
                    displayed = true;
                }
            }
            if (displayed == false) {
                negativeMessage("There are no " + profileType + " Profiles to display");
            }
            return;
        }
        // profiles is empty
        negativeMessage("\nThere are no Profiles");
    }

    /**
     * Display ALL the Profiles in Profiles.csv
     * 
     * @return true if something was displayed false if nothing was displayed
     */
    private static boolean listAllProfiles() {
        // read and store the csv contents into profiles
        ArrayList<Profile> profiles = csvAccess.convertCSVToProfile();

        if (!profiles.isEmpty()) {
            // display each profile
            int count = 1;
            for (Profile profile : profiles) {
                System.out.println("\nProfile Number: " + count + profile);
                count++;
            }
        } else {
            negativeMessage("There are no Profiles to display here!");
            return false;
        }

        return true;
    }

    // --------------------------END OF PARENT PROFILES--------------------------\\

    // --------------------------START OF CHILD PROFILES--------------------------\\

    /**
     * Profile Settings Menu viewed as a child account
     */
    private static void childProfileSettings() {
        String options[] = { "List Child Profiles", "List Parent Profiles", "List All Profiles", "Device Settings",
                "Back to Main Menu" };
        Menu menu = new Menu("Profiles Menu", options);

        boolean finished = false;

        do {
            int option = menu.getUserChoice();
            switch (option) {
                case 1:
                    listProfiles(ProfileType.CHILD);
                    break;
                case 2:
                    listProfiles(ProfileType.PARENT);
                    break;
                case 3:
                    listAllProfiles();
                    break;
                case 4:
                    deviceSettingsChild();
                    break;
                case 5:
                    finished = true;
                    break;
                default:
                    System.out.println("Not a valid option.");
            }
        } while (!finished);
    }

    // ---------------------------END OF CHILD PROFILES---------------------------\\

    // -----------------------------START OF DEVICES-----------------------------\\

    /**
     * Device menu system for a child profile
     */
    private static void deviceSettingsChild() {
        String options[] = { "Show All Devices", "Lighting Settings", "Heating Settings", "Camera Settings",
                "Gate Settings" };
        Menu menu = new Menu("Device Menu", options);

        boolean finished = false;

        do {
            int option = menu.getUserChoice();
            switch (option) {
                case 1:
                    showDevices();
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
        showDevices();

        // store the Devices from the CSV
        ArrayList<Device> devices = csvAccess.convertCSVToDevice();

        System.out.print("\nWhich Device do you want to delete?\nEnter Number: ");
        try {

            int delProfileInt = input.nextInt();
            input.nextLine();

            devices.get(delProfileInt-1).remove(userProfile, delProfileInt);

            // tell the user it was deleted
            positiveMessage("Profile Deleted");

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
            // System.out.print("\n\nDo you want to change any of the Devices States?\n1.
            // Yes\n2. No\nEnter Number: ");
            // try {
            // byte addDeviceOptions = input.nextByte();
            // if (addDeviceOptions == 2) {
            // return;
            // }

            // System.out.println("\nWhich Device would you like to change?\nEnter Device
            // Number: ");
            // addDeviceOptions = input.nextByte();
            // if (addDeviceOptions <= devices.size() && addDeviceOptions > 0) {
            // if (devices.get(addDeviceOptions + 1).getState().equals(State.ON)) {
            // devices.get(addDeviceOptions + 1).setState(State.OFF);
            // sectionFinished = true;
            // } else if (devices.get(addDeviceOptions + 1).getState().equals(State.OFF)) {
            // devices.get(addDeviceOptions + 1).setState(State.ON);
            // sectionFinished = true;
            // } else if (devices.get(addDeviceOptions + 1).getState().equals(State.OPEN)) {
            // devices.get(addDeviceOptions + 1).setState(State.CLOSE);
            // sectionFinished = true;
            // } else if (devices.get(addDeviceOptions + 1).getState().equals(State.CLOSE))
            // {
            // devices.get(addDeviceOptions + 1).setState(State.OPEN);
            // sectionFinished = true;
            // } else {

            // }
            // } else {
            // negativeMessage(USERERRORENTERNUMBER);
            // }
            // } catch (InputMismatchException e) {
            // negativeMessage(USERERRORENTERNUMBER);
            // input.nextLine();
            // }
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
        String options[] = { "Show Heating", "Delete Heating", "Add a Heating", "Create A Heating Schedule",
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
                    createHeatingSchedule();
                case 5:
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

    // ------------------------START OF HEATING SCHEDULE------------------------ \\
    private static void createHeatingSchedule() {

        // initial variable
        boolean loopFinished = false;

        // Heating parameters
        String nameHeating = "";
        String locationHeating = "";
        State stateHeating = State.INITIALISE;
        ProfileType profileTypeHeating = ProfileType.INITIALISE;

        // display the Heating devices until they pick a valid one
        do {

            // display all the heating
            ArrayList<Device> devices = csvAccess.convertCSVToDevice();
            byte count = 1;
            // What happens if there is more than 128 devices?
            for (Device device : devices) {
                if (device.getDeviceType().equals(DeviceType.HEATING)) {
                    System.out.println("Heating ID: " + count + "\n" + device);
                }
                count++;
            }

            // ask user which one they want
            System.out.println("Which Heating device do you want the schedule for? \nEnter Number");

            // make sure the app doesnt crash if they enter a string
            try {
                byte userNameID = input.nextByte();
                if (devices.get(userNameID + 1).getDeviceType() == DeviceType.HEATING) {
                    nameHeating = devices.get(userNameID + 1).getName();
                    locationHeating = devices.get(userNameID + 1).getLocation();
                    stateHeating = devices.get(userNameID + 1).getState();
                    profileTypeHeating = devices.get(userNameID + 1).getProfileType();

                } else {
                    negativeMessage("Please Select a valid heating!!!");
                }
            } catch (InputMismatchException e) {
                negativeMessage("Please enter a number!!!");
            }
        } while (!loopFinished);
        // create the selected heating and store it in RAM
        Heating heatingName = new Heating(nameHeating, locationHeating, stateHeating, profileTypeHeating);
        // RESET loopFinished
        loopFinished = false;

        // Year
        String startYear = "";
        do {
            System.out.println("What Year do you want " + heatingName.getName() + " to start?");
            try {
                short tempYearInt = input.nextShort();
                if (tempYearInt >= 00 && tempYearInt <= 24) {
                    startYear = Integer.toString(tempYearInt);
                    loopFinished = true;
                } else {
                    negativeMessage("please enter a value between 0 and 59");
                }
            } catch (InputMismatchException e) {
                negativeMessage("Please enter a number");
                input.nextLine();
            }

        } while (!loopFinished);
        // RESET
        loopFinished = false;

        // Month
        String startMonth = "";
        do {
            // display options (Months)
            System.out.println(
                    "What Month do you want " + heatingName.getName()
                            + " to start?\n1. January     2. Febuary     3. March     4. April\n5. May     6. June     7.July     8. August\n9. September     10. October     11. November     12. December");
            try {

                byte tempMonth = input.nextByte();
                // check if the number is a valid month
                if (tempMonth < 1 || tempMonth > 12) {
                    negativeMessage("Please enter a number between 1 - 12");
                } else {
                    startMonth = Byte.toString(tempMonth);
                    loopFinished = true;
                }
            } catch (Exception e) {
                negativeMessage("Please enter a number");
                input.nextLine();
            }

        } while (!loopFinished);
        loopFinished = false;

        // Date
        String startDate = "";
        do {
            System.out.println("What day of the Month do you want " + heatingName.getName() + " to start?");
            try {
                byte tempDateInt = input.nextByte();

                switch (startMonth) {
                    case "1":// <January>
                        if (tempDateInt >= 0 && tempDateInt <= 31) {
                            // set date
                            startDate = Byte.toString(tempDateInt);
                            loopFinished = true;
                        }
                        break;
                    case "2":// <February>
                        if (tempDateInt >= 0 && tempDateInt <= 29) {
                            short tempyear = Short.parseShort(startYear);
                            if (tempyear % 400 == 0 && tempyear % 100 == 0 && tempDateInt == 29) {
                                // set date
                                startDate = Byte.toString(tempDateInt);
                                loopFinished = true;
                            } else {
                                // set date
                                startDate = Byte.toString(tempDateInt);
                                loopFinished = true;
                            }
                        }
                        break;
                    case "3":// <March>
                        if (tempDateInt >= 0 && tempDateInt <= 31) {
                            // set date
                            startDate = Byte.toString(tempDateInt);
                            loopFinished = true;
                        }
                        break;
                    case "4":// <April>
                        if (tempDateInt >= 0 && tempDateInt <= 30) {
                            // set date
                            startDate = Byte.toString(tempDateInt);
                            loopFinished = true;
                        }
                        break;
                    case "5":// <May>
                        if (tempDateInt >= 0 && tempDateInt <= 31) {
                            // set date
                            startDate = Byte.toString(tempDateInt);
                            loopFinished = true;
                        }
                        break;
                    case "6":// <June>
                        if (tempDateInt >= 0 && tempDateInt <= 30) {
                            // set date
                            startDate = Byte.toString(tempDateInt);
                            loopFinished = true;
                        }
                        break;
                    case "7":// <July>
                        if (tempDateInt >= 0 && tempDateInt <= 31) {
                            // set date
                            startDate = Byte.toString(tempDateInt);
                            loopFinished = true;
                        }
                        break;
                    case "8":// <August>
                        if (tempDateInt >= 0 && tempDateInt <= 31) {
                            // set date
                            startDate = Byte.toString(tempDateInt);
                            loopFinished = true;
                        }
                        break;
                    case "9":// <September>
                        if (tempDateInt >= 0 && tempDateInt <= 30) {
                            // set date
                            startDate = Byte.toString(tempDateInt);
                            loopFinished = true;
                        }
                        break;
                    case "10":// <October>
                        if (tempDateInt >= 0 && tempDateInt <= 31) {
                            // set date
                            startDate = Byte.toString(tempDateInt);
                            loopFinished = true;
                        }
                        break;
                    case "11":// <November>
                        if (tempDateInt >= 0 && tempDateInt <= 30) {
                            // set date
                            startDate = Byte.toString(tempDateInt);
                            loopFinished = true;
                        }
                        break;
                    case "12":// <December>
                        if (tempDateInt >= 0 && tempDateInt <= 31) {
                            // set date
                            startDate = Byte.toString(tempDateInt);
                            loopFinished = true;
                        }
                        break;
                    default:
                        negativeMessage("Incorrect date");
                        break;
                }
            } catch (InputMismatchException e) {
                negativeMessage("Please enter a number");
                input.nextLine();
            }
        } while (!loopFinished);
        // RESET
        loopFinished = false;

        // Hour
        String startHour = "";
        do {
            System.out.println("What Hour do you want " + heatingName.getName() + " to start?");
            try {
                int tempHourInt = input.nextInt();
                if (tempHourInt >= 00 && tempHourInt <= 24) {
                    startHour = Integer.toString(tempHourInt);
                    loopFinished = true;
                } else {
                    System.out.println("please enter a value between 0 and 59");
                }
            } catch (Exception e) {
                negativeMessage("Please enter a number!!");
                input.nextLine();
            }

        } while (!loopFinished);
        // RESET
        loopFinished = false;

        // Minute
        String startMinute = "";
        do {
            System.out.println("What minute do you want " + heatingName.getName() + " to start?");
            try {
                byte tempMinuteInt = input.nextByte();
                if (tempMinuteInt >= 00 && tempMinuteInt <= 59) {
                    startMinute = Byte.toString(tempMinuteInt);
                    loopFinished = true;
                } else {
                    negativeMessage("please enter a value between 0 and 59");
                }
            } catch (InputMismatchException e) {
                negativeMessage("Please enter a number!!");
                input.nextLine();
            }

        } while (!loopFinished);
        // RESET
        loopFinished = false;

        // All Loops for Start should be done

        // -------------------End Date and Time-----------------------------

        // Year
        String endYear = "";
        do {
            System.out.println("What Year do you want <it> to start?"); // change <it> to name
            try {
                short tempYearInt = input.nextShort();

                if (tempYearInt >= LocalDateTime.now().getYear()
                        && tempYearInt <= (LocalDateTime.now().getYear() + 50)) {
                    endYear = Short.toString(tempYearInt);
                    loopFinished = true;
                } else {
                    negativeMessage("please enter a value between 0 and 59");
                }
            } catch (InputMismatchException e) {
                negativeMessage("Please enter a number!!");
                input.nextLine();
            }

        } while (!loopFinished);
        // RESET
        loopFinished = false;

        // Month
        String endMonth = "";
        do {
            // display options (Months)
            System.out.println(
                    "What Month do you want " + heatingName.getName()
                            + " to start?\n1. January     2. Febuary     3. March     4. April\n5. May     6. June     7.July     8. August\n9. September     10. October     11. November     12. December");
            try {

                Byte tempMonth = input.nextByte();
                // check if the number is a valid month
                if (tempMonth < 1 || tempMonth > 12) {
                    negativeMessage("Please enter a number between 1 - 12");
                } else {
                    endMonth = Byte.toString(tempMonth);
                    loopFinished = true;
                }
            } catch (InputMismatchException e) {
                negativeMessage("Please enter a number");
                input.nextLine();
            }

        } while (!loopFinished);
        loopFinished = false;

        // Date
        String endDate = "";
        do {
            System.out.println("What day of the Month do you want " + heatingName.getName() + " to start?");
            try {
                Byte tempDateInt = input.nextByte();

                switch (endMonth) {
                    case "1":// <January>
                        if (tempDateInt >= 0 && tempDateInt <= 31) {
                            // set date
                            endDate = Byte.toString(tempDateInt);
                            loopFinished = true;
                        }
                        break;
                    case "2":// <February>
                        if (tempDateInt >= 0 && tempDateInt <= 29) {
                            int tempyear = Byte.parseByte(endYear);
                            if (tempyear % 400 == 0 && tempyear % 100 == 0 && tempDateInt == 29) {
                                // set date
                                endDate = Byte.toString(tempDateInt);
                                loopFinished = true;
                            } else {
                                // set date
                                endDate = Byte.toString(tempDateInt);
                                loopFinished = true;
                            }
                        }
                        break;
                    case "3":// <March>
                        if (tempDateInt >= 0 && tempDateInt <= 31) {
                            // set date
                            endDate = Byte.toString(tempDateInt);
                            loopFinished = true;
                        }
                        break;
                    case "4":// <April>
                        if (tempDateInt >= 0 && tempDateInt <= 30) {
                            // set date
                            endDate = Byte.toString(tempDateInt);
                            loopFinished = true;
                        }
                        break;
                    case "5":// <May>
                        if (tempDateInt >= 0 && tempDateInt <= 31) {
                            // set date
                            endDate = Byte.toString(tempDateInt);
                            loopFinished = true;
                        }
                        break;
                    case "6":// <June>
                        if (tempDateInt >= 0 && tempDateInt <= 30) {
                            // set date
                            endDate = Byte.toString(tempDateInt);
                            loopFinished = true;
                        }
                        break;
                    case "7":// <July>
                        if (tempDateInt >= 0 && tempDateInt <= 31) {
                            // set date
                            endDate = Byte.toString(tempDateInt);
                            loopFinished = true;
                        }
                        break;
                    case "8":// <August>
                        if (tempDateInt >= 0 && tempDateInt <= 31) {
                            // set date
                            endDate = Byte.toString(tempDateInt);
                            loopFinished = true;
                        }
                        break;
                    case "9":// <September>
                        if (tempDateInt >= 0 && tempDateInt <= 30) {
                            // set date
                            endDate = Byte.toString(tempDateInt);
                            loopFinished = true;
                        }
                        break;
                    case "10":// <October>
                        if (tempDateInt >= 0 && tempDateInt <= 31) {
                            // set date
                            endDate = Byte.toString(tempDateInt);
                            loopFinished = true;
                        }
                        break;
                    case "11":// <November>
                        if (tempDateInt >= 0 && tempDateInt <= 30) {
                            // set date
                            endDate = Byte.toString(tempDateInt);
                            loopFinished = true;
                        }
                        break;
                    case "12":// <December>
                        if (tempDateInt >= 0 && tempDateInt <= 31) {
                            // set date
                            endDate = Byte.toString(tempDateInt);
                            loopFinished = true;
                        }
                        break;
                    default:
                        negativeMessage("Incorrect date");
                        break;
                }
            } catch (InputMismatchException e) {
                negativeMessage("Please enter a number");
                input.nextLine();
            }
        } while (!loopFinished);
        // RESET
        loopFinished = false;

        // Hour
        String endHour = "";
        do {
            System.out.println("What Hour do you want " + heatingName.getName() + " to start?");
            try {
                Byte tempHourInt = input.nextByte();
                if (tempHourInt >= 00 && tempHourInt <= 24) {
                    endHour = Byte.toString(tempHourInt);
                    loopFinished = true;
                } else {
                    negativeMessage("please enter a value between 0 and 59");
                }
            } catch (InputMismatchException e) {
                negativeMessage("please enter a number!!!!");
            }
        } while (!loopFinished);
        // RESET
        loopFinished = false;

        // Minute
        String endMinute = "";
        do {
            System.out.println("What minute do you want " + heatingName.getName() + " to start?");
            try {
                Byte tempMinuteInt = input.nextByte();
                if (tempMinuteInt >= 00 && tempMinuteInt <= 59) {
                    endMinute = Byte.toString(tempMinuteInt);
                    loopFinished = true;
                } else {
                    negativeMessage("please enter a value between 0 and 59");
                }
            } catch (InputMismatchException e) {
                negativeMessage("please enter a number!!!");
            }

        } while (!loopFinished);
        // RESET
        loopFinished = false;

        // Combine each part of the date
        String startDateTimeStr = startDate + "/" + startMonth + "/" + startYear + " " + startHour + ":" + startMinute
                + ":00";
        String endDateTimeStr = endDate + "/" + endMonth + "/" + endYear + " " + endHour + ":" + endMinute + ":00";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss", Locale.UK);
        LocalDateTime startDateTime = LocalDateTime.parse(startDateTimeStr, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(endDateTimeStr, formatter);

        // temp
        Byte temperature = -111;
        do {
            System.out.println("What temp do you want" + heatingName.getName() + "to be?\nEnter Number:");
            try {
                Byte tempTemperature = input.nextByte();
                if (tempTemperature <= 30 && tempTemperature >= 10) {
                    temperature = tempTemperature;
                    loopFinished = true;
                } else {
                    negativeMessage("Please enter a number between 10 - 30");
                }
            } catch (InputMismatchException e) {
                negativeMessage("please enter a number!!!");
            }
        } while (loopFinished);

        HeatingSched heatingSched = new HeatingSched(heatingName, startDateTime, endDateTime, temperature);
        heatingSched.addSchedule(heatingSched);

        positiveMessage("Heating Schedule Added :D");
    }

    // -------------------------END OF HEATING SCHEDULE------------------------- \\

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

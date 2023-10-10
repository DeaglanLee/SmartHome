package Household.Profile;

import java.io.File;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import Application.CSVAccess;
import Application.Menu;
import Application.CustomExceptions.ProfileException;

public class profileTester {
    static Scanner input = new Scanner(System.in);
    static CSVAccess csvAccess = new CSVAccess();
    static File profilesFile = new File("Profiles.csv");
    static Profile userProfile;

    public static final String RESETTEXTCOLOUR = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String REDTEXT = "\u001B[31m";
    public static final String GREENTEXT = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static void main(String[] args) {
        welcome();

        if (userProfile.getProfileType() == ProfileType.PARENT) {
            parentProfileSettings();
        } else {
            childProfileSettings();
        }
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
                "List All Profiles", "Edit Profile",
                "Back to Main Menu" };
        Menu menu = new Menu("Profiles Menu", options);

        boolean quit = false;

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
                    quit = true;
                    break;
                default:
                    negativeMessage("Not a valid option.");
            }
        } while (!quit);
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
                    byte delProfileInt = input.nextByte();
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

    // ---------------------------END OF PARENT PROFILES--------------------------\\

    // --------------------------START OF CHILD PROFILES--------------------------\\

    /**
     * Profile Settings Menu viewed as a child account
     */
    private static void childProfileSettings() {
        String options[] = { "List Child Profiles", "List Parent Profiles", "List All Profiles",
                "Back to Main Menu" };
        Menu menu = new Menu("Profiles Menu", options);

        boolean quit = false;

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
                    quit = true;
                    break;
                default:
                    negativeMessage("Not a valid option!!!");
            }
        } while (!quit);
    }

    // ---------------------------END OF CHILD PROFILES---------------------------\\

    // --------------------------START OF MESSAGE TYPES--------------------------\\

    private static void positiveMessage(String message) {
        System.out.println(GREENTEXT + message + RESETTEXTCOLOUR);
    }

    private static void negativeMessage(String message) {
        System.out.println(REDTEXT + message + RESETTEXTCOLOUR);
    }

    // ---------------------------END OF MESSAGE TYPES---------------------------\\
}

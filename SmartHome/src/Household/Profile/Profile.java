package Household.Profile;

import java.io.File;
import java.util.ArrayList;

import Application.CSVAccess;
import Application.CustomExceptions.ProfileException;

/**
 * An absract represtentation of what a profile should look like.
 * 
 */
public abstract class Profile {
    private String username;
    private String password;
    private ProfileType profileType;
    private int permission;

    /**
     * Creates a {@code Profile}
     * 
     * @param username    sets the username of the profile
     * @param password    sets the Password of the profile
     * @param profileType sets the ProfileType of the profile
     * @param permission  sets the Permission of the profile
     * @throws ProfileException If any of the sets fail
     */
    public Profile(String username, String password, ProfileType profileType, byte permission) {
        setUsername(username);
        setPassword(password);
        setPermission(permission);
        setProfileType(profileType);
    }

    /**
     * Used to convert a string types of a profile to the correct types
     * <p>
     * eg from a CSV file
     * 
     * @param username
     * @param password
     * @param profileType
     * @param permission
     * @throws ProfileException If any of the sets fail
     */
    public Profile(String username, String password, String profileType, String permission) {
        setUsername(username);
        setPassword(password);
        setProfileType(profileType);
        setPermission(permission);
    }

    /**
     * Add a Profile to the Profiles CSV
     * 
     * @param profile     is the profile they are trying to add
     * @param userProfile is the current profile of the user that is signed in
     * @throws ProfileException If the {@code userProfile} is <b>NOT</b> a Parent
     *                          Profile
     * @see ProfileType
     * @see CSVAccess
     */
    public void add(Profile profile, Profile userProfile) {
        // check the profile is a Parent profile
        if (userProfile.profileType.equals(ProfileType.PARENT)) {
            // create an Arraylist (parameter for csvAccess writeToCSV)
            ArrayList<String> outputList = new ArrayList<>();
            CSVAccess csvAccess = new CSVAccess();
            outputList.add(csvAccess.convertToCSV(profile));
            // write the Arraylist to the csv
            csvAccess.writeToCSV(new File("Profiles.csv"), outputList);
            return;
        }
        throw new ProfileException(userProfile + " not able to add a profile");
    }

    /**
     * Add a Profile to the Profiles CSV
     * 
     * <p>
     * Unlike {@code add(Profile, Profile)} this only has 1 parameter but does the
     * same thing.
     * 
     * @param profile
     * @see CSVAccess
     */
    public void add(Profile profile) {
        // create an Arraylist (parameter for csvAccess writeToCSV)
        ArrayList<String> outputList = new ArrayList<>();
        CSVAccess csvAccess = new CSVAccess();
        outputList.add(csvAccess.convertToCSV(profile));
        // write the Arraylist to the csv
        csvAccess.writeToCSV(new File("Profiles.csv"), outputList);
    }

    /**
     * Remove a stored Profile from the csv
     * 
     * @param userProfile   is the profile they currently are logged in to
     * @param delProfileInt is the profile they are trying to remove
     * @throws ProfileException if the profile is <b>not</b> a parent profile
     * @throws IllegalArgumentException if the file <b>isn't</b> what is hard coded 
     * @see CSVAccess#deleteRow(int, File) For the HardCoded Filenames that you can use
     */
    public void remove(Profile userProfile, int delProfileInt) {
        if (userProfile.getProfileType().equals(ProfileType.PARENT)) {
            CSVAccess csvAccess = new CSVAccess();
            // delete the profile
            csvAccess.deleteRow(delProfileInt, new File("Profiles.csv"));
            return;
        }
        throw new ProfileException(userProfile + " is not able to delete a profile");
    }

    // getters

    /**
     * Returns the username of a given Profile
     * 
     * @return the Username of the Profile
     */
    public String getUsername() {
        return username;
    }

    /**
     * Return the password of a given Profile
     * 
     * @return the password of the Profile
     */
    public String getPassword() {
        return password;
    }

    /**
     * Return the profileType of a given Profile
     * 
     * @return the profileType of the Profile
     */
    public ProfileType getProfileType() {
        return profileType;
    }

    /**
     * Return the permission of a given Profile
     * 
     * @return the permission of the Profile
     */
    public int getPermission() {
        return permission;
    }

    // setters
    /**
     * Set the username of a Profile
     * 
     * @param username
     * @throws ProfileException If the username length is not between 5 and 20
     *                          (inclusive)
     */
    public void setUsername(String username) {
        // Must only contain between 8 and 20 characters
        if (username.length() >= 5 && username.length() <= 20) {
            this.username = username;
            return;
        }
        throw new ProfileException("Username should be between 5 and 20 Characters Long");
    }

    /**
     * Set the password of a profile
     * 
     * @param password
     * @throws ProfileException If the password is not between 8 and 50 (inclusive)
     */
    public void setPassword(String password) {
        if (password.length() >= 8 && password.length() <= 50) {
            this.password = password;
            return;
        }
        throw new ProfileException("Password should be between 8 and 50 Characters Long");
    }

    /**
     * Set the profileType of a profile.
     * 
     * @param profileType
     * @throws ProfileException If the profileType is not {@code Parent} or
     *                          {@code Child}.
     * @see ProfileType
     */
    public void setProfileType(ProfileType profileType) {
        if (profileType == ProfileType.PARENT || profileType == ProfileType.CHILD) {
            this.profileType = profileType;
            return;
        }
        throw new ProfileException("Username should be between 8 and 20 Characters Long");
    }

    /**
     * Set the profileType of a profile with the profileType being a String.
     * <p>
     * compares the String contents with {@code Parent}, {@code Child} and
     * {@code All} then calls the {@code setProfileType(profileType)} if the
     * contents are the same (ignoring the case).
     * 
     * @param profileType that will be compared
     * @throws ProfileException If the profileType is not {@code Parent},
     *                          {@code Child} or {@code All}.
     * @see ProfileType
     */
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
        throw new ProfileException("Username should be between 8 and 20 Characters Long");
    }

    /**
     * Set the permission number.
     * <p>
     * 1 being full permission and 0 being restricted.
     * 
     * @param permission
     * @throws ProfileException If the permission is not 1 or 0
     */
    public void setPermission(byte permission) {
        // must only been 0 or 1
        if (permission == 0 || permission == 1) {
            this.permission = permission;
            return;
        }
        throw new ProfileException("Permission can only be a 1 or a 0");
    }

    /**
     * Set the permission number by parsing the string to an Integer.
     * <p>
     * Calls the {@code setPermission(int)}
     * 
     * @param permission
     */
    public void setPermission(String permission) {
        setPermission(Byte.parseByte(permission));
    }

    // Display
    public String toString() {
        return "\nUsername: " + username + "\nProfile Type: " + getProfileType();
    }
}

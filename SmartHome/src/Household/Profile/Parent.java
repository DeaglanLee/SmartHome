package Household.Profile;

public class Parent extends Profile {
    private static ProfileType profileType = ProfileType.PARENT;
    private static byte permission = 1;

    public Parent(String username, String password) {
        super(username, password, profileType, permission);
    }

    public Parent(String username, String password, String profileType, String permission){
        super(username, password, profileType, permission);
    }
}

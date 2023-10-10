package Household.Profile;

public class Child extends Profile {
    private static ProfileType profileType = ProfileType.CHILD;
    private static byte permission = 0;

    public Child(String username, String password) {
        super(username, password, profileType, permission);
    }

    public Child(String username, String password, String profileType, String permission){
        super(username, password, profileType, permission);
    }
}

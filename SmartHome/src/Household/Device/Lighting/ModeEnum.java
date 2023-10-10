package Household.Device.Lighting;

public enum ModeEnum {
    Default(0), ChristmasMode(1);

    private int numLocation;
    private String types[] = { "Default Mode", "Christmas Mode"};

    private ModeEnum(int num) {
        numLocation = num;
    }

    public String toString() {
        return types[numLocation];
    }
}

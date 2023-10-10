package Household.Device;

public enum DeviceType {
    CAMERA(0), GATE(1), BLINDS(2), LIGHTING(3), HEATING(4), INITIALISE(5);

    private int numLocation;
    private String types[] = { "Camera", "Gate", "Blinds", "Lighting", "Heating", "Initialising"};

    private DeviceType(int num) {
        numLocation = num;
    }

    public String toString() {
        return types[numLocation];
    }

}

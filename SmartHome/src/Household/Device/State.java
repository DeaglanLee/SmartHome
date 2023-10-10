package Household.Device;

public enum State {
    OPEN(0), CLOSE(1), ON(2), OFF(3), INITIALISE(4);

    private int numLocation;
    private String types[] = { "Open", "Close", "On", "Off","Initialising"};

    private State(int num) {
        numLocation = num;
    }

    public String toString() {
        return types[numLocation];
    }

}

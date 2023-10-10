package Household.Profile;

/**
 * Profile Types that can be used
 */
public enum ProfileType {
    PARENT("Parent"), CHILD("Child"), ALL("All"), INITIALISE("Initalise");
	
	private String info;

	private ProfileType(String str) {
		info = str;
	}

	public String toString() {
		return info;
	}

}

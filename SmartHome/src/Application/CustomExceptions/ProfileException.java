package Application.CustomExceptions;

/**
 * Thrown to indicate a method has been passed an Illegal Argument that is specific to Profile
 */
public class ProfileException extends IllegalArgumentException{
    public ProfileException() {
        super();
    }

    public ProfileException(String message) {
        super(message);
    }
}

package Application.CustomExceptions;

/**
 * Thrown to indicate a method has been passed an Illegal Argument that is specific to Device
 */
public class DeviceException extends IllegalArgumentException{
    public DeviceException() {
        super();
    }

    public DeviceException(String message) {
        super(message);
    }
}

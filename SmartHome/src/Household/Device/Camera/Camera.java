package Household.Device.Camera;

import Application.CustomExceptions.DeviceException;
import Household.Device.Device;
import Household.Device.DeviceType;
import Household.Device.State;
import Household.Profile.ProfileType;

/**
 * A representation of a Camera that extends from {@code Device}
 */
public class Camera extends Device {
    private static final DeviceType deviceType = DeviceType.CAMERA;

    /**
     * Creates a {@code Camera} Device that has to have a certain
     * {@code profileType} to interact with it.
     * <p>
     * The {@code deviceType} is Automatically set to <i>Camera</i>.
     * 
     * <p>
     * Acts the exact same way as
     * {@code Camera(String, String, String, ProfileType)}.
     * 
     * @param name        of the Camera
     * @param location    of the Camera
     * @param status      of the Camera
     * @param profileType that can interact with the Camera
     * @throws DeviceException If any of the parameters setting fails
     */
    public Camera(String name, String location, State status, ProfileType profileType) {
        super(name, location, deviceType, status, profileType);
    }

    /**
     * Creates a {@code Camera} Device that has to have a certain
     * {@code profileType} to interact with it.
     * <p>
     * The {@code deviceType} is Automatically set to <i>Camera</i>.
     * 
     * <p>
     * Acts the exact same way as
     * {@code Camera(String, String, String, ProfileType)}.
     * 
     * @param name        of the Camera
     * @param location    of the Camera
     * @param status      of the Camera
     * @param profileType that can interact with the Camera
     * @throws DeviceException If any of the parameters setting fails
     */
    public Camera(String name, String location, String state, ProfileType profileType) {
        super(name, location, deviceType, state, profileType);
    }

    /**
     * Creates a {@code Camera} Device that by default anyone can interact with.
     * <p>
     * The {@code deviceType} is Automatically set to <i>Camera</i>.
     * 
     * @param name     of the Camera
     * @param location of the Camera
     * @param status   of the Camera
     * @throws DeviceException If the any of the parameters settings fails
     */
    public Camera(String name, String location, State status) {
        super(name, location, deviceType, status);
    }

    /**
     * Creates a {@code Camera} Device that by default anyone can interact with.
     * <p>
     * The {@code deviceType} is Automatically set to <i>Camera</i>.
     * <p>
     * Acts in the same way of {@code Camera(String, String, State)}
     * 
     * @param name     of the Camera
     * @param location of the Camera
     * @param status   of the Camera
     * @throws DeviceException If the any of the parameters settings fails
     */
    public Camera(String name, String location, String state){
        super(name,location,deviceType,state);
    }

    @Override
    public void setState(State status) {
        if (status == State.ON || status == State.OFF) {
            this.status = status;
            return;
        }
        throw new DeviceException("State For A Camera Must Be: On OR Off");
    }
}

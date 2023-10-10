package Application;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

import Household.Device.Device;
import Household.Device.State;
import Household.Device.Heating.HeatingSched;

public class MultiThread implements Runnable {
    private int threadNumber;

    public MultiThread(int threadNumber) {
        this.threadNumber = threadNumber;
    }

    @Override
    public void run() {
        CSVAccess csvAccess = new CSVAccess();
        while (!Thread.currentThread().isInterrupted()) {
            boolean change = false;
            ArrayList<HeatingSched> heatingScheds = csvAccess.convertCSVToHeatingScheds();
            ArrayList<Device> devices = csvAccess.convertCSVToDevice();
            for (HeatingSched heatingSched : heatingScheds) {
                for (Device device : devices) {
                    if (device.getName().equals(heatingSched.getHeating().getName())) {
                        LocalDateTime timeNow = formatDateTime();
                        // check if the time is within the schedule and the change it to ON
                        if (((timeNow.isAfter(heatingSched.getOnTime()) || timeNow.isEqual(heatingSched.getOnTime()))&& timeNow.isBefore(heatingSched.getOffTime())) && device.getState().equals(State.OFF)) {
                            change = true;
                            device.setState(State.ON);
                        } else if (((timeNow.isAfter(heatingSched.getOffTime()) || timeNow.isEqual(heatingSched.getOffTime())) || timeNow.isBefore(heatingSched.getOnTime())) && device.getState().equals(State.ON)) {
                            change = true;
                            device.setState(State.OFF);
                        }
                    }
                }
            }

            if (change) {
                File devicesFile = new File("Devices.csv");
                devicesFile.delete();
                csvAccess.firstLineCSV(devicesFile);
                csvAccess.writeToCSV(devicesFile, csvAccess.devicesCSV(devices));
            }
        }
    }

    private LocalDateTime formatDateTime() {
        LocalDateTime timeNow = LocalDateTime.now();

        String day = Integer.toString(timeNow.getDayOfMonth());
        String month = Integer.toString(timeNow.getMonthValue());
        String year = Integer.toString(timeNow.getYear());

        String hour = Integer.toString(timeNow.getHour());
        String minute = Integer.toString(timeNow.getMinute());

        //gives a 0 value before the number if its only one number eg 1/7 (day/month) = 01/07
        //if you dont do this it crashes
        byte dayCheck = Byte.parseByte(day);
        byte monthCheck = Byte.parseByte(month);
        
        byte hourCheck = Byte.parseByte(hour);
        byte minuteCheck = Byte.parseByte(minute);

        if (dayCheck < 10){
            day = "0" + day;
        }

        if (monthCheck < 10) {
            month = "0" + month;
        }

        if (hourCheck < 10){
            hour = "0" + hour;
        }

        if (minuteCheck < 10) {
            minute = "0" + minute;
        }

        //put the time back together and then format it, then return the formatted date and time
        String currentTime = day + "/" + month + "/" + year + " " + hour + ":" + minute + ":00";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss", Locale.UK);
        return LocalDateTime.parse(currentTime, formatter);
    }
}

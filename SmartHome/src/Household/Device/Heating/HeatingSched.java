package Household.Device.Heating;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;

import Application.CSVAccess;
import Household.Device.Device;

/**
 * Create a Heating Schedule
 * 
 * @author Deaglan Lee
 * @author Michelle Pollock
 * 
 */
public class HeatingSched {
	private Heating heating;
	private LocalDateTime onTime;
	private LocalDateTime offTime;
	private byte temp;

	static CSVAccess csvAccess = new CSVAccess();
	final File heatingSchedule = new File("HeatingSchedule.csv");

	public HeatingSched(Heating heating, LocalDateTime onTime, LocalDateTime offTime, byte temp) {
		setHeating(heating);
		setOnTime(onTime);
		setOffTime(offTime);
		setTemp(temp);
	}

	/**
	 * adds the required informaton to the csv for the schedule
	 */
	public void addSchedule(HeatingSched heatingScheds) {
		ArrayList<String> outputList = new ArrayList<>();
		outputList.add(csvAccess.convertToCSV(heatingScheds));
		csvAccess.writeToCSV(heatingSchedule, outputList);
	}

	// getters
	public Heating getHeating() {
		return heating;
	}

	public byte getTemp() {
		return this.temp;
	}

	public String getCurrentDay() {
		String day = Integer.toString(Calendar.DAY_OF_WEEK);
		return day;
	}

	public LocalDateTime getOnTime() {
		return onTime;
	}

	public LocalDateTime getOffTime() {
		return offTime;
	}

	// setters
	public void setHeating(Heating heating) {
		this.heating = heating;
	}

	/**
	 * Used to convert CSV to Heating
	 * 
	 * @param Heating
	 */
	public void setHeating(String heating) {
		CSVAccess csvAccess = new CSVAccess();
		ArrayList<Device> devices = csvAccess.convertCSVToDevice();
		boolean found = false;
		while (!found) {
			for (Device device : devices) {
				if (device.getName().equals(heating)) {
					Heating newHeating = new Heating(heating, device.getLocation(), device.getState());
					setHeating(newHeating);
					found = true;
				} 
			}
		}
	}

	public void setOnTime(LocalDateTime onTime) {
		this.onTime = onTime;
	}

	/**
	 * Set the time the heating will turn on at
	 * <p>
	 * <b> SHOULD ONLY BE USED WHEN ACCESSED FROM CSV
	 * 
	 * @param onTime
	 */
	public void setOnTime(String onTime) {
		// make sure the string is in the correct time format and is a valid time
		String[] hourDay = onTime.split(":");
		if (Integer.parseInt(hourDay[0]) >= 0 && Integer.parseInt(hourDay[0]) <= 24) {
			// if its valid then convert it then set the status to ON
			if (Integer.parseInt(hourDay[1]) >= 0 && Integer.parseInt(hourDay[0]) <= 59) {
				setOnTime(LocalDateTime.parse(onTime));
			} else {
				throw new IllegalArgumentException("There is only 59 minutes in an hour");
			}

		} else {
			throw new IllegalArgumentException("There is only 23 Hours in a day");
		}
	}

	public void setOffTime(LocalDateTime offTime) {
		this.offTime = offTime;
	}

	/**
	 * Set the time the heating will turn on at
	 * <p>
	 * <b> SHOULD ONLY BE USED WHEN ACCESSED FROM CSV
	 * 
	 * @param onTime
	 */
	public void setOffTime(String onTime) {
		// make sure the string is in the correct time format and is a valid time
		String[] hourDay = onTime.split(":");
		if (Integer.parseInt(hourDay[0]) >= 0 && Integer.parseInt(hourDay[0]) <= 24) {
			// if its valid then convert it then set the status to ON
			if (Integer.parseInt(hourDay[1]) >= 0 && Integer.parseInt(hourDay[0]) <= 59) {
				setOnTime(LocalDateTime.parse(onTime));
			} else {
				throw new IllegalArgumentException("There is only 59 minutes in an hour");
			}

		} else {
			throw new IllegalArgumentException("There is only 23 Hours in a day");
		}
	}

	public void setTemp(byte temp) {
		this.temp = temp;
	}

	public void setTemp(String temp) {
		setTemp(Byte.parseByte(temp));
	}

	/**
	 * Check the Schedule date and time is the same as the Current date and time
	 * 
	 * @param row
	 * @return
	 */
	public void config(int row) {
		ArrayList<HeatingSched> schedules = csvAccess.convertCSVToHeatingScheds();

		// Get current date and time
		LocalDateTime time = LocalDateTime.now();

		// String day

		// //Check if the day is the same as the scheduled day
		// if (time.getHour() == ) {
		// //check if the On Time is the same as the scheduled On Time
		// if (time == schedules.get(row).getOnTime()) {
		// schedules.get(row).setStatus(State.ON);
		// csvAccess.writeToCSV(heatingSchedule, csvAccess.heatingSchedCSV(schedules));
		// } else if (time == schedules.get(row).getOffTime()) { //if its the same as
		// the Off Time
		// schedules.get(row).setStatus(State.OFF);
		// csvAccess.writeToCSV(heatingSchedule, csvAccess.heatingSchedCSV(schedules));
		// }
		// }
	}

	/**
	 * Display the Schedules
	 */
	public void displaySchedules(ArrayList<HeatingSched> heatingScheds) {
		for (HeatingSched heatingSched : heatingScheds) {
			System.out.println(heatingSched);
		}
	}

	public String toString() {
		String res = "";
		res += "\nName: " + getHeating().getName();
		res += "\nOnTime: " + getOnTime();
		res += "\nOffTime: " + getOffTime();
		res += "\nTemp: " + getTemp();

		return res;
	}
}

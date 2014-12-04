package databaseModels;

import org.json.JSONException;
import org.json.JSONObject;

public class Car {
	
	private String carNumber;
	private String brand;
	private String colour;
	private int seats;
	private int year;
	
	public Car(JSONObject carDB){
		try {
			this.carNumber = carDB.getString("carnumber");
			this.brand = carDB.getString("brand");
			this.colour = carDB.getString("colour");
			this.seats = carDB.getInt("seats");
			this.year = carDB.getInt("year");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public String getCarNumber() {
		return carNumber;
	}
	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getColour() {
		return colour;
	}
	public void setColour(String colour) {
		this.colour = colour;
	}
	public int getSeats() {
		return seats;
	}
	public void setSeats(int seats) {
		this.seats = seats;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	
}

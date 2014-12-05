package databaseModels;

import org.json.JSONException;
import org.json.JSONObject;

public class Carpool {
	
	private String username;
	private String source;
	private String destination;
	private String date;
	private String time;
	private int availableSeats;
	private int price;
	private int duration;
	private String itinerary;
	private int commutePeriod;
	
	public Carpool(JSONObject carpoolDB){
		try {
			this.username = carpoolDB.getString("username");
			this.source = carpoolDB.getString("source");
			this.destination = carpoolDB.getString("destination");
			this.date = carpoolDB.getString("date");
			this.time = carpoolDB.getString("time");
			this.availableSeats = carpoolDB.getInt("avseats");
			this.price = carpoolDB.getInt("price");
			this.duration = carpoolDB.getInt("duration");
			this.itinerary = carpoolDB.getString("itinerary");
			this.commutePeriod = carpoolDB.getInt("commper");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getAvailableSeats() {
		return availableSeats;
	}
	public void setAvailableSeats(int availableSeats) {
		this.availableSeats = availableSeats;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public String getItinerary() {
		return itinerary;
	}
	public void setItinerary(String itinerary) {
		this.itinerary = itinerary;
	}
	public int getCommutePeriod() {
		return commutePeriod;
	}
	public void setCommutePeriod(int commutePeriod) {
		this.commutePeriod = commutePeriod;
	}
		
}

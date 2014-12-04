package databaseModels;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String birthDate;
	private String date;
	private String bio;
	private String email;
	private String phone;
	private int driver;
	private int rider;
	private float rankingDriver;
	private float rankingRider;
	private String photo;
	private String city;
	private int smoker;
	
	public User(JSONObject userDB){
		try {
			this.username = userDB.getString("username");
			this.password = userDB.getString("password");
			this.firstName = userDB.getString("firstname");
			this.lastName = userDB.getString("lastname");
			this.birthDate = userDB.getString("birthdate");
			this.bio = userDB.getString("bio");
			this.email = userDB.getString("email");
			this.phone = userDB.getString("phone");
			this.driver = userDB.getInt("driver");
			this.rider = userDB.getInt("rider");
			this.rankingDriver = Float.parseFloat(userDB.getString("rankingd"));
			this.rankingRider = Float.parseFloat(userDB.getString("rankingr"));
			this.photo = userDB.getString("photo");
			this.city = userDB.getString("city");
			this.smoker = userDB.getInt("smoker");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getBio() {
		return bio;
	}
	public void setBio(String bio) {
		this.bio = bio;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getDriver() {
		return driver;
	}
	public void setDriver(int driver) {
		this.driver = driver;
	}
	public int getRider() {
		return rider;
	}
	public void setRider(int rider) {
		this.rider = rider;
	}
	public float getRankingDriver() {
		return rankingDriver;
	}
	public void setRankingDriver(float rankingDriver) {
		this.rankingDriver = rankingDriver;
	}
	public float getRankingRider() {
		return rankingRider;
	}
	public void setRankingRider(float rankingRider) {
		this.rankingRider = rankingRider;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public int getSmoker() {
		return smoker;
	}
	public void setSmoker(int smoker) {
		this.smoker = smoker;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
}

package ca.tbcn.greenp;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class Carpark implements Serializable {

	private static final long serialVersionUID = 1L;
    private String dateScraped; 
    private String paymentOptions;
    private String capacity; 
    private String title; 
    private String url; 
    private String rateDetails; 
    private String facilityType; 
    private String rate; 
    private String paymentTypes; 
    private float lat; 
    private float lng; 
    private String streetAddress;

    public static Carpark fromJSON(JSONObject json) throws JSONException {
    	Carpark c = new Carpark();
    	
    	c.setDateScraped(json.getString("date_scraped"));
    	c.setPaymentOptions(json.getString("payment_options"));
    	c.setCapacity(json.getString("capacity"));
    	c.setTitle(json.getString("title"));
    	c.setUrl(json.getString("url"));
    	c.setRateDetails(json.getString("rate_details"));
    	c.setFacilityType(json.getString("facility_type"));
    	c.setRate(json.getString("rate"));
    	c.setPaymentTypes(json.getString("payment_types"));
    	c.setStreetAddress(json.getString("street_address"));
    	c.setLat(new Float(json.getString("lat")).floatValue());
    	c.setLng(new Float(json.getString("lng")).floatValue());
    	
    	return c;
    }
    
    public String directionsAddress() {
    	return title + "@" + lat + "," + lng;
    }

	public String getDateScraped() {
		return dateScraped;
	}

	public void setDateScraped(String dateScraped) {
		this.dateScraped = dateScraped;
	}

	public String getPaymentOptions() {
		return paymentOptions;
	}

	public void setPaymentOptions(String paymentOptions) {
		this.paymentOptions = paymentOptions;
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRateDetails() {
		return rateDetails;
	}

	public void setRateDetails(String rateDetails) {
		this.rateDetails = rateDetails;
	}

	public String getFacilityType() {
		return facilityType;
	}

	public void setFacilityType(String facilityType) {
		this.facilityType = facilityType;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getPaymentTypes() {
		return paymentTypes;
	}

	public void setPaymentTypes(String paymentTypes) {
		this.paymentTypes = paymentTypes;
	}

	public float getLat() {
		return lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public float getLng() {
		return lng;
	}

	public void setLng(float lng) {
		this.lng = lng;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}

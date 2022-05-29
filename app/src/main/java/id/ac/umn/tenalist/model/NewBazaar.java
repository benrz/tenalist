package id.ac.umn.tenalist.model;

import com.google.gson.annotations.SerializedName;

import java.sql.Time;
import java.util.Date;

public class NewBazaar {
    @SerializedName("bazaarCategoryId")
    private int bazaarCategoryId;

    @SerializedName("bazaarCity")
    private String bazaarCity;

    @SerializedName("bazaarDescription")
    private String bazaarDescription;

    @SerializedName("bazaarEndDate")
    private Date bazaarEndDate;

    @SerializedName("bazaarEndTime")
    private Time bazaarEndTime;

    @SerializedName("bazaarFloorPlan")
    private String bazaarFloorPlan;

    @SerializedName("bazaarLocation")
    private String bazaarLocation;

    @SerializedName("bazaarMOU")
    private String bazaarMOU;

    @SerializedName("bazaarName")
    private String bazaarName;

    @SerializedName("bazaarPoster")
    private String bazaarPoster;

    @SerializedName("bazaarPrice")
    private int bazaarPrice;

    @SerializedName("bazaarStartDate")
    private Date bazaarStartDate;

    @SerializedName("bazaarStartTime")
    private Time bazaarStartTime;

    @SerializedName("bazaarStatusId")
    private int bazaarStatusId;

    @SerializedName("bazaarTotalBooth")
    private int bazaarTotalBooth;

    @SerializedName("bazaarValidation")
    private int bazaarValidation;

    @SerializedName("organizerId")
    private int organizerId;

    public NewBazaar(String bazaarName, String bazaarDescription, Date bazaarStartDate,
                      Date bazaarEndDate, Time bazaarStartTime, Time bazaarEndTime,
                      String bazaarLocation, int bazaarTotalBooth, int bazaarPrice,
                      String bazaarPoster, String bazaarMOU){
        this.bazaarName = bazaarName;
        this.bazaarDescription = bazaarDescription;
        this.bazaarStartDate = bazaarStartDate;
        this.bazaarEndDate = bazaarEndDate;
        this.bazaarStartTime = bazaarStartTime;
        this.bazaarEndTime = bazaarEndTime;
        this.bazaarLocation = bazaarLocation;
        this.bazaarTotalBooth = bazaarTotalBooth;
        this.bazaarPrice = bazaarPrice;
        this.bazaarPoster = bazaarPoster;
        this.bazaarMOU = bazaarMOU;
    }

    public String getBazaarName() { return bazaarName; }
    public String getBazaarDescription() { return bazaarDescription; }
    public Date getBazaarStartDate() { return bazaarStartDate; }
    public Date getBazaarEndDate() { return bazaarEndDate; }
    public Time getBazaarStartTime() { return bazaarStartTime; }
    public Time getBazaarEndTime() { return bazaarEndTime; }
    public String getBazaarLocation() { return bazaarLocation; }
    public int getBazaarTotalBooth() { return bazaarTotalBooth; }
    public int getBazaarPrice() { return bazaarPrice; }
    public String getBazaarPoster() { return bazaarPoster; }
    public String getBazaarMOU() { return bazaarMOU; }

    public void setBazaarName( String bazaarName) { this.bazaarName = bazaarName; }
    public void setBazaarDescription( String bazaarDescription) { this.bazaarDescription = bazaarDescription; }
    public void setBazaarStartDate( Date bazaarStartDate) { this.bazaarStartDate = bazaarStartDate; }
    public void setBazaarEndDate( Date bazaarEndDate) { this.bazaarEndDate = bazaarEndDate; }
    public void setBazaarStartTime( Time bazaarStartTime) { this.bazaarStartTime = bazaarStartTime; }
    public void setBazaarEndTime( Time bazaarEndTime) { this.bazaarEndTime = bazaarEndTime; }
    public void setBazaarLocation( String bazaarLocation) { this.bazaarLocation = bazaarLocation; }
    public void setBazaarTotalBooth( int bazaarTotalBooth) { this.bazaarTotalBooth = bazaarTotalBooth; }
    public void setBazaarPrice( int bazaarPrice) { this.bazaarPrice = bazaarPrice; }
    public void setBazaarPoster( String bazaarPoster) { this.bazaarPoster = bazaarPoster; }
    public void setBazaarMOU( String bazaarMOU) { this.bazaarMOU = bazaarMOU; }
}

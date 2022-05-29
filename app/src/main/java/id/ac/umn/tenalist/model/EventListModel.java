package id.ac.umn.tenalist.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class EventListModel {
    @SerializedName("success")
    public int success;

    @SerializedName("message")
    public String message;

    @SerializedName("bazaarId")
    public int eventId;

    @SerializedName("bazaarName")
    public String eventTitle;

    @SerializedName("bazaarCity")
    public String eventPlace;

    @SerializedName("bazaarStartDate")
    public Date eventStartDate;

    @SerializedName("bazaarEndDate")
    public Date eventEndDate;

    @SerializedName("bazaarStartTime")
    public String eventStartTime;

    @SerializedName("bazaarEndTime")
    public String eventEndTime;

    @SerializedName("bazaarPrice")
    public int eventPrice;

    @SerializedName("bazaarCount")
    public int eventCountDown= 0;

    @SerializedName("bazaarStatusName")
    public String eventStatus;

    @SerializedName("bazaarPoster")
    public String eventImage;

    @SerializedName("bazaarCategoryName")
    public String eventCategory;

    public EventListModel(){}

    public EventListModel(int eventId, String eventTitle, String eventPlace, Date eventStartDate, Date eventEndDate, String eventStartTime, String eventEndTime, int eventPrice, String eventStatus, String eventImage, String eventCategory) {
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.eventPlace = eventPlace;
        this.eventStartDate = eventStartDate;
        this.eventEndDate = eventEndDate;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
        this.eventPrice = eventPrice;
        this.eventStatus = eventStatus;
        this.eventImage = eventImage;
        this.eventCategory = eventCategory;
    }

    public EventListModel(int eventId, String eventTitle, String eventPlace, Date eventStartDate, Date eventEndDate, String eventStartTime, String eventEndTime, int eventPrice, int eventCountDown, String eventStatus, String eventImage, String eventCategory) {
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.eventPlace = eventPlace;
        this.eventStartDate = eventStartDate;
        this.eventEndDate = eventEndDate;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
        this.eventPrice = eventPrice;
        this.eventCountDown = eventCountDown;
        this.eventStatus = eventStatus;
        this.eventImage = eventImage;
        this.eventCategory = eventCategory;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventPlace() {
        return eventPlace;
    }

    public void setEventPlace(String eventPlace) {
        this.eventPlace = eventPlace;
    }

    public Date getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(Date eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public Date getEventEndDate() {
        return eventEndDate;
    }

    public void setEventEndDate(Date eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    public String getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(String eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public String getEventEndTime() {
        return eventEndTime;
    }

    public void setEventEndTime(String eventEndTime) {
        this.eventEndTime = eventEndTime;
    }

    public int getEventPrice() {
        return eventPrice;
    }

    public void setEventPrice(int eventPrice) {
        this.eventPrice = eventPrice;
    }

    public int getEventCountDown() {
        return eventCountDown;
    }

    public void setEventCountDown(int eventCountDown) {
        this.eventCountDown = eventCountDown;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public String getEventImage() {
        return eventImage;
    }

    public void setEventImage(String eventImage) {
        this.eventImage = eventImage;
    }

    public String getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
    }

    public int getSuccess(){ return success;}

    public String getMessage(){ return message;}
}

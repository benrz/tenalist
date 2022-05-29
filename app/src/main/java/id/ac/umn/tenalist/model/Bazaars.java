package id.ac.umn.tenalist.model;

import android.text.format.Time;

import com.google.gson.annotations.SerializedName;


import java.util.Date;
import java.util.List;

public class Bazaars {
    @SerializedName("bazaars")
    public List<Bazaar> bazaar = null;
    public static Bazaars.Bazaar createBazaar = new Bazaars.Bazaar();

    @SerializedName("success")
    public int success;

    @SerializedName("message")
    public String message;

    public static class Bazaar {
        private int bazaarId;
        private int organizerId;
        private String bazaarName;
        private Date bazaarStartDate;
        private Date bazaarEndDate;
        private String bazaarStartTime;
        private String bazaarEndTime;
        private int bazaarPrice;
        private String bazaarLocation;
        private String bazaarCity;
        private boolean bazaarValidation;
        private String bazaarDescription;
        private String bazaarPoster;
        private int bazaarStatusId;
        private String bazaarCategoryName;
        private int bazaarTotalBooth;
        private String bazaarMOU = null;
        private String bazaarImage = null;
        private float organizerRating;

        public Bazaar(){}

        public int getBazaarId() {
            return bazaarId;
        }

        public void setBazaarId(int bazaarId) {
            this.bazaarId = bazaarId;
        }

        public int getOrganizerId() {
            return organizerId;
        }

        public void setOrganizerId(int organizerId) {
            this.organizerId = organizerId;
        }

        public String getBazaarName() {
            return bazaarName;
        }

        public void setBazaarName(String bazaarName) {
            this.bazaarName = bazaarName;
        }

        public Date getBazaarStartDate() {
            return bazaarStartDate;
        }

        public void setBazaarStartDate(Date bazaarStartDate) {
            this.bazaarStartDate = bazaarStartDate;
        }

        public Date getBazaarEndDate() {
            return bazaarEndDate;
        }

        public void setBazaarEndDate(Date bazaarEndDate) {
            this.bazaarEndDate = bazaarEndDate;
        }

        public String getBazaarStartTime() {
            return bazaarStartTime;
        }

        public void setBazaarStartTime(String bazaarStartTime) {
            this.bazaarStartTime = bazaarStartTime;
        }

        public String getBazaarEndTime() {
            return bazaarEndTime;
        }

        public void setBazaarEndTime(String bazaarEndTime) {
            this.bazaarEndTime = bazaarEndTime;
        }

        public int getBazaarPrice() {
            return bazaarPrice;
        }

        public void setBazaarPrice(int bazaarPrice) {
            this.bazaarPrice = bazaarPrice;
        }

        public String getBazaarLocation() {
            return bazaarLocation;
        }

        public void setBazaarLocation(String bazaarLocation) {
            this.bazaarLocation = bazaarLocation;
        }

        public String getBazaarCity() {
            return bazaarCity;
        }

        public void setBazaarCity(String bazaarCity) {
            this.bazaarCity = bazaarCity;
        }

        public boolean isBazaarValidation() {
            return bazaarValidation;
        }

        public void setBazaarValidation(boolean bazaarValidation) {
            this.bazaarValidation = bazaarValidation;
        }

        public String getBazaarDescription() {
            return bazaarDescription;
        }

        public void setBazaarDescription(String bazaarDescription) {
            this.bazaarDescription = bazaarDescription;
        }

        public String getBazaarPoster() {
            return bazaarPoster;
        }

        public void setBazaarPoster(String bazaarPoster) {
            this.bazaarPoster = bazaarPoster;
        }

        public int getBazaarStatusId() {
            return bazaarStatusId;
        }

        public void setBazaarStatusId(int bazaarStatusId) {
            this.bazaarStatusId = bazaarStatusId;
        }

        public String getBazaarCategoryName() {
            return bazaarCategoryName;
        }

        public void setBazaarCategoryName(String bazaarCategoryId) {
            this.bazaarCategoryName = bazaarCategoryId;
        }

        public int getBazaarTotalBooth() {
            return bazaarTotalBooth;
        }

        public void setBazaarTotalBooth(int bazaarTotalBooth) {
            this.bazaarTotalBooth = bazaarTotalBooth;
        }

        public String getBazaarMOU() {
            return bazaarMOU;
        }

        public void setBazaarMOU(String bazaarMOU) {
            this.bazaarMOU = bazaarMOU;
        }

        public String getBazaarImage() {
            return bazaarImage;
        }

        public void setBazaarImage(String bazaarImage) {
            this.bazaarImage = bazaarImage;
        }

        public float getOrganizerRating() {
            return organizerRating;
        }

        public void setOrganizerRating(float organizerRating) {
            this.organizerRating = organizerRating;
        }
    }

    public int getSuccess(){ return success;}

    public String getMessage(){ return message;}

}

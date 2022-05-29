package id.ac.umn.tenalist.model;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.sql.Time;
import java.util.Date;

public class BazaarDetail {
    @SerializedName("result")
    bazaarDetail bazaarDetail;
    @SerializedName("success")
    public int success;

    public BazaarDetail(bazaarDetail bazaarDetail, int success) {
        this.bazaarDetail = bazaarDetail;
        this.success = success;
    }

    public bazaarDetail getBazaarDetail() {
//        Log.d("DEBUG", String.valueOf(bazaarDetail));
        return bazaarDetail;
    }

    public void setBazaarDetail(bazaarDetail bazaarDetail) {
        this.bazaarDetail = bazaarDetail;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public class bazaarDetail{
        @SerializedName("bazaarId")
        private int bazaarId;

        @SerializedName("organizerId")
        private int organizerId;

        @SerializedName("userFullName")
        private String userFullName;

        @SerializedName("organizerRating")
        private float organizerRating;

        @SerializedName("bazaarName")
        private String bazaarName;

        @SerializedName("bazaarStartDate")
        private String bazaarStartDate;

        @SerializedName("bazaarEndDate")
        private String bazaarEndDate;

        @SerializedName("bazaarStartTime")
        private String bazaarStartTime;

        @SerializedName("bazaarEndTime")
        private String bazaarEndTime;

        @SerializedName("bazaarPrice")
        private int bazaarPrice;

        @SerializedName("bazaarStatusId")
        private int bazaarStatusId;

        @SerializedName("bazaarLocation")
        private String bazaarLocation;

        @SerializedName("bazaarCity")
        private String bazaarCity;

        @SerializedName("bazaarValidation")
        private int bazaarValidation;

        @SerializedName("bazaarDescription")
        private String bazaarDescription;

        @SerializedName("bazaarPoster")
        private String bazaarPoster;

        @SerializedName("bazaarStatusName")
        private String bazaarStatusName;

        @SerializedName("bazaarCategoryName")
        private String bazaarCategoryName;

        @SerializedName("bazaarTotalBooth")
        private int bazaarTotalBooth;

        @SerializedName("bazaarFloorPlan")
        private String bazaarFloorPlan;

        @SerializedName("OrganizerEmail")
        private String OrganizerEmail;

        @SerializedName("totalbazaar")
        private int totalbazaar;

        @SerializedName("userPicture")
        private String userPicture;

        public bazaarDetail(){}
        public bazaarDetail(int bazaarId, int organizerId, String userFullName,
                            float organizerRating, String bazaarName,
                            String bazaarStartDate, String bazaarEndDate, String bazaarStartTime,
                            String bazaarEndTime, int bazaarPrice, int bazaarStatusId, String bazaarLocation,
                            String bazaarCity, int bazaarValidation, String bazaarDescription,
                            String bazaarPoster, String bazaarStatusName, String bazaarCategoryName,
                            int bazaarTotalBooth, String bazaarFloorPlan, String OrganizerEmail, int totalbazaar, String userPicture) {
            this.bazaarId = bazaarId;
            this.organizerId = organizerId;
            this.userFullName = userFullName;
            this.organizerRating = organizerRating;
            this.bazaarName = bazaarName;
            this.bazaarStartDate = bazaarStartDate;
            this.bazaarEndDate = bazaarEndDate;
            this.bazaarStartTime = bazaarStartTime;
            this.bazaarEndTime = bazaarEndTime;
            this.bazaarPrice = bazaarPrice;
            this.bazaarStatusId = bazaarStatusId;
            this.bazaarLocation = bazaarLocation;
            this.bazaarCity = bazaarCity;
            this.bazaarValidation = bazaarValidation;
            this.bazaarDescription = bazaarDescription;
            this.bazaarPoster = bazaarPoster;
            this.bazaarStatusName = bazaarStatusName;
            this.bazaarCategoryName = bazaarCategoryName;
            this.bazaarTotalBooth = bazaarTotalBooth;
            this.bazaarFloorPlan = bazaarFloorPlan;
            this.OrganizerEmail = OrganizerEmail;
            this.totalbazaar = totalbazaar;
            this.userPicture = userPicture;
        }

        public String getUserPicture() {
            return userPicture;
        }

        public void setUserPicture(String userPicture) {
            this.userPicture = userPicture;
        }

        public int getTotalbazaar() {
            return totalbazaar;
        }

        public void setTotalbazaar(int totalbazaar) {
            this.totalbazaar = totalbazaar;
        }

        public String getOrganizerEmail() {
            return OrganizerEmail;
        }

        public void setOrganizerEmail(String organizerEmail) {
            OrganizerEmail = organizerEmail;
        }

        public int getBazaarStatusId() {
            return bazaarStatusId;
        }

        public void setBazaarStatusId(int bazaarStatusId) {
            this.bazaarStatusId = bazaarStatusId;
        }

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

        public String getUserFullName() {
            return userFullName;
        }

        public void setUserFullName(String userFullName) {
            this.userFullName = userFullName;
        }

        public float getOrganizerRating() {
            return organizerRating;
        }

        public void setOrganizerRating(float organizerRating) {
            this.organizerRating = organizerRating;
        }

        public String getBazaarName() {
            return bazaarName;
        }

        public void setBazaarName(String bazaarName) {
            this.bazaarName = bazaarName;
        }

        public String getBazaarStartDate() {
            return bazaarStartDate;
        }

        public void setBazaarStartDate(String bazaarStartDate) {
            this.bazaarStartDate = bazaarStartDate;
        }

        public String getBazaarEndDate() {
            return bazaarEndDate;
        }

        public void setBazaarEndDate(String bazaarEndDate) {
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

        public int getBazaarValidation() {
            return bazaarValidation;
        }

        public void setBazaarValidation(int bazaarValidation) {
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

        public String getBazaarStatusName() {
            return bazaarStatusName;
        }

        public void setBazaarStatusName(String bazaarStatusName) {
            this.bazaarStatusName = bazaarStatusName;
        }

        public String getBazaarCategoryName() {
            return bazaarCategoryName;
        }

        public void setBazaarCategoryName(String bazaarCategoryName) {
            this.bazaarCategoryName = bazaarCategoryName;
        }

        public int getBazaarTotalBooth() {
            return bazaarTotalBooth;
        }

        public void setBazaarTotalBooth(int bazaarTotalBooth) {
            this.bazaarTotalBooth = bazaarTotalBooth;
        }

        public String getBazaarFloorPlan() {
            return bazaarFloorPlan;
        }

        public void setBazaarFloorPlan(String bazaarFloorPlan) {
            this.bazaarFloorPlan = bazaarFloorPlan;
        }
    }

}

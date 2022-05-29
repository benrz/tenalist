package id.ac.umn.tenalist;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import id.ac.umn.tenalist.model.BoothSlotNumber;

public class BazaarBooth {
    @SerializedName("slot")
    public List<BoothSlotNumber> bazaarBoothList;
    @SerializedName("success")
    public int success;

    public BazaarBooth(){}
    public BazaarBooth(List<BoothSlotNumber> bazaarBoothList, int success) {
        this.bazaarBoothList = bazaarBoothList;
        this.success = success;
    }

    public List<BoothSlotNumber> getBazaarBoothList() {
        return bazaarBoothList;
    }

    public void setBazaarBoothList(List<BoothSlotNumber> bazaarBoothList) {
        this.bazaarBoothList = bazaarBoothList;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int count_available_slot(){
        return bazaarBoothList.size();
    }

}
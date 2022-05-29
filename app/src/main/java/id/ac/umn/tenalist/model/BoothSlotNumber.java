package id.ac.umn.tenalist.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BoothSlotNumber {
    @SerializedName("bazaarBoothId")
    private int bazaarBoothId;

    @SerializedName("bazaarId")
    private int bazaarId;

    @SerializedName("bazaarBoothNumber")
    private int bazaarBoothNumber;

    public BoothSlotNumber(){}
    public BoothSlotNumber(int bazaarBoothId, int bazaarId, int bazaarBoothNumber) {
        this.bazaarBoothId = bazaarBoothId;
        this.bazaarId = bazaarId;
        this.bazaarBoothNumber = bazaarBoothNumber;
    }

    public int getBazaarBoothId() {
        return bazaarBoothId;
    }

    public void setBazaarBoothId(int bazaarBoothId) {
        this.bazaarBoothId = bazaarBoothId;
    }

    public int getBazaarId() {
        return bazaarId;
    }

    public void setBazaarId(int bazaarId) {
        this.bazaarId = bazaarId;
    }

    public int getBazaarBoothNumber() {
        return bazaarBoothNumber;
    }

    public void setBazaarBoothNumber(int bazaarBoothNumber) {
        this.bazaarBoothNumber = bazaarBoothNumber;
    }
}
package id.ac.umn.tenalist.model;

import com.google.gson.annotations.SerializedName;

public class InsertResponse {
    @SerializedName("id")
    private int id=0;
    @SerializedName("status")
    private String status;
    @SerializedName("upload")
    private String upload="";

    public InsertResponse(int id, String status) {
        this.id = id;
        this.status = status;
    }

    public InsertResponse(String upload, String status) {
        this.upload = upload;
        this.status = status;
    }

    public InsertResponse(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpload() {
        return upload;
    }

    public void setUpload(String upload) {
        this.upload = upload;
    }
}

package id.ac.umn.tenalist.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class User implements Parcelable {
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    @SerializedName("userEmail")
    private String email;
    @SerializedName("userFullName")
    private String name;
    @SerializedName("userRole")
    private String role;
    @SerializedName("userPhoneNumber")
    private String phone;
    @SerializedName("userId")
    private int id;
    @SerializedName("userPicture")
    private String photo = null;
    @SerializedName("userCount")
    private int count_bazaar = 0;
    @SerializedName("userVerification")
    private int ver;
    @SerializedName("rating")
    private float rating = 0;
    // Firebase User Id
    private String uid;

    public User() {
    }

    public User(String email, String name, String role, String phone, int id, String photo, int count_bazaar, int ver, float rating) {
        this.email = email;
        this.name = name;
        this.role = role;
        this.phone = phone;
        this.id = id;
        this.photo = photo;
        this.count_bazaar = count_bazaar;
        this.ver = ver;
        this.rating = rating;
    }

    public User(String name, String role, int id, String photo, int count_bazaar, float rating) {
        this.name = name;
        this.role = role;
        this.id = id;
        this.photo = photo;
        this.count_bazaar = count_bazaar;
        this.rating = rating;
    }

    public User(String email, String name, String role, String phone, int id, String photo, int count_bazaar, int ver) {
        this.email = email;
        this.name = name;
        this.role = role;
        this.phone = phone;
        this.id = id;
        this.count_bazaar = count_bazaar;
        this.ver = ver;
    }

    public User(String email, String name, String role, String phone, int id, int count_bazaar, int ver) {
        this.email = email;
        this.name = name;
        this.role = role;
        this.phone = phone;
        this.id = id;
        this.count_bazaar = count_bazaar;
        this.ver = ver;
    }


    // Object for Firebase Database
    public User(String uid, String name, String photo, String role, String id) {
        this.uid = uid;
        this.name = name;
        this.photo = photo;
        this.role = role;
        this.id = Integer.parseInt(id);
    }

    protected User(Parcel in) {
        this.email = in.readString();
        this.name = in.readString();
        this.role = in.readString();
        this.phone = in.readString();
        this.id = in.readInt();
        this.photo = in.readString();
        this.count_bazaar = in.readInt();
        this.ver = in.readInt();
        this.rating = in.readFloat();
        this.uid = in.readString();
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public boolean isVerified() {
        return ver == 1;
    }

    public int getVer() {
        return ver;
    }

    public void setVer(int ver) {
        this.ver = ver;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getCount_bazaar() {
        return count_bazaar;
    }

    public void setCount_bazaar(int count_bazaar) {
        this.count_bazaar = count_bazaar;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.email);
        dest.writeString(this.name);
        dest.writeString(this.role);
        dest.writeString(this.phone);
        dest.writeInt(this.id);
        dest.writeString(this.photo);
        dest.writeInt(this.count_bazaar);
        dest.writeInt(this.ver);
        dest.writeFloat(this.rating);
        dest.writeString(this.uid);
    }
}

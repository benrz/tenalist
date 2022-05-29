package id.ac.umn.tenalist;

import java.text.SimpleDateFormat;

import id.ac.umn.tenalist.model.User;

public class UserData {
    public static User user= new User();
    public static int picCount=0;
    public static String dateFormat = "MMM d, yyyy";
    public static SimpleDateFormat formattedDate = new SimpleDateFormat(dateFormat);
    public static String timeFormat = "H:mm";
    public static SimpleDateFormat formattedTime = new SimpleDateFormat(timeFormat);
}

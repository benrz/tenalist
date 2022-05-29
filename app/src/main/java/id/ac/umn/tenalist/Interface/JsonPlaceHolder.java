package id.ac.umn.tenalist.Interface;

import com.google.gson.JsonObject;

import id.ac.umn.tenalist.BazaarBooth;
import id.ac.umn.tenalist.TransactionData;
import id.ac.umn.tenalist.model.Bazaars;


import java.util.Date;
import java.util.List;

import id.ac.umn.tenalist.model.BazaarDetail;
import id.ac.umn.tenalist.model.BazaarTransaction;
import id.ac.umn.tenalist.model.BazaarsCategory;
import id.ac.umn.tenalist.model.EventListModel;
import id.ac.umn.tenalist.model.InsertResponse;
import id.ac.umn.tenalist.model.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface JsonPlaceHolder {
    @GET("read_all_bazaar.php")
    Call<Bazaars> getBazaar();

    @GET("popular_bazaar.php")
    Call<Bazaars> getPopularBazaar();

    @GET("read_all_category.php")
    Call<BazaarsCategory> getBazaarCategory();

    @FormUrlEncoded
    @POST("get_user_detail.php")
    Call<User> getUser(
            @Field("email") String email);

    @FormUrlEncoded
    @POST("add_user.php")
    Call<InsertResponse> addUser(
            @Field("email") String email,
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("role") String role);

    @FormUrlEncoded
    @POST("edit_user.php")
    Call<InsertResponse> editUser(
            @Field("id") int id,
            @Field("email") String email,
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("verification") int verify);

    @FormUrlEncoded
    @POST("create_bazaar.php")
    Call<Bazaars> createBazaar(
            @Field("organizerId") Integer organizerId,
            @Field("bazaarName") String bazaarName,
            @Field("bazaarDescription") String bazaarDescription,
            @Field("bazaarStartDate") String bazaarStartDate,
            @Field("bazaarEndDate") String bazaarEndDate,
            @Field("bazaarStartTime") String bazaarStartTime,
            @Field("bazaarEndTime") String bazaarEndTime,
            @Field("bazaarLocation") String bazaarLocation,
            @Field("bazaarTotalBooth") int bazaarTotalBooth,
            @Field("bazaarPrice") int bazaarPrice,
            @Field("bazaarPoster") String bazaarPoster,
            @Field("bazaarFloorPlan") String bazaarFloorPlan,
            @Field("bazaarMOU") String bazaarMOU,
            @Field("bazaarCity") String bazaarCity,
            @Field("bazaarValidation") int bazaarValidation,
            @Field("bazaarStatusId") int bazaarStatusId,
            @Field("bazaarCategoryId") int bazaarCategoryId
            );

    @FormUrlEncoded
    @POST("update_bazaar.php")
    Call<Bazaars> updateBazaar(
            @Field("bazaarId") int bazaarId,
            @Field("bazaarName") String bazaarName,
            @Field("bazaarDescription") String bazaarDescription,
            @Field("bazaarStartDate") String bazaarStartDate,
            @Field("bazaarEndDate") String bazaarEndDate,
            @Field("bazaarStartTime") String bazaarStartTime,
            @Field("bazaarEndTime") String bazaarEndTime,
            @Field("bazaarLocation") String bazaarLocation,
            @Field("bazaarTotalBooth") int bazaarTotalBooth,
            @Field("bazaarPrice") int bazaarPrice,
            @Field("bazaarPoster") String bazaarPoster,
            @Field("bazaarFloorPlan") String bazaarFloorPlan,
            @Field("bazaarMOU") String bazaarMOU,
            @Field("bazaarCity") String bazaarCity,
            @Field("bazaarValidation") int bazaarValidation,
            @Field("bazaarStatusId") int bazaarStatusId,
            @Field("bazaarCategoryId") int bazaarCategoryId);

    @FormUrlEncoded
    @POST("delete_bazaar.php")
    Call<Bazaars> deleteBazaar();

    @FormUrlEncoded
    @POST("create_transaction_bazaar.php") // insert into bazaartransaction and update bazaarbooth in 1 php
    Call<BazaarTransaction> createBookTransaction(
            @Field("bazaarId") int bazaarId,
            @Field("bazaarBoothId") int bazaarBoothId,
            @Field("tenantId") int tenantId,
            @Field("organizerId") int organizerId,
            @Field("totalPaid") int totalPaid,
            @Field("paymentStatus") int paymentStatus
    );

    @GET("get_bazaar_detail.php")
    Call<BazaarDetail> getBazaarDetail(
            @Query("bazaarId") int bazaarId
    );

    @GET("get_available_booth.php")
    Call<BazaarBooth> getBazaarBooth(
            @Query("bazaarId") int bazaarId
    );

    @GET("get_current_transaction.php")
    Call<TransactionData> getCurrentTransaction(
            @Query("bazaarBoothId") int bazaarBoothId,
            @Query("paymentStatus") int paymentStatus
    );

    @GET("update_transaction.php")
    Call<BazaarTransaction> updateTransaction(
            @Query("bazaarTransactionId") int bazaarTransactionId,
            @Query("paymentStatus") int paymentStatus,
            @Query("accountNum") String accountNum,
            @Query("accountName") String accountName,
            @Query("bazaarBoothId") int bazaarBoothId
    );

    @GET("cancel_transaction.php")
    Call<BazaarTransaction> cancelTransaction(
            @Query("bazaarTransactionId") int bazaarTransactionId,
            @Query("bazaarBoothId") int bazaarBoothId
    );

    @FormUrlEncoded
    @POST("edit_user.php")
    Call<InsertResponse> editUserImage(
            @Field("id") int id,
            @Field("email") String email,
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("verification") int verify,
            @Field("picture") String picture);

    @FormUrlEncoded
    @POST("get_user_events.php")
    Call<List<EventListModel>> getUserEvents(
            @Field("id") int id);

}

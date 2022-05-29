package id.ac.umn.tenalist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

import id.ac.umn.tenalist.ApiConfig;
import id.ac.umn.tenalist.BazaarBooth;
import id.ac.umn.tenalist.Interface.JsonPlaceHolder;
import id.ac.umn.tenalist.R;
import id.ac.umn.tenalist.UserData;
import id.ac.umn.tenalist.model.BazaarDetail;
import id.ac.umn.tenalist.model.EventListModel;
import id.ac.umn.tenalist.model.SharedPreference;
import id.ac.umn.tenalist.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.ac.umn.tenalist.UserData.formattedDate;
import static id.ac.umn.tenalist.UserData.formattedTime;

public class DetailActivity extends AppCompatActivity {
    public int bazaarId;
    public String CurrUserRole;
    Button btnCountMeIn;
    SharedPreference sharedPreference;
    private JsonPlaceHolder jsonPlaceHolder;
    private ImageButton btnChat;
    private ImageView ivIsVerified, ivBazaarPoster, ivOrganizer, btnFavorites;
    private TextView tvEventName, tvRate, tvStatus, tvAddress, tvDate, tvTime, tvOrganizer, tvDesc, tvOrganizerName, tvTotalBazaar;
    private LinearLayout LLOrganizer;
    private User organizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle("Event Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CurrUserRole = UserData.user.getRole();

        LLOrganizer = findViewById(R.id.LLOrganizer);
        tvEventName = findViewById(R.id.tvEventName);
        tvRate = findViewById(R.id.tvRate);
        tvStatus = findViewById(R.id.tvStatus);
        tvDate = findViewById(R.id.tvDate);
        tvAddress = findViewById(R.id.tvAddress);
        tvTime = findViewById(R.id.tvTime);
        tvOrganizer = findViewById(R.id.tvOrganizer);
        tvDesc = findViewById(R.id.tvDesc);
        tvOrganizerName = findViewById(R.id.tvOrganizerName);
        tvTotalBazaar = findViewById(R.id.tvTotalBazaar);
        ivBazaarPoster = findViewById(R.id.ivBazaar);
        ivIsVerified = findViewById(R.id.ivVerified);
        ivOrganizer = findViewById(R.id.ivOrganizer);
        btnCountMeIn = findViewById(R.id.btnCountMeIn);
        btnCountMeIn.setEnabled(false);
        btnFavorites = findViewById(R.id.btn_favorite);

        Intent i = getIntent();
        bazaarId = i.getIntExtra("bazaarId", 1);

        LLOrganizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (organizer != null) {
                    Intent profileIntent = new Intent(DetailActivity.this, ProfileActivity.class);
                    profileIntent.putExtra(ProfileActivity.EXT_USER, organizer);
                    startActivity(profileIntent);
                }
            }
        });

        btnChat = findViewById(R.id.btnChat);
        btnChat.setEnabled(false);

        // Get bazaar detail
        jsonPlaceHolder = ApiConfig.getApiConfig();
        Call<BazaarDetail> call = jsonPlaceHolder.getBazaarDetail(bazaarId);
        call.enqueue(new Callback<BazaarDetail>() {
            @Override
            public void onResponse(Call<BazaarDetail> call, final Response<BazaarDetail> response) {
//                Log.e("ERROR - suc", response.body().toString());
                if (!response.isSuccessful()) return;
                else if (response.body().success == 1) {
                    int eventId = response.body().getBazaarDetail().getBazaarId();
                    String eventName = response.body().getBazaarDetail().getBazaarName();
                    float rating = response.body().getBazaarDetail().getOrganizerRating();
                    String organizerRate = String.valueOf(rating);

                    final String StartDate = response.body().getBazaarDetail().getBazaarStartDate();
                    final String EndDate = response.body().getBazaarDetail().getBazaarEndDate();
                    String Address = response.body().getBazaarDetail().getBazaarLocation();
                    String StartTime = response.body().getBazaarDetail().getBazaarStartTime();
                    String EndTime = response.body().getBazaarDetail().getBazaarEndTime();
                    String Organizer = response.body().getBazaarDetail().getUserFullName();
                    String EventDesc = response.body().getBazaarDetail().getBazaarDescription();
                    String category = response.body().getBazaarDetail().getBazaarCategoryName();
                    String posterName = response.body().getBazaarDetail().getBazaarPoster();
                    final String floorPlan = response.body().getBazaarDetail().getBazaarFloorPlan();
                    int bazaarValidation = response.body().getBazaarDetail().getBazaarValidation();
                    final int bazaarStatusId = response.body().getBazaarDetail().getBazaarStatusId();
                    final int bazaarPrice = response.body().getBazaarDetail().getBazaarPrice();
                    final int OrganizerId = response.body().getBazaarDetail().getOrganizerId();
                    int totalBazaarOrganizer = response.body().getBazaarDetail().getTotalbazaar();
                    String organizerPicture = response.body().getBazaarDetail().getUserPicture();
//                    String OrganizerEmail = response.body().getBazaarDetail().getOrganizerEmail();

                    organizer = new User(Organizer, "organizer", OrganizerId, organizerPicture, totalBazaarOrganizer, rating);
                    btnChat.setEnabled(true);

                    DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("users");
                    usersReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                User user1 = snapshot.getValue(User.class);

//                    assert user1 != null;
                                if (user1 == null) {
                                    break;
                                }
                                Log.e("error", String.valueOf(user1));
                                if (user1.getId() == organizer.getId()) {
                                    organizer.setUid(user1.getUid());
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    String[] arrStartDate = StartDate.split("-", 5);
                    int year1 = Integer.parseInt(arrStartDate[0]);
                    int month1 = Integer.parseInt(arrStartDate[1]);
                    int day1 = Integer.parseInt(arrStartDate[2]);
                    String[] arrEndDate = EndDate.split("-", 5);
                    int year2 = Integer.parseInt(arrEndDate[0]);
                    int month2 = Integer.parseInt(arrEndDate[1]);
                    int day2 = Integer.parseInt(arrEndDate[2]);
                    String formatStartDate = formattedDate.format(new Date(year1-1900, month1, day1));
                    String formatEndDate = formattedDate.format(new Date(year2-1900, month2, day2));

                    String formatSTime = StartTime.substring(0,5);
                    String formatETime = EndTime.substring(0,5);

                    tvEventName.setText(category + " event - " + eventName);
                    tvRate.setText(organizerRate + "/5.0");
                    tvDate.setText(formatStartDate + " - " + formatEndDate);
                    tvAddress.setText(Address);
                    tvTime.setText(formatSTime + " - " + formatETime);
                    tvOrganizer.setText(Organizer);
                    tvDesc.setText(EventDesc);
                    tvOrganizerName.setText(Organizer);
                    tvTotalBazaar.setText(totalBazaarOrganizer + " bazaar(s) held");

                    Glide.with(getApplicationContext()).load(ApiConfig.FINAL_URL + "poster/" + posterName).into(ivBazaarPoster);
                    Glide.with(getApplicationContext()).load(ApiConfig.FINAL_URL + "profile/" + organizerPicture).into(ivOrganizer);

                    // Call Bazaar Booth
                    Call<BazaarBooth> slotCall = jsonPlaceHolder.getBazaarBooth(bazaarId);
                    slotCall.enqueue(new Callback<BazaarBooth>() {
                        @Override
                        public void onResponse(Call<BazaarBooth> call, Response<BazaarBooth> response) {
                            //                Log.e("ERROR - suc", response.body().toString());
                            if (response.body().success == 1) {
                                int slot = response.body().count_available_slot();
                                if (CurrUserRole.equals("tenant")) btnChat.setEnabled(true);
                                else {
                                    btnChat.setEnabled(false);
                                }
                                if(slot==0) tvStatus.setText(getResources().getString(R.string.zero_slot));
                                tvStatus.setText(getResources().getQuantityString(R.plurals.numberOfSlots, slot, slot));
                                if (slot > 0) {
                                    tvStatus.setBackgroundColor(getResources().getColor(R.color.colorSuccess));
                                    if (CurrUserRole.equals("tenant")) {
                                        if (bazaarStatusId == 1)
                                            btnCountMeIn.setEnabled(true);
                                        else
                                            btnCountMeIn.setEnabled(false);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<BazaarBooth> call, Throwable t) {
                            Log.e("Error", "unsuccess");
                            Toast.makeText(DetailActivity.this, getString(R.string.connection_problem), Toast.LENGTH_LONG).show();
                        }
                    });

//                    if(bazaarValidation == 0)
                    ivIsVerified.setImageDrawable(null);
//                    else Glide.with(getApplicationContext()).load(ApiConfig.FINAL_URL + "icon/verified.png").into(ivIsVerified);
                    btnCountMeIn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(DetailActivity.this, BookActivity.class);
                            intent.putExtra("BazaarId", bazaarId);
                            intent.putExtra("StartDate", StartDate);
                            intent.putExtra("EndDate", EndDate);
                            intent.putExtra("Price", bazaarPrice);
                            intent.putExtra("OrganizerId", OrganizerId);
                            intent.putExtra("floorPlan", floorPlan);
                            startActivity(intent);
                        }
                    });

                    Date formattedStartDate = null;
                    Date formattedEndDate = null;
                    try {
                        formattedStartDate = new SimpleDateFormat("yyyy-mm-dd").parse(StartDate);
                        formattedEndDate = new SimpleDateFormat("yyyy-mm-dd").parse(EndDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    String formattedSTime = StartTime.substring(0,5);
                    String formattedETime = EndTime.substring(0,5);

                    //final BazaarDetail.bazaarDetail bazaarDetail = response.body().getBazaarDetail();
                    //int eventId, String eventTitle, String eventPlace, Date eventStartDate, Date eventEndDate, String eventStartTime, String eventEndTime, int eventPrice, String eventStatus, String eventImage, String eventCategory
                    final EventListModel mEvent = new EventListModel(eventId, eventName, Address, formattedStartDate, formattedEndDate, formattedSTime, formattedETime, bazaarPrice, -1, "none", posterName, category);
                    if (checkFavoriteItem(mEvent)) {
                        btnFavorites.setImageResource(R.drawable.heart_red);
                        btnFavorites.setTag("red");
                    } else {
                        btnFavorites.setImageResource(R.drawable.heart_grey);
                        btnFavorites.setTag("grey");
                    }

                    btnFavorites.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String tag = btnFavorites.getTag().toString();
                            if (tag.equalsIgnoreCase("grey")) {
                                sharedPreference.addFavorite(getApplicationContext(), mEvent);
                                Toast.makeText(getApplicationContext(),
                                        "Add to Favorites",
                                        Toast.LENGTH_SHORT).show();

                                btnFavorites.setTag("red");
                                btnFavorites.setImageResource(R.drawable.heart_red);
                            } else {
                                sharedPreference.removeFavorite(getApplicationContext(), mEvent);
                                btnFavorites.setTag("grey");
                                btnFavorites.setImageResource(R.drawable.heart_grey);
                                Toast.makeText(getApplicationContext(),
                                        "Remove from Favorites",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Log.e("Error", "unsuccess");
                    Toast.makeText(DetailActivity.this, getString(R.string.error_something_wrong), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BazaarDetail> call, Throwable t) {
                Log.e("ERROR - Failure", t.toString());
                Toast.makeText(DetailActivity.this, getString(R.string.connection_problem), Toast.LENGTH_LONG).show();
            }
        });

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UserID = organizer.getUid();
                Toast.makeText(getApplicationContext(), UserID, Toast.LENGTH_LONG).show();
                Intent messageIntent = new Intent(DetailActivity.this, MessageActivity.class);
                messageIntent.putExtra("userId", UserID);
                startActivity(messageIntent);
            }
        });

    }

//    public boolean checkFavoriteItem(BazaarDetail.bazaarDetail bazaarDetail) {
//        boolean check = false;
//        sharedPreference = new SharedPreference();
//        List<BazaarDetail.bazaarDetail> favorites = sharedPreference.getFavorites(getApplicationContext());
//        if (favorites != null) {
//            for (BazaarDetail.bazaarDetail bazaar : favorites) {
//                if (bazaar.getBazaarName().equals(bazaarDetail.getBazaarName())) {
//                    check = true;
//                    break;
//                }
//            }
//        }
//        return check;
//    }

    public boolean checkFavoriteItem(EventListModel mEvent) {
        boolean check = false;
        sharedPreference = new SharedPreference();
        List<EventListModel> favorites = sharedPreference.getFavorites(getApplicationContext());
        if (favorites != null) {
            for (EventListModel favorite : favorites) {
                if (favorite.getEventId() == mEvent.getEventId()) {
                    check = true;
                    break;
                }
            }
        }
        return check;
    }
}

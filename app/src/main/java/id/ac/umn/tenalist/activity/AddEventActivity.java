package id.ac.umn.tenalist.activity;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.util.TimeZone;
import android.net.Uri;
import android.os.Bundle;
import android.icu.util.Calendar;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.ac.umn.tenalist.ApiConfig;
import id.ac.umn.tenalist.Interface.FragmentActionListener;
import id.ac.umn.tenalist.Interface.JsonPlaceHolder;
import id.ac.umn.tenalist.R;
import id.ac.umn.tenalist.UserData;
import id.ac.umn.tenalist.fragment.SublimePickerFragment;
import id.ac.umn.tenalist.fragment.TimePickerFragment;
import id.ac.umn.tenalist.model.BazaarDetail;
import id.ac.umn.tenalist.model.Bazaars;
import id.ac.umn.tenalist.model.BazaarsCategory;
import id.ac.umn.tenalist.model.BoothSlotNumber;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.ac.umn.tenalist.activity.EditProfileActivity.REQ_CODE_CAMERA_PROFILE;
import static id.ac.umn.tenalist.activity.EditProfileActivity.REQ_CODE_CAMERA_VERIFY;
import static id.ac.umn.tenalist.activity.EditProfileActivity.REQ_CODE_GALLERY_PROFILE;
import static id.ac.umn.tenalist.activity.EditProfileActivity.REQ_CODE_GALLERY_VERIFY;

public class AddEventActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, AdapterView.OnItemSelectedListener {

    Integer event_Price;
    Integer bazaarStatusId = 1;
    Integer organizerId;
    Integer bazaarCategoryId;
    Integer value;
    String mDateStart;
    String mDateEnd;
    String eventStartDate;
    String eventEndDate;
    String posterName;
    String floorPlanName;
    Date dateEventStartDate;
    Date dateEventEndDate;
    Boolean mTimeStart = false;
    Boolean mTimeEnd = false;
    Boolean imgeventPoster = false;
    Boolean imgeventFloorPlan = false;
    Boolean uploadEventPoster = false;
    Boolean uploadEventFloorPlan = false;
    String eventCategory;
    String eventStartTime;
    String eventEndTime;
    int bazaarId;
    private ArrayList<String> categoryID;
    private ArrayList<String> categoryName;
    private Bitmap bitmap_eventPoster = null;
    private Bitmap bitmap_eventFloorPlan = null;
    JsonPlaceHolder jsonPlaceHolder;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private ImageView img_eventPoster;
    private ImageView img_eventFloorPlan;
    private TextInputLayout til_eventName;
    private TextInputLayout til_eventDescription;
    private TextInputLayout til_eventTotalBooth;
    private TextInputLayout til_eventLocation;
    private TextInputLayout til_eventCity;
    private TextInputLayout til_eventPrice;
    private TextInputEditText edt_eventName;
    private TextInputEditText edt_eventDescription;
    private TextInputEditText edt_eventTotalBooth;
    private TextInputEditText edt_eventLocation;
    private TextInputEditText edt_eventCity;
    private TextInputEditText edt_eventPrice;
    private TextView tv_eventMOU;
    private Button btn_eventStartDate;
    private Button btn_eventEndDate;
    private Button btn_eventStartTime;
    private Button btn_eventEndTime;
    private Button btn_boothIncrement;
    private Button btn_boothDecrement;
    private Button btn_eventPoster;
    private Button btn_eventMOU;
    private Button btn_eventFloorPlan;
    private Button btn_eventAdd;
    private Button btn_eventCancel;
    private Spinner spn_eventCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        img_eventFloorPlan = findViewById(R.id.img_eventFloorPlan);
        img_eventPoster = findViewById(R.id.img_eventPoster);
        tv_eventMOU = findViewById(R.id.tv_eventMOU);
        til_eventTotalBooth = findViewById(R.id.til_eventTotalBooth);
        til_eventName = findViewById(R.id.til_eventName);
        til_eventDescription = findViewById(R.id.til_eventDescription);
        til_eventLocation = findViewById(R.id.til_eventLocation);
        til_eventCity = findViewById(R.id.til_eventCity);
        til_eventPrice = findViewById(R.id.til_eventPrice);
        edt_eventName = findViewById(R.id.edt_eventName);
        edt_eventDescription = findViewById(R.id.edt_eventDescription);
        edt_eventTotalBooth = findViewById(R.id.edt_eventTotalBooth);
        edt_eventLocation = findViewById(R.id.edt_eventLocation);
        edt_eventCity = findViewById(R.id.edt_eventCity);
        edt_eventPrice = findViewById(R.id.edt_eventPrice);
        btn_boothIncrement = findViewById(R.id.btn_boothIncrement);
        btn_boothDecrement = findViewById(R.id.btn_boothDecrement);
        btn_eventPoster = findViewById(R.id.btn_eventPoster);
        btn_eventFloorPlan = findViewById(R.id.btn_eventFloorPlan);
        btn_eventAdd = findViewById(R.id.btn_eventAdd);
        btn_eventStartDate = findViewById(R.id.btn_eventStartDate);
        btn_eventEndDate = findViewById(R.id.btn_eventEndDate);
        btn_eventStartTime = findViewById(R.id.btn_eventStartTime);
        btn_eventEndTime = findViewById(R.id.btn_eventEndTime);
        btn_eventCancel = findViewById(R.id.btn_eventCancel);
        spn_eventCategory = findViewById(R.id.spn_eventCategory);

        final Intent i = getIntent();
        jsonPlaceHolder = ApiConfig.getApiConfig();

        if (i.getStringExtra("MODE").equals("UPDATE")) {
            btn_eventAdd.setText("UPDATE EVENT");
            bazaarId = i.getIntExtra("bazaarId", 1);
            Log.d("UPDATE", String.valueOf(bazaarId));
            Call<BazaarDetail> call = jsonPlaceHolder.getBazaarDetail(bazaarId);
            call.enqueue(new Callback<BazaarDetail>() {
                @Override
                public void onResponse(Call<BazaarDetail> call, Response<BazaarDetail> response) {
                    Log.d("RESPONSE MESSAGE", response.message());
                    Log.d("RESPONSE SUCCESSFUL", String.valueOf(response.isSuccessful()));
                    Log.d("MESSAGE3", response.raw().toString());
                    Log.d("MESSAGE4", response.toString());
                    if(response.isSuccessful() && response.body() != null) {
                        Log.d("MESSAGE5", response.body().toString());
                        BazaarDetail.bazaarDetail bazaarDetail = response.body().getBazaarDetail();
                        edt_eventName.setText(bazaarDetail.getBazaarName());
                        edt_eventDescription.setText(bazaarDetail.getBazaarDescription());
                        btn_eventStartDate.setText(bazaarDetail.getBazaarStartDate());
                        btn_eventEndDate.setText(bazaarDetail.getBazaarEndDate());
                        btn_eventStartTime.setText(bazaarDetail.getBazaarStartTime());
                        btn_eventEndTime.setText(bazaarDetail.getBazaarEndTime());
                        edt_eventLocation.setText(bazaarDetail.getBazaarLocation());
                        edt_eventCity.setText(bazaarDetail.getBazaarCity());
                        edt_eventTotalBooth.setText(String.valueOf(bazaarDetail.getBazaarTotalBooth()));
                        edt_eventPrice.setText(String.valueOf(bazaarDetail.getBazaarPrice()));

                        String selectedCategory = bazaarDetail.getBazaarCategoryName();
                        spn_eventCategory.setSelection(getIndex(spn_eventCategory, selectedCategory));

                        posterName = bazaarDetail.getBazaarPoster();
                        if(posterName != null){
                            img_eventPoster.setVisibility(View.VISIBLE);
                            Glide.with(getApplicationContext()).load(ApiConfig.FINAL_URL + "poster/" + posterName).into(img_eventPoster);
                        }

                        floorPlanName = bazaarDetail.getBazaarFloorPlan();
                        if(floorPlanName != null){
                            img_eventFloorPlan.setVisibility(View.VISIBLE);
                            Glide.with(getApplicationContext()).load(ApiConfig.FINAL_URL + "floor-plan/" + floorPlanName).into(img_eventFloorPlan);
                        }

                        eventStartDate = bazaarDetail.getBazaarStartDate();
                        eventEndDate = bazaarDetail.getBazaarEndDate();
                        bazaarStatusId = bazaarDetail.getBazaarStatusId();
                    }
                }

                @Override
                public void onFailure(Call<BazaarDetail> call, Throwable t) {
                    Log.e("ERROR", t.getMessage());
                }
            });
        } else {
            btn_eventCancel.setVisibility(View.GONE);
        }

        // GET ALL CATEGORY
        Call<BazaarsCategory> bazaarCategoryCall = jsonPlaceHolder.getBazaarCategory();
        bazaarCategoryCall.enqueue(new Callback<BazaarsCategory>() {
            @Override
            public void onResponse(Call<BazaarsCategory> call, Response<BazaarsCategory> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                List<BazaarsCategory.BazaarCategory> bazaarCategoryList = response.body().bazaarCategory;
                categoryID = new ArrayList<String>();
                categoryName = new ArrayList<String>();
                for(int i = 0; i < bazaarCategoryList.size(); i++){
                    categoryID.add(String.valueOf(bazaarCategoryList.get(i).getBazaarCategoryId()));
                    categoryName.add(String.valueOf(bazaarCategoryList.get(i).getBazaarCategoryName()));
                }
                fetchSpinnerCategory(categoryName, categoryID);
            }

            @Override
            public void onFailure(Call<BazaarsCategory> call, Throwable t) {
                Log.d("onFailure", t.getMessage());
            }
        });

        final SublimePickerFragment pickerFrag = new SublimePickerFragment();

        // tombol untuk memilih tanggal event dimulai dan berakhirnya event
        btn_eventStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerFrag.setCallback(new SublimePickerFragment.Callback() {
                    @Override
                    public void onCancelled() {
                        Toast.makeText(AddEventActivity.this, "User cancel",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDateTimeRecurrenceSet(final SelectedDate selectedDate, int hourOfDay, int minute,
                                                        SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
                                                        String recurrenceRule) {

                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat formatDate = new SimpleDateFormat("dd MMMM yyyy");
                        mDateStart = formatDate.format(selectedDate.getStartDate().getTime());
                        mDateEnd = formatDate.format(selectedDate.getEndDate().getTime());

                        btn_eventStartDate.setText(mDateStart);
                        btn_eventEndDate.setText(mDateEnd);

                        formatDate = new SimpleDateFormat("yyyy-MM-dd");
                        eventStartDate = formatDate.format(selectedDate.getStartDate().getTime());
                        eventEndDate = formatDate.format(selectedDate.getEndDate().getTime());

                        try {
                            dateEventStartDate = formatDate.parse(eventStartDate);
                            dateEventEndDate = formatDate.parse(eventEndDate);

                            // mengimput status bazaar (1 = coming soon, 2 = on progress)
                            formatDate = new SimpleDateFormat("dd/MM/yyyy");
                            Date strDate = formatDate.parse(eventStartDate);
                            if (System.currentTimeMillis() < strDate.getTime()) {
                                bazaarStatusId = 1;
                            }
                            else{
                                bazaarStatusId = 2;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Log.d("eventStartDate", String.valueOf(selectedDate.getStartDate().getTime()));
                        Log.d("eventEndDate", String.valueOf(selectedDate.getEndDate().getTime()));
                    }
                });
                SublimeOptions options = new SublimeOptions();
                options.setCanPickDateRange(true);
                options.setDisplayOptions(SublimeOptions.ACTIVATE_DATE_PICKER);
                options.setPickerToShow(SublimeOptions.Picker.DATE_PICKER);

                Bundle bundle = new Bundle();
                bundle.putParcelable("SUBLIME_OPTIONS", options);
                pickerFrag.setArguments(bundle);

                pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                pickerFrag.show(getSupportFragmentManager(), "SUBLIME_PICKER");
            }
        });

        // tombol untuk memilih tanggal event dimulai dan berakhirnya event
        btn_eventEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerFrag.setCallback(new SublimePickerFragment.Callback() {
                    @Override
                    public void onCancelled() {
                        Toast.makeText(AddEventActivity.this, "User cancel",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDateTimeRecurrenceSet(final SelectedDate selectedDate, int hourOfDay, int minute,
                                                        SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
                                                        String recurrenceRule) {
                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat formatDate = new SimpleDateFormat("dd MMMM yyyy");
                        mDateStart = formatDate.format(selectedDate.getStartDate().getTime());
                        mDateEnd = formatDate.format(selectedDate.getEndDate().getTime());

                        btn_eventStartDate.setText(mDateStart);
                        btn_eventEndDate.setText(mDateEnd);

                        formatDate = new SimpleDateFormat("yyyy-MM-dd");
                        eventStartDate = formatDate.format(selectedDate.getStartDate().getTime());
                        eventEndDate = formatDate.format(selectedDate.getEndDate().getTime());

                        try {
                            dateEventStartDate = formatDate.parse(eventStartDate);
                            dateEventEndDate = formatDate.parse(eventEndDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        btn_eventStartDate.setText(eventStartDate);
                        btn_eventEndDate.setText(eventEndDate);
                        btn_eventStartTime.setText(eventStartTime);
                        btn_eventEndTime.setText(eventEndTime);
                    }
                });
                SublimeOptions options = new SublimeOptions();
                options.setCanPickDateRange(true);
                options.setDisplayOptions(SublimeOptions.ACTIVATE_DATE_PICKER);
                options.setPickerToShow(SublimeOptions.Picker.DATE_PICKER);

                Bundle bundle = new Bundle();
                bundle.putParcelable("SUBLIME_OPTIONS", options);
                pickerFrag.setArguments(bundle);

                pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                pickerFrag.show(getSupportFragmentManager(), "SUBLIME_PICKER");
            }
        });

        // tombol untuk menentukan waktu dimulainya event
        btn_eventStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimeEnd = false;
                mTimeStart = true;
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        // tombol untuk menentukan waktu berakhirnya event
        btn_eventEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimeStart = false;
                mTimeEnd = true;
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        // tombol untuk menambahkan total slot bazaar
        btn_boothIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value = Integer.parseInt(edt_eventTotalBooth.getText().toString());
                value++;
                edt_eventTotalBooth.setText("" + value);
            }
        });

        // tombol untuk menurunkan total slot bazaar
        btn_boothDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value = Integer.parseInt(edt_eventTotalBooth.getText().toString());
                if (value > 1) {
                    value--;
                    edt_eventTotalBooth.setText("" + value);
                }
            }
        });

        btn_eventPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // megatur trigger agar gambar yang dipilih nantinya akan tersimpan sesuai dengan tombol ini
                imgeventPoster = true;
                imgeventFloorPlan = false;
                uploadEventPoster = true;
                selectImage();
            }
        });

        btn_eventFloorPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // megatur trigger agar gambar yang dipilih nantinya akan tersimpan sesuai dengan tombol ini
                imgeventPoster = false;
                imgeventFloorPlan = true;
                uploadEventFloorPlan = true;
                selectImage();
            }
        });

        // tombol untuk menambahkan event baru
        btn_eventAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm:ss");

                final String eventName = edt_eventName.getText().toString();
                final String eventDescription = edt_eventDescription.getText().toString();
                final String eventStartTime = btn_eventStartTime.getText().toString();
                final String eventEndTime = btn_eventEndTime.getText().toString();
                final String eventLocation = edt_eventLocation.getText().toString();
                final String eventCity = edt_eventCity.getText().toString();
                final String eventPrice = edt_eventPrice.getText().toString();
                final int eventTotalBooth = Integer.parseInt(edt_eventTotalBooth.getText().toString());

                if (eventName.isEmpty()) til_eventName.setError("Please insert event name");
                else til_eventName.setErrorEnabled(false);

                if (eventDescription.isEmpty())
                    til_eventDescription.setError("Please insert event description");
                else til_eventDescription.setErrorEnabled(false);

                if (eventLocation.isEmpty())
                    til_eventLocation.setError("Please insert event Location");
                else til_eventLocation.setErrorEnabled(false);

                if (eventCity.isEmpty())
                    til_eventCity.setError("Please insert event City");
                else til_eventCity.setErrorEnabled(false);

                if (eventPrice.isEmpty()) til_eventPrice.setError("Please insert event Price");
                else {
                    event_Price = Integer.parseInt(eventPrice);
                    til_eventPrice.setErrorEnabled(false);
                }

                organizerId = UserData.user.getId();

                if(uploadEventPoster){
                    // mengecncode gambar event poster dan event floorplan menjadi string
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap_eventPoster.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    posterName = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                    uploadEventPoster = false;
                }

                if(uploadEventFloorPlan){
                    ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
                    bitmap_eventFloorPlan.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream2);
                    floorPlanName = Base64.encodeToString(byteArrayOutputStream2.toByteArray(), Base64.DEFAULT);
                    uploadEventFloorPlan = false;
                }

                final String eventMOU =  "mou-14.pdf";
                final int bazaarValidation = 1;

                Log.d("organizerId", String.valueOf(organizerId));
                Log.d("eventName", (eventName));
                Log.d("eventDescription", (eventDescription));
                Log.d("eventStartDate", String.valueOf(eventStartDate));
                Log.d("eventEndDate", String.valueOf(eventEndDate));
                Log.d("eventStartTime", (eventStartTime));
                Log.d("eventEndTime", (eventEndTime));
                Log.d("eventLocation", (eventLocation));
                Log.d("eventCity", (eventCity));
                Log.d("eventTotalBooth", String.valueOf(eventTotalBooth));
                Log.d("event_Price", String.valueOf(event_Price));
                Log.d("eventPoster", String.valueOf(posterName));
                Log.d("eventFloorPlan", String.valueOf(floorPlanName));
                Log.d("eventMOU", (eventMOU));
                Log.d("eventCategoryID", String.valueOf(bazaarCategoryId));
                Log.d("bazaarStatus", String.valueOf(bazaarStatusId));

                if (!eventName.isEmpty() && !eventDescription.isEmpty() && !eventLocation.isEmpty() && !eventCity.isEmpty() && !eventPrice.isEmpty()) {
                    Log.d("IF", "MASUK IF");
                    if (i.getStringExtra("MODE").equals("UPDATE")) {
                        Log.d("MODE", "UPDATE");
                        Log.d("bazaarID", String.valueOf(bazaarId));
                        Call<Bazaars> call = jsonPlaceHolder.updateBazaar(bazaarId, eventName, eventDescription, String.valueOf(eventStartDate),
                                String.valueOf(eventEndDate), eventStartTime, eventEndTime, eventLocation,
                                eventTotalBooth, event_Price, posterName, floorPlanName, eventMOU, eventCity,
                                bazaarValidation, bazaarStatusId, bazaarCategoryId);
                        call.enqueue(new Callback<Bazaars>() {
                            @Override
                            public void onResponse(Call<Bazaars> call, Response<Bazaars> response) {
                                Log.d("RESPONSE MESSAGE", response.message());
                                Log.d("RESPONSE SUCCESSFUL", String.valueOf(response.isSuccessful()));
                                Log.d("MESSAGE3", response.raw().toString());
                                Log.d("MESSAGE4", response.toString());
                                Log.d("MESSAGE5", response.body().toString());
                                if(response.isSuccessful() && response.body() != null) {
                                    Bazaars bzr = response.body();
                                    Log.e("ONRESPONSE", String.valueOf(bzr.getSuccess()));
                                    Log.e("MESSAGE", bzr.getMessage());
                                    Log.e("SUCCESSFUL?", String.valueOf(response.isSuccessful()));

                                    Toast.makeText(AddEventActivity.this, "Event Successfully Update",
                                            Toast.LENGTH_SHORT).show();
//                                    finish();
                                    Intent eventListIntent = new Intent(AddEventActivity.this, EventListActivity.class);
                                    startActivity(eventListIntent);
                                }
                            }

                            @Override
                            public void onFailure(Call<Bazaars> call, Throwable t) {
                                Log.e("ERROR", t.getMessage());
                            }
                        });
                    } else {
                        Log.d("MODE", "CREATE");

                        Call<Bazaars> call = jsonPlaceHolder.createBazaar(organizerId, eventName, eventDescription,
                                String.valueOf(eventStartDate), String.valueOf(eventEndDate), eventStartTime, eventEndTime,
                                eventLocation, eventTotalBooth, event_Price, posterName, floorPlanName, eventMOU, eventCity,
                                bazaarValidation, bazaarStatusId, bazaarCategoryId);
                        call.enqueue(new Callback<Bazaars>() {
                            @Override
                            public void onResponse(Call<Bazaars> call, Response<Bazaars> response) {

                                Log.d("RESPONSE MESSAGE", response.message());
                                Log.d("RESPONSE SUCCESSFUL", String.valueOf(response.isSuccessful()));
                                Log.d("MESSAGE3", response.raw().toString());
                                Log.d("MESSAGE4", response.toString());
                                if(response.isSuccessful() && response.body() != null){
                                    Bazaars bzr = response.body();

                                    Log.e("ONRESPONSE", String.valueOf(bzr.getSuccess()));
                                    Log.e("MESSAGE", bzr.getMessage());
                                    Log.e("SUCCESSFUL?", String.valueOf(response.isSuccessful()));
                                    Log.d("SUCCESSFUL ADDED", "SUCCESS");

                                    Toast.makeText(AddEventActivity.this, "New Event Successfully Added",
                                            Toast.LENGTH_SHORT).show();
//                                    finish();
                                    Intent eventListIntent = new Intent(AddEventActivity.this, EventListActivity.class);
                                    startActivity(eventListIntent);
                                }else {
                                    Log.d("CREATE", "CREATE FAILED");
                                }
                            }

                            @Override
                            public void onFailure(Call<Bazaars> call, Throwable t) {
                                Log.e("ERROR",t.getMessage());
                            }
                        });
                    }
                }
            }
        });

        btn_eventCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent eventlist = new Intent(AddEventActivity.this, EventListActivity.class);
                startActivity(eventlist);
            }
        });
    }

    private void fetchSpinnerCategory(ArrayList<String> categoryName, ArrayList<String> categoryID) {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(AddEventActivity.this, R.layout.support_simple_spinner_dropdown_item, categoryName);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spn_eventCategory.setAdapter(spinnerArrayAdapter);
        spn_eventCategory.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        eventCategory = parent.getItemAtPosition(position).toString();
        int SelectedPosition = parent.getSelectedItemPosition();
        bazaarCategoryId = Integer.parseInt(categoryID.get(SelectedPosition));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    // function untuk mengatur spinner agar sesuai dengan data yang diambil dari database
    private int getIndex(Spinner spinner, String selectedcategory){
        int index = 0;
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(selectedcategory)){
                index = i;
            }
        }
        return index;
    }

    // function untuk mengkoversi tanggal
    public String convertDate(int input) {
        if (input >= 10) {
            return String.valueOf(input);
        } else {
            return "0" + input;
        }
    }

    // function untuk memasukan waktu yang dipilih ke dalam button startTime dan EndTime
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (mTimeStart) {
            btn_eventStartTime.setText(convertDate(hourOfDay) + ":" + convertDate(minute));
        } else if (mTimeEnd) {
            btn_eventEndTime.setText(convertDate(hourOfDay) + ":" + convertDate(minute));
        }
    }

    // memilih foto yang digunakan untuk upload gambar poster, mou dan foorplan (dapat memilih dari gallery atau melalui kamera)
    private void selectImage() {
        final CharSequence[] items = {
                "Take Photo",
                "Choose from Library",
                "Cancel"
        };

        // memunculkan alert dialog terkait pilihan di atas.
        AlertDialog.Builder builder = new AlertDialog.Builder(AddEventActivity.this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Take Photo")) {
                    getPicFromCamera();
                } else if (items[i].equals("Choose from Library")) {
                    getPicFromGallery();
                } else if (items[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    // mengambil gambar menggunakan camera
    private void getPicFromCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQ_CODE_CAMERA_VERIFY);
    }

    // mengambil gambar dari gallery
    private void getPicFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQ_CODE_GALLERY_VERIFY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) return;

        // mengambil data dari result yang ada.
        switch (requestCode) {
            // mengambil gambar dari gallery untuk upload
            case REQ_CODE_GALLERY_VERIFY:
                if (data != null) {
                    Uri contentUri = data.getData();
                    try {
                        // mengecek apakah gambar merupakan event poster atau event floorplan
                        if(imgeventPoster){
                            bitmap_eventPoster = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentUri);
                            img_eventPoster.setVisibility(View.VISIBLE);
                            img_eventPoster.setImageBitmap(bitmap_eventPoster);
                        }else if(imgeventFloorPlan){
                            bitmap_eventFloorPlan = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentUri);
                            img_eventFloorPlan.setVisibility(View.VISIBLE);
                            img_eventFloorPlan.setImageBitmap(bitmap_eventFloorPlan);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(AddEventActivity.this, "Failed to get image", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            // mengambil gambar dari kamera untuk upload
            case REQ_CODE_CAMERA_VERIFY:
                Bundle extra_verify = data.getExtras();

                if(imgeventPoster){
                    bitmap_eventPoster = (Bitmap) extra_verify.get("data");
                    img_eventPoster.setVisibility(View.VISIBLE);
                    img_eventPoster.setImageBitmap(bitmap_eventPoster);
                }else if(imgeventFloorPlan){
                    bitmap_eventFloorPlan = (Bitmap) extra_verify.get("data");
                    img_eventFloorPlan.setVisibility(View.VISIBLE);
                    img_eventFloorPlan.setImageBitmap(bitmap_eventFloorPlan);
                }
                break;
        }
    }
}

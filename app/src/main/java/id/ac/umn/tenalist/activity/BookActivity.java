package id.ac.umn.tenalist.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import id.ac.umn.tenalist.ApiConfig;
import id.ac.umn.tenalist.Interface.JsonPlaceHolder;
import id.ac.umn.tenalist.R;
import id.ac.umn.tenalist.UserData;
import id.ac.umn.tenalist.BazaarBooth;
import id.ac.umn.tenalist.model.BazaarTransaction;
import id.ac.umn.tenalist.model.BoothSlotNumber;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{
    private JsonPlaceHolder jsonPlaceHolder;
    public int paymentStatus; // 0 = downpayment, 1 = fullpayment, 2 = invalid
    public int bazaarBoothId, totalPaid, BoothNumber, bazaarPrice, tenantId, bazaarId, organizerId;
    public String bazaarStartDate, bazaarEndDate, floorPlan;
    public SimpleDateFormat sdf;
    public Date startingDate, endingDate, DBStartDate, DBEndDate;

    private TextView tvStartDate, tvEndDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener1, mDateSetListener2;
    private Button btnBook;
    private Spinner spPaymentMethod, sBoothCode;
    private ImageView ivFloorplan;

    public String DBStart, DBEnd;
    public int year1, year2, month1, month2, day1, day2;

    private ArrayList<String> Bid;
    private ArrayList<String> BNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        setTitle("Event Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ivFloorplan = findViewById(R.id.ivBazaarSlot);
        tenantId = UserData.user.getId();

        // Get all extras from Prev. Intent
        Intent i = getIntent();
        bazaarId = i.getIntExtra("BazaarId", 1);
        DBStart = i.getStringExtra("StartDate");
        DBEnd = i.getStringExtra("EndDate");
        bazaarPrice = i.getIntExtra("Price", 0);
        organizerId = i.getIntExtra("OrganizerId", 1);
        floorPlan = i.getStringExtra("floorPlan");
//        Log.d("DEBUG", floorPlan + "");

        Glide.with(getApplicationContext()).load(ApiConfig.FINAL_URL + "floor-plan/" + floorPlan).into(ivFloorplan);

        // Initiate for year, month date (start and end)
        String[] arrStartDate = DBStart.split("-", 5);
        year1 = Integer.parseInt(arrStartDate[0]);
        month1 = Integer.parseInt(arrStartDate[1]);
        day1 = Integer.parseInt(arrStartDate[2]);

        String[] arrEndDate = DBEnd.split("-", 5);
        year2 = Integer.parseInt(arrEndDate[0]);
        month2 = Integer.parseInt(arrEndDate[1]);
        day2 = Integer.parseInt(arrEndDate[2]);

        //dateformat declaration
        sdf = new SimpleDateFormat("MMMM dd, yyyy");

        //date Start
        tvStartDate = (TextView) findViewById(R.id.tvStartDate);
        String startDate = sdf.format(new Date(year1-1900, month1, day1));
        tvStartDate.setText(startDate);
        try {
            startingDate = sdf.parse(startDate);
            DBStartDate = startingDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tvStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog1 = new DatePickerDialog(
                        BookActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener1, year1,month1,day1);
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog1.show();
            }
        });
        mDateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String startDate = sdf.format(new Date(year-1900, month, dayOfMonth));
                try {
                    startingDate = sdf.parse(startDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                month = month+1;
                bazaarStartDate = month + "-" + dayOfMonth + "-" + year;
                tvStartDate.setText(startDate);
            }
        };

        //Date End
        tvEndDate = (TextView) findViewById(R.id.tvEndDate);
        String EndDate = sdf.format(new Date(year2-1900, month2, day2));
        tvEndDate.setText(EndDate);
        try {
            endingDate = sdf.parse(EndDate);
            DBEndDate = endingDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tvEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog2 = new DatePickerDialog(
                        BookActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener2, year2,month2,day2);
                dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog2.show();
            }
        });
        mDateSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String EndDate = sdf.format(new Date(year-1900, month, dayOfMonth));
                try {
                    endingDate = sdf.parse(EndDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                month = month+1;
                bazaarEndDate = month + "-" + dayOfMonth + "-" + year;
                tvEndDate.setText(EndDate);
            }
        };

        Bid = new ArrayList<String>();
        BNum = new ArrayList<String>();

        //Button Book Now - btnBook
        btnBook = (Button) findViewById(R.id.btnBook);
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startDate = tvStartDate.getText().toString();
                String EndDate = tvEndDate.getText().toString();
                if(startingDate.compareTo(endingDate) > 0){
                    Toast.makeText(getApplicationContext(), "Wrong Date Input", Toast.LENGTH_LONG).show();
                }else if(startingDate.compareTo(endingDate) <= 0) {
                    // Count days
                    if ((DBStartDate.compareTo(startingDate) <= 0) && (endingDate.compareTo(DBEndDate) <= 0)){
                        long diff = endingDate.getTime() - startingDate.getTime();
                        final int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1;
                        totalPaid = days * bazaarPrice;
//                    Toast.makeText(getApplicationContext(), days + " - " + startingDate + " - " + endingDate, Toast.LENGTH_LONG).show();
//                     add to table BazaarTransaction
                        Call<BazaarTransaction> call = jsonPlaceHolder
                                .createBookTransaction(bazaarId, bazaarBoothId, tenantId, organizerId, totalPaid, paymentStatus);
                        call.enqueue(new Callback<BazaarTransaction>() {
                            @Override
                            public void onResponse(Call<BazaarTransaction> call, Response<BazaarTransaction> response) {
                                if(!response.isSuccessful()) {
                                    Log.e("error1", response.message());
                                    Toast.makeText(BookActivity.this, getString(R.string.error_something_wrong), Toast.LENGTH_LONG).show();
                                }else {
//                                Toast.makeText(BookActivity.this, "Proses sukses", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(BookActivity.this, PaymentActivity.class);
                                    intent.putExtra("paymentStatus", paymentStatus);
                                    intent.putExtra("BoothNumber", BoothNumber);
                                    intent.putExtra("price", totalPaid);
                                    intent.putExtra("bazaarId", bazaarId);
                                    intent.putExtra("bazaarBoothId", bazaarBoothId);
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onFailure(Call<BazaarTransaction> call, Throwable t) {
                                Log.e("error2", t.getMessage());
                                Toast.makeText(BookActivity.this, getString(R.string.error_something_wrong), Toast.LENGTH_LONG).show();
                            }
                        });
                    }else {
                        Toast.makeText(getApplicationContext(), "Wrong Date Input! Event is not held on that date", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        jsonPlaceHolder = ApiConfig.getApiConfig();
        final Call<BazaarBooth> boothCall = jsonPlaceHolder.getBazaarBooth(bazaarId);
        boothCall.enqueue(new Callback<BazaarBooth>() {
            @Override
            public void onResponse(Call<BazaarBooth> call, Response<BazaarBooth> response) {
                if(response.isSuccessful()){
                    List<BoothSlotNumber> bsn = response.body().getBazaarBoothList();

                    for(int i = 0; i < bsn.size(); i++){
                        Bid.add(String.valueOf(bsn.get(i).getBazaarBoothId()));
                        BNum.add(String.valueOf(bsn.get(i).getBazaarBoothNumber()));
                    }
                    Log.d("BookActivity", Bid.toString());
                    Log.d("BookActivity", BNum.toString());
                    fetchSpinnerBooth(BNum, Bid);
                }
            }

            @Override
            public void onFailure(Call<BazaarBooth> call, Throwable t) {
                Toast.makeText(BookActivity.this, getString(R.string.connection_problem),Toast.LENGTH_LONG).show();
            }
        });

        //Spinner BoothCode - sBoothCode
        sBoothCode = findViewById(R.id.sBoothCode);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.boothCode, android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Bid);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sBoothCode.setAdapter(adapter);
        sBoothCode.setOnItemSelectedListener(this);

        //Spinner Payment Method - sPayment
        spPaymentMethod = findViewById(R.id.sPayment);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.paymentMethod, android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPaymentMethod.setAdapter(adapter2);
        spPaymentMethod.setOnItemSelectedListener(this);
    }

    private void fetchSpinnerBooth(ArrayList<String> BNum, ArrayList<String> Bid) {
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(BookActivity.this, R.layout.support_simple_spinner_dropdown_item, BNum);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            sBoothCode.setAdapter(spinnerArrayAdapter);
            sBoothCode.setOnItemSelectedListener(this);
            Log.d("BookActivity", Bid.toString());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spin = (Spinner)parent;
        if(spin.getId() == R.id.sBoothCode)
        {
//            sBoothCode
            String BoothId = parent.getSelectedItem().toString();
            int SelectedPosition = parent.getSelectedItemPosition();
//            Toast.makeText(getApplicationContext(), BoothId + " index : " + SelectedPosition + ", BID " + Bid.get(SelectedPosition), Toast.LENGTH_LONG).show();
            bazaarBoothId = Integer.parseInt(Bid.get(SelectedPosition));
            BoothNumber = Integer.parseInt(BoothId);
        }
        if(spin.getId() == R.id.sPayment)
        {
//            sPayment
            String paymentMethod = parent.getItemAtPosition(position).toString();
            if(paymentMethod.compareTo("Down Payment") == 0) paymentStatus = 0;
            else if(paymentMethod.compareTo("Full Payment") == 0) paymentStatus = 1;
        }
//        Toast.makeText(this, bazaarBoothId + " : " + paymentStatus,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
}
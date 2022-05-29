package id.ac.umn.tenalist.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import id.ac.umn.tenalist.ApiConfig;
import id.ac.umn.tenalist.Interface.JsonPlaceHolder;
import id.ac.umn.tenalist.R;
import id.ac.umn.tenalist.TransactionData;
import id.ac.umn.tenalist.model.BazaarDetail;
import id.ac.umn.tenalist.model.BazaarTransaction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {
    private JsonPlaceHolder jsonPlaceHolder;
    // paymentstatus >>>> 0 = downpayment, 1 = fullpayment, 2 = cancelled
    public int paymentStatus, bazaarBoothNumber, totalPaid, oldpaymentStatus, bazaarId, bazaarBoothId, bazaarTransactionId;
    private EditText etAccountNumber, etAccountName;
    private TextView tvPlaceNumber, tvEventName, tvAddress, tvDate, tvTime, tvBookNum, tvPrice, tvCountDown;
    private RadioButton rbDownPayment, rbFullPayment;
    private RadioGroup rgPaymentMethod;
    private Button btnPayNow;
    public String accountNum, accountName;
    private CountDownTimer countDownTimer;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.onFinish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        setTitle("Payment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvEventName = findViewById(R.id.tvEventName);
        tvAddress = findViewById(R.id.tvAddress);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        tvBookNum = findViewById(R.id.tvBookNum);
        tvCountDown = findViewById(R.id.tvCountDown);
        etAccountName = findViewById(R.id.etAccountName);
        etAccountNumber = findViewById(R.id.etAccountNumber);

        // Start of getting value from activity before
        Intent i = getIntent();
        bazaarBoothNumber = i.getIntExtra("BoothNumber", 0);
        paymentStatus = i.getIntExtra("paymentStatus", 0);
        oldpaymentStatus = paymentStatus;
        totalPaid = i.getIntExtra("price", 0);
        bazaarId = i.getIntExtra("bazaarId", 1);
        bazaarBoothId = i.getIntExtra("bazaarBoothId", 1);
        // End of getting value from activity before

        // Set place number
        tvPlaceNumber = findViewById(R.id.tvPlaceNumber);
        tvPlaceNumber.setText(bazaarBoothNumber + "");

        // Set Total Price
        tvPrice = findViewById(R.id.tvPrice);
        tvPrice.setText("Rp. " + totalPaid + ",-");

        countDownTimer = new CountDownTimer(180000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                NumberFormat f = new DecimalFormat("00");
//                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;

                tvCountDown.setText(f.format(min) + ":" + f.format(sec));
            }

            @Override
            public void onFinish() {
//                Toast.makeText(getApplicationContext(), "finished", Toast.LENGTH_LONG).show();
                Call<BazaarTransaction> cancelCall = jsonPlaceHolder.cancelTransaction(bazaarTransactionId, bazaarBoothId);
                cancelCall.enqueue(new Callback<BazaarTransaction>() {
                    @Override
                    public void onResponse(Call<BazaarTransaction> call, Response<BazaarTransaction> response) {
                        Toast.makeText(getApplicationContext(), "Your Transaction is Failed, count down is over!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
                        startActivity(intent);
                        Log.e("ERROR", "SUCCESS");
                    }

                    @Override
                    public void onFailure(Call<BazaarTransaction> call, Throwable t) {
                        Log.e("ERROR", "FAILED");
                    }
                });
            }
        }.start();

        jsonPlaceHolder = ApiConfig.getApiConfig();
        Call<BazaarDetail> call = jsonPlaceHolder.getBazaarDetail(bazaarId);
        call.enqueue(new Callback<BazaarDetail>() {
            @Override
            public void onResponse(Call<BazaarDetail> call, Response<BazaarDetail> response) {
                if(response.body().success == 1){
                    String eventName = response.body().getBazaarDetail().getBazaarName();
                    String StartDate = response.body().getBazaarDetail().getBazaarStartDate();
                    String EndDate = response.body().getBazaarDetail().getBazaarEndDate();
                    String Address = response.body().getBazaarDetail().getBazaarLocation();
                    String StartTime = response.body().getBazaarDetail().getBazaarStartTime();
                    String EndTime = response.body().getBazaarDetail().getBazaarEndTime();
                    String category = response.body().getBazaarDetail().getBazaarCategoryName();

                    tvEventName.setText(category + " event - " + eventName);
                    tvDate.setText(StartDate + " - " + EndDate);
                    tvAddress.setText(Address);
                    tvTime.setText(StartTime + " - " + EndTime);
                }else {
                    Log.e("Error", "unsuccess");
                    Toast.makeText(PaymentActivity.this, getString(R.string.error_something_wrong),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BazaarDetail> call, Throwable t) {
                Log.e("ERROR - Failure", t.toString());
                Toast.makeText(PaymentActivity.this, getString(R.string.connection_problem),Toast.LENGTH_LONG).show();
            }
        });

        // Call transactionData
        Call<TransactionData> transactionDataCall = jsonPlaceHolder.getCurrentTransaction(bazaarBoothId, oldpaymentStatus);
        transactionDataCall.enqueue(new Callback<TransactionData>() {
            @Override
            public void onResponse(Call<TransactionData> call, Response<TransactionData> response) {
                if(response.body().success == 1){
                    int BazaarTransactionId = response.body().getBazaarTransaction().getBazaarTransactionId();
//                    Log.e("ERROR - BTID", BazaarTransactionId + "");
                    tvBookNum.setText("BOOKING NUMBER : " + BazaarTransactionId);
                    getTransactionId(BazaarTransactionId);
                }else {
                    Log.e("Error", "unsuccess");
                    Toast.makeText(PaymentActivity.this, getString(R.string.error_something_wrong),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<TransactionData> call, Throwable t) {
                Log.e("ERROR - Failure", t.getMessage());
                Toast.makeText(PaymentActivity.this, getString(R.string.connection_problem),Toast.LENGTH_LONG).show();
            }
        });

        //Start of radio button and radio group
        rbDownPayment = findViewById(R.id.rbDP);
        rbFullPayment = findViewById(R.id.rbFP);
        if(paymentStatus == 0){
            rbDownPayment.setChecked(true);
        }else {
            rbFullPayment.setChecked(true);
        }

        rgPaymentMethod = findViewById(R.id.rgPaymentMethod);
        rgPaymentMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rbDP:
                        //Down Payment
                        paymentStatus = 0;
                        break;
                    case R.id.rbFP:
                        //Full Payment
                        paymentStatus = 1;
                        break;
                }
            }
        });
        //END of radio button and radio group

        //Start of Button Pay Now
        btnPayNow = (Button) findViewById(R.id.btnPayNow);
        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), bazaarTransactionId + " - " + paymentStatus + " - " + accountNum + " - " + accountName, Toast.LENGTH_LONG).show();
                accountNum = etAccountNumber.getText().toString();
                accountName = etAccountName.getText().toString();
                Call<BazaarTransaction> updateTransCall = jsonPlaceHolder.updateTransaction(bazaarTransactionId, paymentStatus, accountNum, accountName, bazaarBoothId);
                updateTransCall.enqueue(new Callback<BazaarTransaction>() {
                    @Override
                    public void onResponse(Call<BazaarTransaction> call, Response<BazaarTransaction> response) {
                        if(response.isSuccessful()){
                            countDownTimer.cancel();
//                            Log.e("payment A.", response.toString());
//                            Toast.makeText(getApplicationContext(), bazaarTransactionId + " - " + paymentStatus + " - " + accountNum + " - " + accountName, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
                            startActivity(intent);
                            Log.e("Success", "transaction updated");
                        }else {
                            Log.e("Error", "unsuccess");
                            Toast.makeText(PaymentActivity.this, getString(R.string.error_something_wrong), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<BazaarTransaction> call, Throwable t) {
                        Log.e("ERROR - Failure", t.toString());
                        Toast.makeText(PaymentActivity.this, getString(R.string.connection_problem),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        //End of Button Pay Now
    }

    private void getTransactionId(int transId) {
        bazaarTransactionId = transId;
    }
}

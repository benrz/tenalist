package id.ac.umn.tenalist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Pattern;

import id.ac.umn.tenalist.ApiConfig;
import id.ac.umn.tenalist.Interface.JsonPlaceHolder;
import id.ac.umn.tenalist.R;
import id.ac.umn.tenalist.UserData;
import id.ac.umn.tenalist.model.InsertResponse;
import id.ac.umn.tenalist.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private TextView tv_heading;
    private TextView tv_error_message;
    private TextInputEditText edt_email;
    private TextInputEditText edt_name;
    //    private TextInputEditText edt_username;
    private TextInputEditText edt_password;
    private TextInputEditText edt_conf_password;
    private TextInputEditText edt_phone;
    private TextInputLayout til_email;
    private TextInputLayout til_name;
    //    private TextInputLayout til_username;
    private TextInputLayout til_password;
    private TextInputLayout til_conf_password;
    private TextInputLayout til_phone;

    private RadioGroup rg_role;
    private RadioButton rb_tenant;
    private RadioButton rb_organizer;

    private CheckBox cb_terms_conditions;
    private Button btn_register;
    private TextView tv_cancel;
    private ProgressBar progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        //Layout
        tv_heading = findViewById(R.id.tv_heading);
        tv_error_message = findViewById(R.id.tv_error_message);
        til_email = findViewById(R.id.til_email);
        til_name = findViewById(R.id.til_name);
//        til_username = findViewById(R.id.til_username);
        til_password = findViewById(R.id.til_password);
        til_conf_password = findViewById(R.id.til_conf_password);
        til_phone = findViewById(R.id.til_phone);
        edt_email = findViewById(R.id.edt_email);
        edt_name = findViewById(R.id.edt_name);
        edt_password = findViewById(R.id.edt_password);
        edt_conf_password = findViewById(R.id.edt_conf_password);
        edt_phone = findViewById(R.id.edt_phone);
        rg_role = findViewById(R.id.rg_role);
        rb_tenant = findViewById(R.id.rb_tenant);
        rb_organizer = findViewById(R.id.rb_organizer);

        cb_terms_conditions = findViewById(R.id.cb_terms_conditions);
        btn_register = findViewById(R.id.btn_register);
        tv_cancel = findViewById(R.id.tv_cancel);
        progress_bar = findViewById(R.id.progress_bar);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_error_message.setVisibility(View.GONE);

                //Checking empty and invalid input field
                final String email = edt_email.getText().toString();
                Pattern pattern = Patterns.EMAIL_ADDRESS;
                boolean emailFlag = pattern.matcher(email).matches();
                if (email.isEmpty()) til_email.setError("Please insert your email.");
                else if (!emailFlag) til_email.setError("Invalid email address.");
                else til_email.setErrorEnabled(false);

                final String name = edt_name.getText().toString();
                if (name.isEmpty()) til_name.setError("Please insert your name");
                else til_name.setErrorEnabled(false);

                String password = edt_password.getText().toString();
                if (password.isEmpty()) til_password.setError("Please insert your password");
                else til_password.setErrorEnabled(false);

                String confPassword = edt_conf_password.getText().toString();
                boolean confFlag = confPassword.equals(password);
                if (confPassword.isEmpty() || !confFlag)
                    til_conf_password.setError("Passwords did not match");
                else til_conf_password.setErrorEnabled(false);

                final String phone = edt_phone.getText().toString();
                if (phone.isEmpty())
                    til_phone.setError("Please insert your phone number\"");
                else til_phone.setErrorEnabled(false);

                if (!cb_terms_conditions.isChecked()) {
                    cb_terms_conditions.setError("Please read our terms and conditions");
                    cb_terms_conditions.requestFocus();
                } else cb_terms_conditions.setError(null);

                int radio = rg_role.getCheckedRadioButtonId();
                String role = "";
                if (radio <= 0) {//Grp is your radio group object
                    rb_tenant.setError("Select your role");//Set error to last Radio button
                    rb_organizer.setError("Select your role");//Set error to last Radio button
                } else {
                    rb_tenant.setError(null);
                    rb_organizer.setError(null);
                    if (radio == findViewById(R.id.rb_organizer).getId()) role = "organizer";
                    else role = "tenant";
                }

                //If inputs are valid, then
                if (emailFlag && !name.isEmpty() && !password.isEmpty() && confFlag && !phone.isEmpty() && radio > 0 && cb_terms_conditions.isChecked()) {
                    progress_bar.setVisibility(View.VISIBLE);
                    tv_error_message.setVisibility(View.GONE);

                    final String finalRole = role;
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                final FirebaseUser firebaseUser = task.getResult().getUser();
                                JsonPlaceHolder jsonPlaceHolder = ApiConfig.getApiConfig();

                                Call<InsertResponse> call = jsonPlaceHolder.addUser(email, name, phone, finalRole);
                                call.enqueue(new Callback<InsertResponse>() {
                                    @Override
                                    public void onResponse(Call<InsertResponse> call, Response<InsertResponse> response) {
//                                        Log.e("REG", email+" "+name+" "+phone+" "+finalRole );
                                        if (!response.isSuccessful()) {
                                            Log.e("REG", "onResponse: matila" + response.message());
                                            progress_bar.setVisibility(View.GONE);
                                            firebaseUser.delete();
                                            tv_error_message.setText(getString(R.string.error_something_wrong));
                                            tv_error_message.setVisibility(View.VISIBLE);
                                            return;
                                        }
                                        InsertResponse insertResponse = response.body();
//                                        if(insertResponse.getStatus().equalsIgnoreCase("success")){
                                        UserData.user = new User(email, name, finalRole, phone, insertResponse.getId(), 0, 0);
                                        Log.e("REG", UserData.user.getEmail() + " " + UserData.user.getName());

                                        // Add to Firebase Database
                                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                        assert currentUser != null;
                                        String currentUserId = currentUser.getUid();
                                        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("users").child(currentUserId);
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("uid", currentUserId);
                                        hashMap.put("name", name);
                                        hashMap.put("photo", "default");
                                        hashMap.put("role", finalRole);
                                        hashMap.put("id", insertResponse.getId());

                                        usersReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    progress_bar.setVisibility(View.GONE);
                                                    Toast.makeText(RegisterActivity.this, "Your account is registered", Toast.LENGTH_SHORT).show();
                                                    //Moving to Main Activity
                                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                                // Toast.makeText(RegisterActivity.this, "Added to Firebase Database successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        });
//                                        progress_bar.setVisibility(View.GONE);
//                                        Toast.makeText(RegisterActivity.this, "Your account is registered", Toast.LENGTH_SHORT).show();
//                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<InsertResponse> call, Throwable t) {
                                        firebaseUser.delete();
                                        progress_bar.setVisibility(View.GONE);
                                        tv_error_message.setText(getString(R.string.error_something_wrong));
                                        tv_error_message.setVisibility(View.VISIBLE);
                                    }
                                });

                            } else {
                                // Error message
                                String errorMessage = "";
                                if (task.getException() instanceof FirebaseAuthException) {
                                    String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                                    switch (errorCode) {
                                        case "ERROR_EMAIL_ALREADY_IN_USE":
                                            errorMessage = getString(R.string.register_error_email_in_used);
                                            break;

                                        case "ERROR_WEAK_PASSWORD":
                                            errorMessage = getString(R.string.register_error_weak_password);
                                            break;
                                        default:
                                            errorMessage = task.getException().getMessage();
                                            break;
                                    }
                                } else {
                                    errorMessage = getString(R.string.network_error);
                                }
                                tv_error_message.setVisibility(View.VISIBLE);
                                tv_error_message.setText(errorMessage);
                            }
                        }
                    });
                }
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

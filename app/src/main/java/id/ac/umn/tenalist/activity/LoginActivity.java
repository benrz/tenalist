package id.ac.umn.tenalist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import java.util.regex.Pattern;

import id.ac.umn.tenalist.ApiConfig;
import id.ac.umn.tenalist.Interface.JsonPlaceHolder;
import id.ac.umn.tenalist.R;
import id.ac.umn.tenalist.UserData;
import id.ac.umn.tenalist.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private TextView tv_heading;
    private TextView tv_error_message;
    private TextInputEditText edt_email;
    private TextInputEditText edt_password;
    private TextInputLayout til_email;
    private TextInputLayout til_password;
    private Button btn_login;
    private TextView tv_register;
    private ProgressBar progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        tv_heading = findViewById(R.id.tv_heading);
        tv_error_message = findViewById(R.id.tv_error_message);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        til_email = findViewById(R.id.til_email);
        til_password = findViewById(R.id.til_password);
        btn_login = findViewById(R.id.btn_login);
        tv_register = findViewById(R.id.tv_register);
        progress_bar = findViewById(R.id.progress_bar);

        String text = "<font color=#000000>New user?</font> " +
                "<font color=#FF9A9E>Register now</font>";
        tv_register.setText(Html.fromHtml(text));

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_error_message.setVisibility(View.GONE);
                //Checking empty and invalid input field
                String email = edt_email.getText().toString();
                Pattern pattern = Patterns.EMAIL_ADDRESS;
                boolean emailFlag = pattern.matcher(email).matches();
                if (email.isEmpty()) til_email.setError("Please insert your email.");
                else if (!emailFlag) til_email.setError("Invalid email address.");
                else til_email.setErrorEnabled(false);

                String password = edt_password.getText().toString();
                if (password.isEmpty()) til_password.setError("Please insert your password");
                else til_password.setErrorEnabled(false);

                //If inputs are valid, then
                if (emailFlag && !password.isEmpty()) {
                    progress_bar.setVisibility(View.VISIBLE);
                    final String finalEmail = email;
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            tv_error_message.setVisibility(View.GONE);

                            if (task.isSuccessful()) {
                                JsonPlaceHolder jsonPlaceHolder = ApiConfig.getApiConfig();

                                Call<User> call = jsonPlaceHolder.getUser(finalEmail);
                                call.enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User> response) {
                                        progress_bar.setVisibility(View.GONE);
                                        User user=response.body();
                                        UserData.user= user;
                                    }

                                    @Override
                                    public void onFailure(Call<User> call, Throwable t) {
                                        progress_bar.setVisibility(View.GONE);
                                        tv_error_message.setText(getResources().getString(R.string.error_something_wrong));
                                        tv_error_message.setVisibility(View.VISIBLE);
                                    }
                                });

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                //Error message
                                String errorMessage = "";
                                if (task.getException() instanceof FirebaseAuthException) {
                                    errorMessage = getString(R.string.login_error);
                                } else {
                                    errorMessage = getString(R.string.network_error);
                                }
                                tv_error_message.setVisibility(View.VISIBLE);
                                tv_error_message.setText(errorMessage);
                                progress_bar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

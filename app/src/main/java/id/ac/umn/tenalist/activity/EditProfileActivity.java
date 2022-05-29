package id.ac.umn.tenalist.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import id.ac.umn.tenalist.ApiConfig;
import id.ac.umn.tenalist.Interface.JsonPlaceHolder;
import id.ac.umn.tenalist.R;
import id.ac.umn.tenalist.UserData;
import id.ac.umn.tenalist.model.InsertResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    public static final int REQ_CODE_GALLERY_PROFILE = 1;
    public static final int REQ_CODE_GALLERY_VERIFY = 2;
    public static final int REQ_CODE_CAMERA_PROFILE = 3;
    public static final int REQ_CODE_CAMERA_VERIFY = 4;

    private FirebaseAuth firebaseAuth;

    private ImageView img_profile;
    private ImageView img_verify;
    private TextView tv_name;
    private TextView tv_role;
    private TextView tv_bazaar_count;
    private TextView tv_edit;
    private TextView tv_verify;
    private TextView tv_error_message;
    private TextInputEditText edt_email;
    private TextInputEditText edt_name;
    private TextInputEditText edt_new_password;
    private TextInputEditText edt_conf_password;
    private TextInputEditText edt_old_password;
    private TextInputEditText edt_phone;
    private TextInputLayout til_email;
    private TextInputLayout til_name;
    private TextInputLayout til_new_password;
    private TextInputLayout til_conf_password;
    private TextInputLayout til_old_password;
    private TextInputLayout til_phone;

    private Button btn_update;
    private Button btn_cancel;
    private ProgressBar progress_bar;
    private ProgressBar progress_bar_img;

    private boolean prof_pic = false;
    private boolean verify = false;
    private Bitmap bitmap_profile = null;
    private Bitmap bitmap_verify = null;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        requestPermissions();

        firebaseAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.profile);

        //Layout
        img_profile = findViewById(R.id.img_profile);
        img_verify = findViewById(R.id.img_verify);
        tv_name = findViewById(R.id.tv_name);
        tv_role = findViewById(R.id.tv_role);
        tv_bazaar_count = findViewById(R.id.tv_bazaar_count);
        tv_edit = findViewById(R.id.tv_edit);
        tv_verify = findViewById(R.id.tv_verify);
        tv_error_message = findViewById(R.id.tv_error_message);
        til_email = findViewById(R.id.til_email);
        til_name = findViewById(R.id.til_name);
        til_new_password = findViewById(R.id.til_new_password);
        til_conf_password = findViewById(R.id.til_conf_password);
        til_old_password = findViewById(R.id.til_old_password);
        til_phone = findViewById(R.id.til_phone);
        edt_email = findViewById(R.id.edt_email);
        edt_name = findViewById(R.id.edt_name);
        edt_new_password = findViewById(R.id.edt_new_password);
        edt_conf_password = findViewById(R.id.edt_conf_password);
        edt_old_password = findViewById(R.id.edt_old_password);
        edt_phone = findViewById(R.id.edt_phone);

        btn_update = findViewById(R.id.btn_update);
        btn_cancel = findViewById(R.id.btn_cancel);
        progress_bar = findViewById(R.id.progress_bar);
        progress_bar_img = findViewById(R.id.progress_bar_img);

        //jika data user telah di-load, maka akan menunjukkan halaman dengan data user.
        if (UserData.user.getName() != null) {
            //jika user memiliki foto, maka akan me-load foto menggunakan glide.
            if (UserData.user.getPhoto() != null)
                Glide.with(this)
                        .load(ApiConfig.FINAL_URL + "profile/" + UserData.user.getPhoto())
                        .apply(RequestOptions.skipMemoryCacheOf(true))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                progress_bar_img.setVisibility(View.GONE);
                                if (UserData.user.getRole().equalsIgnoreCase("tenant"))
                                    img_profile.setImageDrawable(getDrawable(R.drawable.ic_tenant));
                                else
                                    img_profile.setImageDrawable(getDrawable(R.drawable.ic_organizer));
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                progress_bar_img.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(img_profile);

            //jika tidak memiliki foto, maka akan menggunakan logo tenant dan organizer.
            else {
                progress_bar_img.setVisibility(View.GONE);
                if (UserData.user.getRole().equalsIgnoreCase("tenant"))
                    img_profile.setImageDrawable(getDrawable(R.drawable.ic_tenant));
                else img_profile.setImageDrawable(getDrawable(R.drawable.ic_organizer));
            }

            tv_name.setText(UserData.user.getName());
            tv_role.setText(UserData.user.getRole());
            if (UserData.user.getRole().equalsIgnoreCase("tenant"))
                if (UserData.user.getCount_bazaar() == 0)
                    tv_bazaar_count.setText(getResources().getString(R.string.zero_tenant));
                else
                    tv_bazaar_count.setText(getResources().getQuantityString(R.plurals.numberOfBazaarsJoined, UserData.user.getCount_bazaar(), UserData.user.getCount_bazaar()));
            else if (UserData.user.getCount_bazaar() == 0)
                tv_bazaar_count.setText(getResources().getString(R.string.zero_tenant));
            else
                tv_bazaar_count.setText(getResources().getQuantityString(R.plurals.numberOfBazaarsHeld, UserData.user.getCount_bazaar(), UserData.user.getCount_bazaar()));

            edt_name.setText(UserData.user.getName());
            edt_email.setText(UserData.user.getEmail());
            edt_phone.setText(UserData.user.getPhone());
            if (UserData.user.isVerified()) tv_verify.setVisibility(View.GONE);

        } else {
            tv_error_message.setText(getResources().getString(R.string.network_error));
            tv_error_message.setVisibility(View.VISIBLE);
        }

        //onclick listener
        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(true);
            }
        });

        tv_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(false);
            }
        });

        img_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(false);
            }
        });

        //jika menekan update, maka akan mengecek input field.
        btn_update.setOnClickListener(new View.OnClickListener() {
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

                final String new_password = edt_new_password.getText().toString();
                boolean newPass = false;
                if (!new_password.isEmpty()) newPass = true;

                String conf_password = edt_conf_password.getText().toString();
                boolean confFlag = conf_password.equals(new_password);
                if (!confFlag)
                    til_conf_password.setError("Passwords did not match");
                else til_conf_password.setErrorEnabled(false);

                final String phone = edt_phone.getText().toString();
                if (phone.isEmpty())
                    til_phone.setError("Please insert your phone number\"");
                else til_phone.setErrorEnabled(false);

                //Jika input field isinya sudah sesuai, akan menjalankan code berikut.
                if (!name.isEmpty() && emailFlag && confFlag && !phone.isEmpty()) {
                    //jika input sama dengan data sebelumnya, maka tidak ada data yang diganti.
                    if (name.equalsIgnoreCase(UserData.user.getName()) && email.equalsIgnoreCase(UserData.user.getEmail()) && phone.equalsIgnoreCase(UserData.user.getPhone()) && !newPass && !prof_pic && !verify)
                        goBack();

                        //jika ada input yang diganti, maka akan mengupdate database dan firebase.
                    else {
                        final String old_pass = edt_old_password.getText().toString();
                        if ((old_pass.isEmpty() || !confFlag))
                            til_old_password.setError(getString(R.string.error_wrong_password));
                        else {
                            til_old_password.setErrorEnabled(false);
                            progress_bar.setVisibility(View.VISIBLE);

                            //update
                            final FirebaseUser user = firebaseAuth.getCurrentUser();
                            AuthCredential credential = EmailAuthProvider.getCredential(UserData.user.getEmail(), old_pass);
                            final boolean finalNewPass = newPass;

                            //mengecek apakah password lama sesuai dengan input
                            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        boolean flag = false;

                                        //change password
                                        if (finalNewPass)
                                            user.updatePassword(new_password).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    progress_bar.setVisibility(View.GONE);
                                                    if (!task.isSuccessful()) {
                                                        tv_error_message.setVisibility(View.VISIBLE);
                                                        tv_error_message.setText(getString(R.string.error_something_wrong));
                                                        return;
                                                    }
                                                    //update email and others
                                                    updateOthers(email, name, phone, user);
                                                }
                                            });
                                        else updateOthers(email, name, phone, user);
                                    } else {
                                        //password yang dimasukkan salah
                                        progress_bar.setVisibility(View.GONE);
                                        String errorMessage = "";
                                        if (task.getException() instanceof FirebaseNetworkException) {
                                            errorMessage = getString(R.string.network_error);
                                        } else {
                                            errorMessage = getString(R.string.error_password);
                                        }
                                        tv_error_message.setVisibility(View.VISIBLE);
                                        tv_error_message.setText(errorMessage);
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });

        //tidak jadi melakukan update
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });
    }

    //request permission untuk membaca data dari gallery menggunakan Dexter.
    private void requestPermissions() {
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                if (multiplePermissionsReport.areAllPermissionsGranted()) {
                    Toast.makeText(EditProfileActivity.this, "All permissions granted.", Toast.LENGTH_SHORT).show();
                }

                if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                    Toast.makeText(EditProfileActivity.this, "Permission denied!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).withErrorListener(new PermissionRequestErrorListener() {
            @Override
            public void onError(DexterError dexterError) {
                Toast.makeText(EditProfileActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        }).onSameThread()
                .check();
    }

    //memilih sorce image mana yang akan digunakan, memilih foto yang ada, ambil dari camera, atau membatalkan aktivitas.
    private void selectImage(final boolean isProfile) {
        final CharSequence[] items = {
                "Take Photo",
                "Choose from Library",
                "Cancel"
        };

        //memunculkan alert dialog terkait pilihan di atas.
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Take Photo")) {
                    getPicFromCamera(isProfile);
                } else if (items[i].equals("Choose from Library")) {
                    getPicFromGallery(isProfile);
                } else if (items[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    //mengambil gambar dari gallery
    private void getPicFromGallery(boolean isProfile) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (isProfile) startActivityForResult(galleryIntent, REQ_CODE_GALLERY_PROFILE);
        else startActivityForResult(galleryIntent, REQ_CODE_GALLERY_VERIFY);
    }

    //mengambil gambar menggunakan camera
    private void getPicFromCamera(boolean isProfile) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (isProfile) startActivityForResult(cameraIntent, REQ_CODE_CAMERA_PROFILE);
        else startActivityForResult(cameraIntent, REQ_CODE_CAMERA_VERIFY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) return;

        //mengambil data dari result yang ada.
        switch (requestCode) {
            //mengambil gambar dari gallery untuk profile picture
            case REQ_CODE_GALLERY_PROFILE:
                if (data != null) {
                    Uri contentUri = data.getData();
                    try {
                        bitmap_profile = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentUri);
                        img_profile.setImageBitmap(bitmap_profile);
                        prof_pic = true;

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(EditProfileActivity.this, "Failed to get image", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            //mengambil gambar dari camera untuk profile picture
            case REQ_CODE_CAMERA_PROFILE:
                Bundle extra_profile = data.getExtras();
                bitmap_profile = (Bitmap) extra_profile.get("data");
                img_profile.setImageBitmap(bitmap_profile);
                prof_pic = true;
                break;
            //mengambil gambar dari gallery untuk verify
            case REQ_CODE_GALLERY_VERIFY:
                if (data != null) {
                    Uri contentUri = data.getData();
                    try {
                        bitmap_verify = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentUri);
                        tv_verify.setText(R.string.verification_add);
                        img_verify.setVisibility(View.VISIBLE);
                        img_verify.setImageBitmap(bitmap_verify);
                        verify = true;

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(EditProfileActivity.this, "Failed to get image", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            //mengambil gambar dari kamera untuk verify
            case REQ_CODE_CAMERA_VERIFY:
                Bundle extra_verify = data.getExtras();
                bitmap_profile = (Bitmap) extra_verify.get("data");
                tv_verify.setText(R.string.verification_add);
                img_verify.setVisibility(View.VISIBLE);
                img_verify.setImageBitmap(bitmap_profile);
                verify = true;
                break;
        }
    }

    //mengecek data apa saja yang perlu di-update.
    private void updateOthers(final String email, final String name, final String phone, FirebaseUser user) {
        //check apakah email perlu di-update
        if (!email.equalsIgnoreCase(UserData.user.getEmail())) {
            progress_bar.setVisibility(View.VISIBLE);
            user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    progress_bar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        if (prof_pic)
                            updateDatabasePic(email, name, phone);
                        else updateDatabase(email, name, phone);
                    } else {
                        tv_error_message.setVisibility(View.VISIBLE);
                        Log.e("EDIT", "onComplete: EMAIl");
                        Log.e("EDIT", task.getException().getMessage());
                        tv_error_message.setText(getString(R.string.error_something_wrong));
                    }
                }
            });
        }

        //update data selain password dan email.
        else if (!name.equalsIgnoreCase(UserData.user.getName()) || !phone.equalsIgnoreCase(UserData.user.getPhone()) || prof_pic || verify) {
            if (prof_pic) updateDatabasePic(email, name, phone);
            else updateDatabase(email, name, phone);
        }

        //hanya update password saja atau tidak sama sekali.
        else {
            progress_bar.setVisibility(View.GONE);
            Toast.makeText(EditProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
        }
    }

    //function untuk update data jika disertai update foto.
    private void updateDatabasePic(final String email, final String name, final String phone) {
        progress_bar.setVisibility(View.VISIBLE);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        bitmap_profile.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String encoded_image = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        Log.e("UPDATE", "updateDatabase: masuk ke yg pic");
        int ver = 0;
        if (UserData.user.isVerified() || verify) ver = 1;

        JsonPlaceHolder jsonPlaceHolder = ApiConfig.getApiConfig();

        Call<InsertResponse> call = jsonPlaceHolder.editUserImage(UserData.user.getId(), email, name, phone, ver, encoded_image);
        final int finalVer = ver;
        call.enqueue(new Callback<InsertResponse>() {
            @Override
            public void onResponse(Call<InsertResponse> call, Response<InsertResponse> response) {
                progress_bar.setVisibility(View.GONE);
                if (!response.isSuccessful()) return;
                InsertResponse insertResponse = response.body();
                Log.e("RETROFIT", "onResponse:" + response);
                if (insertResponse.getStatus().equalsIgnoreCase("success")) {
                    Log.e("EDIT", "onResponse: DATABASE OK");
                    boolean isPhoto = UserData.user.getPhoto() == null;
                    UserData.user.setName(name);
                    UserData.user.setEmail(email);
                    UserData.user.setPhone(phone);
                    UserData.user.setPhoto("profile_" + UserData.user.getId());
                    UserData.user.setVer(finalVer);
                    UserData.picCount++;

                    // Update image in Firebase
                    if (isPhoto) {
                        progress_bar.setVisibility(View.VISIBLE);
                        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        final DatabaseReference currentUserReference = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());
                        currentUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                currentUserReference.child("photo").setValue("profile_" + UserData.user.getId() + ".jpg");
                                currentUserReference.child("name").setValue(UserData.user.getName());
                                progress_bar.setVisibility(View.GONE);
                                Toast.makeText(EditProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } else {
                        Toast.makeText(EditProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<InsertResponse> call, Throwable t) {
                progress_bar.setVisibility(View.GONE);
                tv_error_message.setText(getString(R.string.error_something_wrong));
                tv_error_message.setVisibility(View.VISIBLE);
            }
        });
    }

    //function untuk update data jika tidak disertai dengan foto.
    public void updateDatabase(final String email, final String name, final String phone) {
        final boolean isNameChange = !(UserData.user.getName().equalsIgnoreCase(name));
        progress_bar.setVisibility(View.VISIBLE);

        //ver untuk update integer yang mentrigger adanya perubahan profile pada home.
        int ver = 0;
        if (UserData.user.isVerified() || verify) ver = 1;
        JsonPlaceHolder jsonPlaceHolder = ApiConfig.getApiConfig();

        Call<InsertResponse> call = jsonPlaceHolder.editUser(UserData.user.getId(), email, name, phone, ver);
        final int finalVer = ver;
        call.enqueue(new Callback<InsertResponse>() {
            @Override
            public void onResponse(Call<InsertResponse> call, Response<InsertResponse> response) {
                progress_bar.setVisibility(View.GONE);
                if (!response.isSuccessful()) return;
                InsertResponse insertResponse = response.body();
                if (insertResponse.getStatus().equalsIgnoreCase("success")) {
//                    Log.e("EDIT", "onResponse: DATABASE OK");
                    UserData.user.setName(name);
                    UserData.user.setEmail(email);
                    UserData.user.setPhone(phone);
                    UserData.user.setVer(finalVer);

                    if (isNameChange) {
                        progress_bar.setVisibility(View.VISIBLE);
                        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        final DatabaseReference currentUserReference = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());
                        currentUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                currentUserReference.child("name").setValue(UserData.user.getName());
                                progress_bar.setVisibility(View.GONE);
                                Toast.makeText(EditProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    } else {
                        Toast.makeText(EditProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<InsertResponse> call, Throwable t) {
                progress_bar.setVisibility(View.GONE);
                tv_error_message.setText(getString(R.string.error_something_wrong));
                tv_error_message.setVisibility(View.VISIBLE);
            }
        });
    }

    //kembali ke halaman sebelumnya.
    public void goBack() {
        Toast.makeText(EditProfileActivity.this, "No changes", Toast.LENGTH_SHORT).show();
        finish();
    }
}
package id.ac.umn.tenalist.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import id.ac.umn.tenalist.ApiConfig;
import id.ac.umn.tenalist.Interface.JsonPlaceHolder;
import id.ac.umn.tenalist.R;
import id.ac.umn.tenalist.UserData;
import id.ac.umn.tenalist.adapter.EventListAdapter;
import id.ac.umn.tenalist.model.EventListModel;
import id.ac.umn.tenalist.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    public static final String EXT_USER = "extra_user";

    private ImageView img_profile;
    private ImageView img_verify;
    private TextView tv_name;
    private TextView tv_role;
    private TextView tv_bazaar_count;
    private TextView tv_rating;
    private TextView tv_list;
    private TextView tv_none;
    private RecyclerView rv_profile_bazaar;
    private ProgressBar progress_bar;
    private ProgressBar progress_bar_img;

    private ArrayList<EventListModel> eventList = new ArrayList<>();
    private EventListAdapter eventListAdapter;
    private User user;

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
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent.hasExtra(EXT_USER)) user = intent.getParcelableExtra(EXT_USER);
        else user = UserData.user;

        //set layout
        img_profile = findViewById(R.id.img_profile);
        tv_name = findViewById(R.id.tv_name);
        tv_role = findViewById(R.id.tv_role);
        tv_bazaar_count = findViewById(R.id.tv_bazaar_count);
        tv_rating = findViewById(R.id.tv_rating);
        tv_list = findViewById(R.id.tv_list);
        tv_none = findViewById(R.id.tv_none);
        progress_bar = findViewById(R.id.progress_bar);
        progress_bar_img = findViewById(R.id.progress_bar_img);

        rv_profile_bazaar = findViewById(R.id.rv_profile_bazaar);
        rv_profile_bazaar.setLayoutManager(new LinearLayoutManager(this));
        rv_profile_bazaar.setHasFixedSize(true);

        //if user has profile picture
        if (user.getPhoto() != null)
            //if user has profile picture
            if (!user.getPhoto().isEmpty())
                Glide.with(this)
                        .load(ApiConfig.FINAL_URL + "profile/" + user.getPhoto())
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
            else {
                //if user doesn't have profile picture
                progress_bar_img.setVisibility(View.GONE);
                if (user.getRole().equalsIgnoreCase("tenant"))
                    img_profile.setImageDrawable(getDrawable(R.drawable.ic_tenant));
                else img_profile.setImageDrawable(getDrawable(R.drawable.ic_organizer));
            }

        //user not null
        tv_name.setText(user.getName());
        tv_role.setText(user.getRole());

        //user doesn't have profile picture
        if (user.getRole().equalsIgnoreCase("tenant")) {
            if(user.getCount_bazaar()==0) tv_bazaar_count.setText(getResources().getString(R.string.zero_tenant));
            else tv_bazaar_count.setText(getResources().getQuantityString(R.plurals.numberOfBazaarsJoined, user.getCount_bazaar(), user.getCount_bazaar()));
            tv_rating.setVisibility(View.GONE);
        } else {
            if(user.getCount_bazaar()==0) tv_bazaar_count.setText(getResources().getString(R.string.zero_tenant));
            else tv_bazaar_count.setText(getResources().getQuantityString(R.plurals.numberOfBazaarsHeld, user.getCount_bazaar(), user.getCount_bazaar()));
            tv_rating.setText(String.format(getString(R.string.rating), String.valueOf(user.getRating())));
        }

        //event list
        JsonPlaceHolder jsonPlaceHolder = ApiConfig.getApiConfig();

        Call<List<EventListModel>> call = jsonPlaceHolder.getUserEvents(user.getId());
        progress_bar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<List<EventListModel>>() {
            @Override
            public void onResponse(Call<List<EventListModel>> call, Response<List<EventListModel>> response) {
                progress_bar.setVisibility(View.GONE);
                if (!response.isSuccessful()) {
                    return;
                }
                eventList = (ArrayList<EventListModel>) response.body();
                if (user.getRole().equalsIgnoreCase("tenant"))
                    tv_bazaar_count.setText(getResources().getQuantityString(R.plurals.numberOfBazaarsJoined, user.getCount_bazaar(),user.getCount_bazaar()));
                else
                    tv_bazaar_count.setText(getResources().getQuantityString(R.plurals.numberOfBazaarsHeld, user.getCount_bazaar(),user.getCount_bazaar()));
                setAdapter();
            }

            @Override
            public void onFailure(Call<List<EventListModel>> call, Throwable t) {
                progress_bar.setVisibility(View.GONE);
                tv_none.setVisibility(View.VISIBLE);
                setAdapter();
            }
        });
    }

    private void setAdapter() {
        eventListAdapter = new EventListAdapter(ProfileActivity.this, eventList);
        rv_profile_bazaar.setAdapter(eventListAdapter);
    }
}

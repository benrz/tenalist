package id.ac.umn.tenalist.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import id.ac.umn.tenalist.ApiConfig;
import id.ac.umn.tenalist.Interface.FragmentActionListener;
import id.ac.umn.tenalist.Interface.JsonPlaceHolder;
import id.ac.umn.tenalist.R;
import id.ac.umn.tenalist.UserData;
import id.ac.umn.tenalist.fragment.HomeFragment;
import id.ac.umn.tenalist.fragment.SearchCategoryFragment;
import id.ac.umn.tenalist.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentActionListener {

    private boolean login = false;
    private TextView tv_profile_name;
    private ProgressBar progress_bar;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ProgressBar progress_bar_img;
    private ImageView img_profile;
    private FloatingActionButton fab;
    private MenuItem menu_fav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        //Layout
        View headerview = navigationView.getHeaderView(0);
        tv_profile_name = headerview.findViewById(R.id.tv_profile_name);
        progress_bar = headerview.findViewById(R.id.progress_bar);
        progress_bar_img = headerview.findViewById(R.id.progress_bar_img);
        img_profile = headerview.findViewById(R.id.img_profile);

        menu_fav= navigationView.getMenu().findItem(R.id.nav_favorite);

        updateUser();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorAccent)));
        fab = findViewById(R.id.fab);
        if (UserData.user.getId() == 0 || UserData.user.getRole().equalsIgnoreCase("tenant")) {
            fab.setVisibility(View.GONE);
            menu_fav.setVisible(true);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addnewIntent = new Intent(MainActivity.this, AddEventActivity.class);
                addnewIntent.putExtra("MODE","ADD");
                startActivity(addnewIntent);
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        if(UserData.user!=null)
            if(UserData.user.getId()!=0)
                tv_profile_name.setText(UserData.user.getName());

        headerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_edit_profile = new Intent(MainActivity.this, EditProfileActivity.class);
                startActivity(intent_edit_profile);
            }
        });

        addFragment();
    }

    private void addFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setFragmentActionListener(this);

        fragmentTransaction.add(R.id.fragmentContainer, homeFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = this.fragmentManager.findFragmentById(R.id.fragmentContainer);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            Log.d("IF DRAWER", "CLOSE DRAWER");
        } else if (!(fragment instanceof HomeFragment)) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            HomeFragment homeFragment = new HomeFragment();
            homeFragment.setFragmentActionListener(this);

            fragmentTransaction.replace(R.id.fragmentContainer, homeFragment);
            fragmentTransaction.commit();
            Log.d("ELSE IF FRAGMENT", "CHANGE TO HOME FRAGMENT");
        } else {
            super.onBackPressed();
            Log.d("ELSE", "CLOSE APPS");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        if (UserData.user.getId() == 0) {
            Toast.makeText(MainActivity.this, "Loading user data", Toast.LENGTH_SHORT).show();
            return false;
        }

        int id = item.getItemId();
        switch (id) {
            case R.id.nav_home:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                HomeFragment homeFragment = new HomeFragment();
                homeFragment.setFragmentActionListener(this);

                fragmentTransaction.replace(R.id.fragmentContainer, homeFragment);
                fragmentTransaction.commit();
                break;
            case R.id.nav_event:
                Intent eventIntent = new Intent(MainActivity.this, EventListActivity.class);
                startActivity(eventIntent);
                break;
            case R.id.nav_message:
                Intent chatIntent = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(chatIntent);
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                Intent logoutIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(logoutIntent);
                finish();
                break;
            case R.id.nav_favorite:
                Intent favoriteIntent = new Intent(MainActivity.this, FavoriteActivity.class);
                startActivity(favoriteIntent);
                break;
            default:
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUser();
    }

    private void updateUser() {
        progress_bar.setVisibility(View.VISIBLE);
        FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();
        String email = firebaseAuth.getCurrentUser().getEmail();

        JsonPlaceHolder jsonPlaceHolder = ApiConfig.getApiConfig();

        Call<User> call = jsonPlaceHolder.getUser(email);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                progress_bar.setVisibility(View.GONE);
                User user = response.body();
                if (user != null) {
                    UserData.user = user;
                    if (user.getPhoto() != null)
                        Glide.with(MainActivity.this)
                                .load(ApiConfig.FINAL_URL + "profile/" + UserData.user.getPhoto())
                                .signature(new ObjectKey(String.valueOf(UserData.picCount)))
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
                        progress_bar_img.setVisibility(View.GONE);
                        if (UserData.user.getRole().equalsIgnoreCase("tenant"))
                            img_profile.setImageDrawable(getDrawable(R.drawable.ic_tenant));
                        else img_profile.setImageDrawable(getDrawable(R.drawable.ic_organizer));
                    }
                    tv_profile_name.setText(UserData.user.getName());
                    if (user.getRole().equalsIgnoreCase("tenant")) {
                        fab.setVisibility(View.GONE);
                        menu_fav.setVisible(true);
                    }
                    else {
                        fab.setVisibility(View.VISIBLE);
                        menu_fav.setVisible(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progress_bar.setVisibility(View.GONE);
                if (UserData.user.getName() != null) {
                    tv_profile_name.setText(UserData.user.getName());
                }
                Log.e("user", "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void onActionPerformed(Bundle bundle) {
        int actionPerformed = bundle.getInt(FragmentActionListener.ACTION_KEY);

        switch (actionPerformed) {
            case FragmentActionListener.ACTION_VALUE_CATEGORY_SELECTED: {
                Log.e("CATEGORY", String.valueOf(bundle.getInt(FragmentActionListener.KEY_CATEGORY_BAZAAR)));

                fragmentTransaction = fragmentManager.beginTransaction();
                SearchCategoryFragment searchCategoryFragment = new SearchCategoryFragment();
                searchCategoryFragment.setArguments(bundle);
//                searchCategoryFragment.setFragmentActionListener(this);

                fragmentTransaction.replace(R.id.fragmentContainer, searchCategoryFragment);
                fragmentTransaction.commit();
            }
            break;
            case FragmentActionListener.ACTION_VALUE_SEARCH_SELECTED: {
                Log.e("SEARCH", String.valueOf(bundle.getString(FragmentActionListener.KEY_SEARCHED_BAZAAR)));

                fragmentTransaction = fragmentManager.beginTransaction();
                SearchCategoryFragment searchCategoryFragment = new SearchCategoryFragment();
                searchCategoryFragment.setArguments(bundle);
//                searchCategoryFragment.setFragmentActionListener(this);

                fragmentTransaction.replace(R.id.fragmentContainer, searchCategoryFragment);
                fragmentTransaction.commit();
            }
            break;
            case FragmentActionListener.ACTION_VALUE_DETAIL_SELECTED: {
                Log.e("DETAIL", String.valueOf(ACTION_VALUE_DETAIL_SELECTED));
            }
            break;
            default: {

            }
        }
    }


}

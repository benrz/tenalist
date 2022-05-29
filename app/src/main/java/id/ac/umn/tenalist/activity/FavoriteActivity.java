package id.ac.umn.tenalist.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.ac.umn.tenalist.R;
import id.ac.umn.tenalist.adapter.EventListAdapter;
import id.ac.umn.tenalist.adapter.MessageAdapter;
import id.ac.umn.tenalist.model.BazaarDetail;
import id.ac.umn.tenalist.model.EventListModel;
import id.ac.umn.tenalist.model.SharedPreference;

public class FavoriteActivity extends AppCompatActivity {

    EventListAdapter eventListAdapter;
    ArrayList<EventListModel> bazaars;
    RecyclerView recyclerView;
    SharedPreference sharedPreference;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        recyclerView = findViewById(R.id.list_favorites);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        sharedPreference = new SharedPreference();
        bazaars = sharedPreference.getFavorites(getApplicationContext());

        if (bazaars == null) {
            Toast.makeText(getApplicationContext(), "No Favorite Bazaars", Toast.LENGTH_LONG).show();
        } else {
            eventListAdapter = new EventListAdapter(FavoriteActivity.this, bazaars);
            recyclerView.setAdapter(eventListAdapter);
        }
    }
}

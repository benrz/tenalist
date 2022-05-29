package id.ac.umn.tenalist.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

import static id.ac.umn.tenalist.UserData.user;

public class EventListActivity extends AppCompatActivity {

    RecyclerView eventlistrv;
    EventListAdapter eventListAdapter;
    Button newEvent;
    Integer userId;
    String userRole;
    Context c;
    private JsonPlaceHolder jsonPlaceHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        newEvent = findViewById(R.id.NewEvent);
        eventlistrv = findViewById(R.id.eventlist);
        eventlistrv.setLayoutManager(new LinearLayoutManager(this));
        eventlistrv.setHasFixedSize(true);

        userRole = UserData.user.getRole();
        if (userRole.equals("tenant")) {
            newEvent.setVisibility(View.GONE);
        }
        c = this;

        JsonPlaceHolder jsonPlaceHolder = ApiConfig.getApiConfig();

        Call<List<EventListModel>> userEventsCall = jsonPlaceHolder.getUserEvents(UserData.user.getId());
        userEventsCall.enqueue(new Callback<List<EventListModel>>() {
            @Override
            public void onResponse(Call<List<EventListModel>> call, Response<List<EventListModel>> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                Log.e("EVENT", "onResponse: "+response.body() );
                ArrayList<EventListModel> eventLists = (ArrayList<EventListModel>) response.body();

                eventListAdapter = new EventListAdapter(c, eventLists);
                eventlistrv.setAdapter(eventListAdapter);
                for (EventListModel eventList : eventLists) {
                    Log.d("EVENT ID", String.valueOf(eventList.getEventId()));
                    Log.d("BAZAAR NAME", eventList.getEventTitle());
                }
//                bazaarAdapter.setBazaarList(bazaarList); // Set data yang telah diGET kedalam adapter
            }

            @Override
            public void onFailure(Call<List<EventListModel>> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
            }
        });

        newEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newEvent = new Intent(EventListActivity.this, AddEventActivity.class);
                newEvent.putExtra("MODE", "ADD");
                startActivity(newEvent);
            }
        });
    }
}

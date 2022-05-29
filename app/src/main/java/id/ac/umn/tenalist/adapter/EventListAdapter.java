package id.ac.umn.tenalist.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import id.ac.umn.tenalist.ApiConfig;
import id.ac.umn.tenalist.R;
import id.ac.umn.tenalist.Holder.EventListHolder;
import id.ac.umn.tenalist.UserData;
import id.ac.umn.tenalist.activity.AddEventActivity;
import id.ac.umn.tenalist.activity.EventListActivity;
import id.ac.umn.tenalist.activity.FavoriteActivity;
import id.ac.umn.tenalist.model.EventListModel;
import id.ac.umn.tenalist.model.SharedPreference;

import static android.view.View.GONE;
import static id.ac.umn.tenalist.UserData.formattedDate;
import static id.ac.umn.tenalist.UserData.formattedTime;

public class EventListAdapter extends RecyclerView.Adapter<EventListHolder> {
    Context c;
    ArrayList<EventListModel> eventListModels;
    String userRole = UserData.user.getRole();
    SharedPreference sharedPreference;

    public EventListAdapter(Context c){
        this.c = c;
    }

    public EventListAdapter(Context c, ArrayList<EventListModel> eventListModels){
        this.c = c;
        this.eventListModels = eventListModels;
    }

    @NonNull
    @Override
    public EventListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent,false);
        return new EventListHolder(view);
    }

    public void setEventListModels(ArrayList<EventListModel> eventListModels) {
        this.eventListModels = eventListModels;
    }

    @Override
    public void onBindViewHolder(@NonNull final EventListHolder eventListHolder, int position) {
        final EventListModel eventList = eventListModels.get(position);

        eventListHolder.eventTitle.setText(eventList.getEventTitle());
        eventListHolder.eventPlace.setText(eventList.getEventPlace());

        String formatStartDate = formattedDate.format(eventListModels.get(position).getEventStartDate());
        String formatEndDate = formattedDate.format(eventListModels.get(position).getEventEndDate());
        String formatSTime = eventListModels.get(position).getEventStartTime().substring(0,5);
        String formatETime = eventListModels.get(position).getEventEndTime().substring(0,5);

        eventListHolder.eventDate.setText(formatStartDate + " - " + formatEndDate);
        eventListHolder.eventTime.setText(formatSTime + " - " + formatETime);

        Glide.with(eventListHolder.itemView.getContext())
                .load(ApiConfig.FINAL_URL + "poster/" + eventList.getEventImage())
                .apply(new RequestOptions().override(100, 120))
                .into(eventListHolder.eventImage);

        if(eventListModels.get(position).getEventCountDown()>0){
            eventListHolder.eventCountDown.setText(String.format("D-%d", eventList.getEventCountDown()));
        }else if(eventListModels.get(position).getEventCountDown()==0){
            eventListHolder.eventCountDown.setText("D-DAY");
        }else if(eventListModels.get(position).getEventCountDown() == -1){
            eventListHolder.eventCountDown.setVisibility(GONE);
        }else{
            eventListHolder.eventCountDown.setText("FIN");
        }

        if(eventListModels.get(position).getEventStatus().equals("none")) {
            eventListHolder.eventStatus.setVisibility(GONE);
        }else {
            eventListHolder.eventStatus.setText(eventListModels.get(position).getEventStatus());
        }

        //Toast.makeText(c, "test" + c, Toast.LENGTH_LONG).show();
        if(c instanceof FavoriteActivity) {
            if (checkFavoriteItem(eventList)) {
                eventListHolder.buttonFavorite.setImageResource(R.drawable.heart_red);
                eventListHolder.buttonFavorite.setTag("red");
            } else {
                eventListHolder.buttonFavorite.setImageResource(R.drawable.heart_grey);
                eventListHolder.buttonFavorite.setTag("grey");
            }

            eventListHolder.buttonFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sharedPreference.removeFavorite(c, eventList);
                    eventListModels.remove(eventList);
                    notifyDataSetChanged();

                    eventListHolder.buttonFavorite.setImageResource(R.drawable.heart_grey);
                    eventListHolder.buttonFavorite.setTag("grey");
                    Toast.makeText(c,
                            "Remove from Favorites",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        if(userRole.equals("organizer")){
            eventListHolder.cvEventList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("CLICKED", eventListHolder.eventTitle.getText().toString());

                    Intent newEvent = new Intent(c, AddEventActivity.class);;
                    int bazaarId = eventList.getEventId();
                    newEvent.putExtra("bazaarId", bazaarId);
                    newEvent.putExtra("MODE", "UPDATE");
                    c.startActivity(newEvent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return eventListModels.size();
    }

    public boolean checkFavoriteItem(EventListModel mEvent) {
        boolean check = false;
        sharedPreference = new SharedPreference();
        List<EventListModel> favorites = sharedPreference.getFavorites(c);
        if (favorites != null) {
            for (EventListModel favorite : favorites) {
                if (favorite.getEventId() == mEvent.getEventId()) {
                    check = true;
                    break;
                }
            }
        }
        return check;
    }
}

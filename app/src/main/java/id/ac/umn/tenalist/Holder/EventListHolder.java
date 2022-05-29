package id.ac.umn.tenalist.Holder;

import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import id.ac.umn.tenalist.R;

public class EventListHolder extends RecyclerView.ViewHolder {

    public ImageView eventImage, buttonFavorite;
    public TextView eventTitle, eventPlace, eventDate, eventTime, eventCountDown, eventStatus;
    public CardView cvEventList;

    public EventListHolder(@NonNull View itemView) {
        super(itemView);

        eventImage = itemView.findViewById(R.id.EventImage);
        eventTitle = itemView.findViewById(R.id.EventTitle);
        eventPlace = itemView.findViewById(R.id.EventPlace);
        eventDate = itemView.findViewById(R.id.EventDate);
        eventTime = itemView.findViewById(R.id.EventTime);
        eventCountDown = itemView.findViewById(R.id.EventCountDown);
        eventStatus = itemView.findViewById(R.id.EventStatus);
        buttonFavorite = itemView.findViewById(R.id.btnFavorite);
        cvEventList = itemView.findViewById(R.id.cvEventList);
    }
}

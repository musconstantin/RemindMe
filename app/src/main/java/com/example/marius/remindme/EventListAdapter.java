package com.example.marius.remindme;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by marius on 24.12.2016.
 */

public class EventListAdapter extends ArrayAdapter<Event> {
    public EventListAdapter(Context context, int resource, List<Event> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null){
            LayoutInflater inflater;
            inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.event_list_row, null);
        }
        final Event currentEvent = getItem(position);
        if(currentEvent != null){
            LinearLayout eventClick = (LinearLayout) v.findViewById(R.id.rowEventClick);
            TextView eventTitle = (TextView) v.findViewById(R.id.rowEventTitle);
            TextView eventDescription = (TextView) v.findViewById(R.id.rowEventDescription);
            TextView eventFrequency = (TextView) v.findViewById(R.id.rowFrequency);
            TextView eventId = (TextView) v.findViewById(R.id.rowEventId);

            if(eventTitle != null){
                eventTitle.setText(currentEvent.getTitle());
            }
            if(eventDescription != null){
                eventDescription.setText(currentEvent.getDescription());
            }
            if(eventId != null){
                eventId.setText(currentEvent.getId() + "");
            }
            if(eventFrequency != null){
                eventFrequency.setText(getContext().getResources().getString(R.string.label_rowFrequency) + " " + currentEvent.getFrequency() + " " + currentEvent.getFrequencyType());
            }
            eventClick.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    Intent eventAdd = new Intent(getContext(), EventAdd.class);
                    eventAdd.putExtra("currentEventId", currentEvent.getId());
                    getContext().startActivity(eventAdd);
                }
            });
        }
        return v;
    }
}

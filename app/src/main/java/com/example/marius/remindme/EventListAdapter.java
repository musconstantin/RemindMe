package com.example.marius.remindme;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.List;
import java.util.Objects;

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
            TextView eventTitle = (TextView) v.findViewById(R.id.rowEventTitle);
            TextView eventDescription = (TextView) v.findViewById(R.id.rowEventDescription);
            TextView eventId = (TextView) v.findViewById(R.id.rowEventId);
            ToggleButton eventToggle = (ToggleButton) v.findViewById(R.id.rowEventToggle);

            if(eventTitle != null){
                eventTitle.setText(currentEvent.getTitle());
            }
            if(eventDescription != null){
                eventDescription.setText(currentEvent.getDescription());
            }
            if(eventId != null){
                eventId.setText(currentEvent.getId() + "");
            }
            if(eventToggle != null){
                if(currentEvent.getActive().equals("1")){
                    eventToggle.setText(R.string.toggle_on);
                    eventToggle.setChecked(true);
                }else if(currentEvent.getActive().equals("0")){
                    eventToggle.setText(R.string.toggle_off);
                    eventToggle.setChecked(false);
                }
            }

            eventToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    DBHelper db = new DBHelper(getContext());
                    if(isChecked){
                        Toast.makeText(getContext(),
                                "You will now receive notifications for this event.", Toast.LENGTH_SHORT).show();
                        db.setActiveEvent(currentEvent.getId(), "1");
                    }else{
                        Toast.makeText(getContext(),
                                "You will not receive notifications anymore for this event.", Toast.LENGTH_SHORT).show();
                        db.setActiveEvent(currentEvent.getId(), "0");
                    }
                }
            });
        }
        return v;
    }
}

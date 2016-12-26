package com.example.marius.remindme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.List;

public class EventList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        DBHelper db = new DBHelper(this);
        List<Event> events = db.getEventsForInterface();
        EventListAdapter adapter = new EventListAdapter(this, R.layout.event_list_row, events);
        ListView eventsList = (ListView)findViewById(R.id.eventsList);
        eventsList.setEmptyView(findViewById(R.id.emptyList));
        eventsList.setAdapter(adapter);
    }

    public void newEvent(View view) {
        Intent addEvent = new Intent(EventList.this, EventAdd.class);
        EventList.this.startActivity(addEvent);
    }
}

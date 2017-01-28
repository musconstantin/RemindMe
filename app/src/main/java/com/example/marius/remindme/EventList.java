package com.example.marius.remindme;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.List;

public class EventList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.eventListToolbar);
        setSupportActionBar(toolbar);

        DBHelper db = new DBHelper(this);
        List<Event> events = db.getEventsForInterface();
        EventListAdapter adapter = new EventListAdapter(this, R.layout.event_list_row, events);
        ListView eventsList = (ListView)findViewById(R.id.eventsList);
        eventsList.setEmptyView(findViewById(R.id.emptyList));
        eventsList.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_list_items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_event:
                Intent addEvent = new Intent(EventList.this, EventAdd.class);
                EventList.this.startActivity(addEvent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

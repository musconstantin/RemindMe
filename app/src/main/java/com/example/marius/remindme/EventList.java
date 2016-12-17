package com.example.marius.remindme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class EventList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
    }

    public void newEvent(View view) {
        Intent addEvent = new Intent(EventList.this, AddEvent.class);
        EventList.this.startActivity(addEvent);
    }
}

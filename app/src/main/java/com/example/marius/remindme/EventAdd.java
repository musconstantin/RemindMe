package com.example.marius.remindme;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class EventAdd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        final String[] hourArray = this.getResources().getStringArray(R.array.hours);
        final String[] minuteArray = this.getResources().getStringArray(R.array.minutes);
        final String[] dayArray = this.getResources().getStringArray(R.array.days);
        final String[] frequencyArray = this.getResources().getStringArray(R.array.frequency);

        final Spinner timeSpinner = (Spinner) findViewById(R.id.timeSpinner);
        Spinner frequencySpinner = (Spinner) findViewById(R.id.frequencySpinner);
        EditText title = (EditText) findViewById(R.id.eventTitle);
        EditText description = (EditText) findViewById(R.id.eventDescription);

        Toolbar toolbar = (Toolbar)findViewById(R.id.addEventToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent eventList = getIntent();

        addItemsToSpinner(frequencySpinner, frequencyArray);
        frequencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        addItemsToSpinner(timeSpinner, dayArray);
                        break;
                    case 1:
                        addItemsToSpinner(timeSpinner, hourArray);
                        break;
                    case 2:
                        addItemsToSpinner(timeSpinner, minuteArray);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                timeSpinner.setAdapter(null);
            }
        });
        if (eventList.getExtras() == null) {
            frequencySpinner.setSelection(2);
        } else {
            DBHelper db = new DBHelper(EventAdd.this);
            Integer currentEventId = eventList.getExtras().getInt("currentEventId");
            Log.d("event id", "" + currentEventId);
            Cursor currentRow = db.getRow(currentEventId);
            currentRow.moveToFirst();

            String currentRowTitle = currentRow.getString(1);
            String currentRowDescription = currentRow.getString(2);
            String currentRowFrequency = currentRow.getString(3);
            String currentRowFrequencyType = currentRow.getString(4);

            title.setText(currentRowTitle);
            description.setText(currentRowDescription);
            frequencySpinner.setSelection(EventGenerics.getFrequencyIndex(currentRowFrequencyType));
            switch (currentRowFrequencyType){
                case "days":
                    timeSpinner.setSelection(EventGenerics.getDaysIndex(currentRowFrequency));
                    break;
                case "hours":
                    timeSpinner.setSelection(EventGenerics.getHoursIndex(currentRowFrequency));
                    break;
                case "minutes":
                    timeSpinner.setSelection(EventGenerics.getMinutesIndex(currentRowFrequency));
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.delete_event:
                DBHelper db = new DBHelper(EventAdd.this);
                db.deleteEvent(getIntent().getExtras().getInt("currentEventId"));
                Intent eventsList = new Intent(EventAdd.this, EventList.class);
                startActivity(eventsList);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void saveEvent(View view) {
        EditText title = (EditText) findViewById(R.id.eventTitle);
        EditText description = (EditText) findViewById(R.id.eventDescription);
        Spinner frequencyType = (Spinner) findViewById(R.id.frequencySpinner);
        Spinner frequency = (Spinner) findViewById(R.id.timeSpinner);
        String currentTime = EventGenerics.dateFormat.format(Calendar.getInstance().getTime());

        DBHelper dbHelper = new DBHelper(this);
        if(getIntent().getExtras() == null) {
            dbHelper.insertEvent(title.getText().toString(),
                    description.getText().toString(),
                    frequency.getSelectedItem().toString(),
                    frequencyType.getSelectedItem().toString(),
                    EventGenerics.activeDefault,
                    currentTime,
                    EventGenerics.calculateNextTimeAlert(frequencyType.getSelectedItemPosition(), frequency.getSelectedItem().toString()));
        }else{
            dbHelper.updateEvent(getIntent().getExtras().getInt("currentEventId"),
                    title.getText().toString(),
                    description.getText().toString(),
                    frequency.getSelectedItem().toString(),
                    frequencyType.getSelectedItem().toString(),
                    EventGenerics.activeDefault,
                    currentTime,
                    EventGenerics.calculateNextTimeAlert(frequencyType.getSelectedItemPosition(), frequency.getSelectedItem().toString()));
        }
        Intent eventList = new Intent(EventAdd.this, EventList.class);
        EventAdd.this.startActivity(eventList);
    }

    public void addItemsToSpinner(Spinner spinner, String[] entries) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, entries);
        spinner.setAdapter(arrayAdapter);
    }
}

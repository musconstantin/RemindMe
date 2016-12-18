package com.example.marius.remindme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class AddEvent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        final String[] hourArray = this.getResources().getStringArray(R.array.hours);
        final String[] minuteArray = this.getResources().getStringArray(R.array.minutes);
        final String[] dayArray = this.getResources().getStringArray(R.array.days);
        final String[] frequencyArray = this.getResources().getStringArray(R.array.frequency);

        final Spinner timeSpinner = (Spinner)findViewById(R.id.timeSpinner);
        Spinner frequencySpinner = (Spinner)findViewById(R.id.frequencySpinner);

        addItemsToSpinner(frequencySpinner, frequencyArray);

        frequencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
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
        frequencySpinner.setSelection(2);
    }

    public void saveEvent(View view) {
        EditText title = (EditText)findViewById(R.id.eventTitle);
        EditText description = (EditText)findViewById(R.id.eventDescription);
        Spinner frequencyType = (Spinner)findViewById(R.id.frequencySpinner);
        Spinner frequency = (Spinner)findViewById(R.id.timeSpinner);
        String currentTime = EventGenerics.dateFormat.format(Calendar.getInstance().getTime());

        Log.d("freq spinner: ", "" + frequencyType.getSelectedItemPosition());
        Log.d("freq spinner value: ", "" + frequencyType.getSelectedItem());

        DBHelper dbHelper = new DBHelper(this);
        dbHelper.insertEvent(title.getText().toString(),
                description.getText().toString(),
                frequency.getSelectedItem().toString(),
                frequencyType.getSelectedItem().toString(),
                EventGenerics.activeDefault,
                currentTime,
                EventGenerics.calculateNextTimeAlert(frequencyType.getSelectedItemPosition(), frequency.getSelectedItem().toString()));
        Toast.makeText(this, "" + dbHelper.rowCount(), Toast.LENGTH_LONG);
        Intent eventList = new Intent(AddEvent.this, EventList.class);
        AddEvent.this.startActivity(eventList);
    }

    public void cancelEvent(View view) {
        Intent eventList = new Intent(AddEvent.this, EventList.class);
        AddEvent.this.startActivity(eventList);
    }

    public void addItemsToSpinner(Spinner spinner, String[] entries){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, entries);
        spinner.setAdapter(arrayAdapter);
    }
}

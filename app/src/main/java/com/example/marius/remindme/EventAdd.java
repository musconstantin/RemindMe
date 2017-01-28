package com.example.marius.remindme;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
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
    AlarmManager am;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        final String[] frequencyArray = this.getResources().getStringArray(R.array.frequency);

        Spinner frequencySpinner = (Spinner) findViewById(R.id.frequencySpinner);
        EditText title = (EditText) findViewById(R.id.eventTitle);
        EditText description = (EditText) findViewById(R.id.eventDescription);
        EditText time = (EditText) findViewById(R.id.timeText);

        am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent eventList = getIntent();

        Toolbar toolbar = (Toolbar) findViewById(R.id.addEventToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        addItemsToSpinner(frequencySpinner, frequencyArray);

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
            time.setText(currentRowFrequency);
            frequencySpinner.setSelection(EventGenerics.getFrequencyIndex(currentRowFrequencyType));

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getIntent().getExtras() != null) {
            getMenuInflater().inflate(R.menu.toolbar_items, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_event:
                new AlertDialog.Builder(this)
                        .setTitle(this.getResources().getString(R.string.delete_dialog_title))
                        .setMessage(this.getResources().getString(R.string.delete_dialog_message))
                        .setPositiveButton(this.getResources().getString(R.string.dialog_positive_button), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DBHelper db = new DBHelper(EventAdd.this);
                                db.deleteEvent(getIntent().getExtras().getInt("currentEventId"));
                                cancelAlarm(getIntent().getExtras().getInt("currentEventId"));
                                Intent eventsList = new Intent(EventAdd.this, EventList.class);
                                startActivity(eventsList);
                            }
                        })
                        .setNegativeButton(this.getResources().getString(R.string.dialog_negative_button), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void saveEvent(View view) {
        EditText title = (EditText) findViewById(R.id.eventTitle);
        EditText description = (EditText) findViewById(R.id.eventDescription);
        Spinner frequencyType = (Spinner) findViewById(R.id.frequencySpinner);
        EditText time = (EditText) findViewById(R.id.timeText);
        String currentTime = EventGenerics.dateFormat.format(Calendar.getInstance().getTime());

        DBHelper dbHelper = new DBHelper(this);
        if (title.getText().toString().length() == 0) {
            title.setError(this.getResources().getString(R.string.title_empty_error));
        } else if (description.getText().toString().length() == 0) {
            description.setError(this.getResources().getString(R.string.description_empty_error));
        } else if (time.getText().toString().length() == 0) {
            time.setError(this.getResources().getString(R.string.time_empty_error));
        } else if (Integer.parseInt(time.getText().toString()) < 1) {
            time.setError(this.getResources().getString(R.string.time_lesserThan_1));
        } else {
            if (getIntent().getExtras() == null) {
                long eventId = dbHelper.insertEvent(title.getText().toString(),
                        description.getText().toString(),
                        time.getText().toString(),
                        frequencyType.getSelectedItem().toString(),
                        currentTime,
                        EventGenerics.calculateNextTimeAlert(frequencyType.getSelectedItemPosition(), time.getText().toString()));
                setAlarm(frequencyType.getSelectedItemPosition(), time.getText().toString(), (int)eventId);
            }else{
                dbHelper.updateEvent(getIntent().getExtras().getInt("currentEventId"),
                        title.getText().toString(),
                        description.getText().toString(),
                        time.getText().toString(),
                        frequencyType.getSelectedItem().toString(),
                        currentTime,
                        EventGenerics.calculateNextTimeAlert(frequencyType.getSelectedItemPosition(), time.getText().toString()));
                cancelAlarm(getIntent().getExtras().getInt("currentEventId"));
                setAlarm(frequencyType.getSelectedItemPosition(), time.getText().toString(), getIntent().getExtras().getInt("currentEventId"));
            }
            Intent eventList = new Intent(EventAdd.this, EventList.class);
            EventAdd.this.startActivity(eventList);
        }
    }

    public void addItemsToSpinner(Spinner spinner, String[] entries) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, entries);
        spinner.setAdapter(arrayAdapter);
    }

    public void setAlarm(int frequencyType, String frequency, int eventId){
        Intent notificationReceiver = new Intent(this, NotificationReceiver.class);
        notificationReceiver.putExtra("id", eventId);
        PendingIntent pendingNotificationReceiver = PendingIntent.getBroadcast(this, eventId, notificationReceiver, PendingIntent.FLAG_CANCEL_CURRENT);
        am.setRepeating(AlarmManager.RTC_WAKEUP,
                EventGenerics.getNextAlertTime(frequencyType, frequency),
                EventGenerics.getFrequencyInMillis(frequencyType, frequency),
                pendingNotificationReceiver);
    }
    public void cancelAlarm(int eventId){
        Intent notificationReceiver = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingNotificationReceiver = PendingIntent.getBroadcast(this, eventId, notificationReceiver, PendingIntent.FLAG_CANCEL_CURRENT);
        am.cancel(pendingNotificationReceiver);
        pendingNotificationReceiver.cancel();
    }
}

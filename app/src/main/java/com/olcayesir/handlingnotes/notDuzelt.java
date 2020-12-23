package com.olcayesir.handlingnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class notDuzelt extends AppCompatActivity implements View.OnClickListener{
    private int notificationId = 1;
    DatePickerDialog picker;
    TimePickerDialog tpicker;
    EditText tvw;
    EditText time;
    int hours;
    int minutes;
    String ilkB;
    int day;
    int month;
    int year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_duzelt);

        tvw=(EditText)findViewById(R.id.txtDate);
        tvw.setInputType(InputType.TYPE_NULL);
        tvw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                day = cldr.get(Calendar.DAY_OF_MONTH);
                month = cldr.get(Calendar.MONTH);
                year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(notDuzelt.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tvw.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        time=(EditText)findViewById(R.id.txtTime);
        time.setInputType(InputType.TYPE_NULL);
        time.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final Calendar cldr = Calendar.getInstance();
                hours = cldr.get(Calendar.HOUR_OF_DAY);
                minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                tpicker = new TimePickerDialog(notDuzelt.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                time.setText(sHour + ":" + sMinute);
                            }
                        }, hours, minutes, true);
                tpicker.show();
            }

        });
        findViewById(R.id.btnKaydet).setOnClickListener(this);


        Intent intent = getIntent();
        EditText txtBaslik = (EditText) findViewById(R.id.txtBaslik);
        EditText txtNot = (EditText) findViewById(R.id.txtNot);

        txtBaslik.setText(intent.getStringExtra("Baslik"));
        ilkB = txtBaslik.getText().toString();
        txtNot.setText(intent.getStringExtra("Not"));
        time.setText(intent.getStringExtra("Tarih"));
        tvw.setText(intent.getStringExtra("Saat"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnKaydet:
                Switch alarm = (Switch)findViewById(R.id.switch1);
                EditText baslik = (EditText) findViewById(R.id.txtBaslik);
                EditText not = (EditText) findViewById(R.id.txtNot);
                EditText saat = (EditText) findViewById(R.id.txtTime);
                EditText tarih = (EditText) findViewById(R.id.txtDate);

                SQLiteDatabase database = this.openOrCreateDatabase("Notes",MODE_PRIVATE,null);
                database.execSQL("CREATE TABLE IF NOT EXISTS Notes (baslik VARCHAR, note VARCHAR, saat VARCHAR,tarih VARCHAR)");


                if (SqliControl(baslik.getText().toString()) && SqliControl(not.getText().toString()) && IsEmpty(baslik.getText().toString()) && IsEmpty(not.getText().toString()) && IsEmpty(saat.getText().toString()) && IsEmpty(tarih.getText().toString()))
                {
                    database.execSQL("UPDATE Notes SET baslik='" + baslik.getText() + "',"+"note='"+not.getText()+"',"+"saat='"+saat.getText()+"',"+"tarih='"+tarih.getText()+"' WHERE baslik='"+ilkB+"'");
                    Toast.makeText(getApplicationContext(),"Düzeltildi",Toast.LENGTH_LONG).show();


                    if(alarm.isChecked())
                    {
                        Intent intent = new Intent(notDuzelt.this, AlarmReceiver.class);
                        intent.putExtra("notificationId", notificationId);
                        intent.putExtra("message", baslik.getText().toString() + " " + not.getText().toString());

                        // PendingIntent
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                notDuzelt.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT
                        );
                        // AlarmManager
                        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

                        // Create time.
                        String hour[] = saat.getText().toString().split(":");
                        int s = Integer.parseInt(hour[0]);
                        int m = Integer.parseInt(hour[1]);

                        Calendar startTime = Calendar.getInstance();
                        startTime.set(Calendar.DAY_OF_MONTH , day);
                        startTime.set(Calendar.MONTH,month);
                        startTime.set(Calendar.YEAR,year);
                        startTime.set(Calendar.HOUR_OF_DAY, s);
                        startTime.set(Calendar.MINUTE, m);
                        startTime.set(Calendar.SECOND, 0);
                        long alarmStartTime = startTime.getTimeInMillis();

                        // Set Alarm
                        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmStartTime, pendingIntent);

                    }

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Düzeltilemedi",Toast.LENGTH_LONG).show();
                }

                Intent intent = new Intent(notDuzelt.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;

        }
    }

    public boolean SqliControl(String girdi)
    {
        for (int i=0; i<girdi.length();i++)
        {
            if (girdi.charAt(i) == 39){
                return false;
            }
        }

        return true;
    }
    public boolean IsEmpty(String girdi){

        if (girdi.isEmpty()){
            return false;
        }
        else
            return true;
    }
}
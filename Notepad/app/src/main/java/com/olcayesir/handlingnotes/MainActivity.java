package com.olcayesir.handlingnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private static final int WRITE_EXTERNAL_STORAGE = 100;
    NotListAdapter adapter;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ArrayList<Not> notList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);

        checkPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE);


        database = this.openOrCreateDatabase("Notes",MODE_PRIVATE,null);
        database.execSQL("CREATE TABLE IF NOT EXISTS Notes (baslik VARCHAR, note VARCHAR, saat VARCHAR,tarih VARCHAR)");

        Cursor cursor = database.rawQuery("SELECT * FROM Notes",null);
        int iTitle = cursor.getColumnIndex("baslik");
        int iNote = cursor.getColumnIndex("note");
        int iSaat = cursor.getColumnIndex("saat");
        int iTarih = cursor.getColumnIndex("tarih");
        if (cursor!=null)
        {
            while (cursor.moveToNext()){
                Not m = new Not(cursor.getString(iTitle),cursor.getString(iNote),cursor.getString(iSaat),cursor.getString(iTarih));
                notList.add(m);
            }

        }


        adapter = new NotListAdapter(this,notList);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setClickable(true);
        recyclerView.setAdapter(adapter);

        Intent intent = getIntent();
        String title = intent.getStringExtra("bas");
        if (title != null){
            database.execSQL("DELETE FROM Notes WHERE baslik='"+title+"'");
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }

    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (   item.getItemId()     )
        {
            case R.id.item1:
                Intent intent = new Intent(MainActivity.this, notekle.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu,menu);
        return true;
    }

    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] { permission },
                    requestCode);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super
                .onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);

        if (requestCode == WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }

        }
    }


}
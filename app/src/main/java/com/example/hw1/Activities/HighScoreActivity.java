package com.example.hw1.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.hw1.CallBacks.CallBack_List;
import com.example.hw1.CallBacks.CallBack_Map;
import com.example.hw1.Fragments.ListFragment;
import com.example.hw1.Fragments.MapFragment;
import com.example.hw1.Database.MSPv3;
import com.example.hw1.Models.Record;
import com.example.hw1.Database.MyDB;
import com.example.hw1.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

public class HighScoreActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ListFragment listFragment;
    private MapFragment mapFragment;
    private TextView info;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        info = findViewById(R.id.info);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("myDB");
        listFragment = new ListFragment();
        listFragment.setArguments(bundle);
        listFragment.setCallBackList(callBack_List);
        getSupportFragmentManager().beginTransaction().add(R.id.list_frame, listFragment).commit();

        mapFragment = new MapFragment();
        mapFragment.setCallBackMap(callBack_map);
        getSupportFragmentManager().beginTransaction().add(R.id.map_frame, mapFragment).commit();

    }

    CallBack_List callBack_List = new CallBack_List() {
        @Override
        public void setMainTitle(String str) {
            info.setText(str);
        }

        @Override
        public void rowSelected(int i) {
            String fromJSON = MSPv3.getInstance(getApplicationContext()).getStringSP("MY_DB","");
            MyDB myDB = new Gson().fromJson(fromJSON,MyDB.class);
            if (i < myDB.getRecords().size()) {
                Record record = myDB.getRecords().get(i);
                callBack_map.locationSelected(record);
            }
        }
    };

    CallBack_Map callBack_map = new CallBack_Map() {
        @Override
        public void mapClicked(double lat, double lon) {
        }

        @Override
        public void locationSelected(Record record) {
            mapFragment.onClicked(record);
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng mark = new LatLng(32.104236455127015, 34.87987851707526);
        map.addMarker(new MarkerOptions().position(mark).title("I am here"));
        map.moveCamera(CameraUpdateFactory.newLatLng(mark));
    }
}
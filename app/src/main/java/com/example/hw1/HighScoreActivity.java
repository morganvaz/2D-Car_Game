package com.example.hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.hw1.CallBacks.CallBack_List;
import com.example.hw1.CallBacks.CallBack_Map;
import com.example.hw1.Fragments.ListFragment;
import com.example.hw1.Fragments.MapFragment;

public class HighScoreActivity extends AppCompatActivity {

    private ListFragment listFragment;
    private MapFragment mapFragment;
    private TextView info;
    //private GoogleMap map;

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
//        mapFragment.setCallBackMap(callBack_map);
        getSupportFragmentManager().beginTransaction().add(R.id.map_frame, mapFragment).commit();



    }

    CallBack_List callBack_List = new CallBack_List() {
        @Override
        public void setMainTitle(String str) {
            info.setText(str);
        }
    };

    CallBack_Map callBack_map = new CallBack_Map() {
        @Override
        public void mapClicked(double lat, double lon) {
        }
    };

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        map = googleMap;
//        LatLng mark = new LatLng(32.104236455127015, 34.87987851707526);
//        map.addMarker(new MarkerOptions().position(mark).title("I am here"));
//        map.moveCamera(CameraUpdateFactory.newLatLng(mark));
//    }
}
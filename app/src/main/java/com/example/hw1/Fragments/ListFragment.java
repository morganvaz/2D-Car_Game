package com.example.hw1.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hw1.CallBacks.CallBack_List;
import com.example.hw1.MSPv3;
import com.example.hw1.MyDB;
import com.example.hw1.R;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

public class ListFragment extends Fragment {
    private MyDB myDB;
    private CallBack_List callBackList;
    private MaterialTextView[] records = new MaterialTextView[10];

    public void setCallBackList(CallBack_List callBackList) {
        this.callBackList = callBackList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);

        findViews(view);
        initViews();
        String fromJSON = MSPv3.getInstance(getContext().getApplicationContext()).getStringSP("MY_DB","");
        myDB = new Gson().fromJson(fromJSON,MyDB.class);
        if(myDB == null)
            myDB = new MyDB();
        myDB = new Gson().fromJson(fromJSON, MyDB.class);
        for (int i = 0; i < records.length; i++)
            if (i < myDB.getRecords().size())
                records[i].setText(myDB.getRecords().get(i).toString());
        return view;
    }


    private void initViews() {
        for (int i = 0; i < records.length; i++) {

            final int finI = i;
            records[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBackList.rowSelected(finI);
                    if (finI < myDB.getRecords().size())        // for debugging
                        Log.d("myDB", " " + myDB.getRecords().get(finI).getScore());
                }
            });
        }
    }



    private void findViews(View view) {
        records[0] = view.findViewById(R.id.list_LBL_record1);
        records[1] = view.findViewById(R.id.list_LBL_record2);
        records[2] = view.findViewById(R.id.list_LBL_record3);
        records[3] = view.findViewById(R.id.list_LBL_record4);
        records[4] = view.findViewById(R.id.list_LBL_record5);
        records[5] = view.findViewById(R.id.list_LBL_record6);
        records[6] = view.findViewById(R.id.list_LBL_record7);
        records[7] = view.findViewById(R.id.list_LBL_record8);
        records[8] = view.findViewById(R.id.list_LBL_record9);
        records[9] = view.findViewById(R.id.list_LBL_record10);
    }
}
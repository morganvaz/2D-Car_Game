package com.example.hw1.Database;

import com.example.hw1.Models.Record;

import java.util.ArrayList;
import java.util.Collections;

public class MyDB {
    private ArrayList<Record> records = new ArrayList<>();

    public MyDB() {    }

    public ArrayList<Record> getRecords() {
        return records;
    }

    public MyDB setRecords(ArrayList<Record> records){
        this.records = records;
        return this;
    }

    public void sortRecords(){
        Collections.sort(records);
    }
}
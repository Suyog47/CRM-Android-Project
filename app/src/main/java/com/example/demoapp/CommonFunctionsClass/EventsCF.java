package com.example.demoapp.CommonFunctionsClass;

import android.content.Context;
import android.database.Cursor;
import android.widget.ArrayAdapter;

import com.example.demoapp.SqlliteDBClasses.EventSqlliteDbService;

import java.util.ArrayList;
import java.util.List;

public class EventsCF {

    //Function to return year adapter
    public ArrayAdapter setYears(Context context, Cursor res){
        List<String> yr = new ArrayList<String>();
        if(res.getCount() == 0){ yr.add(""); }
        else{
            while(res.moveToNext()){ yr.add(res.getString(0));}
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, yr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return dataAdapter;
    }


    //Function to return date adapter
    public ArrayAdapter setDates(Context context, EventSqlliteDbService db, String year, String type){
        List<String> d = new ArrayList<>();
        Cursor res;
        if(year != ""){
            if(type == "normal"){ res = db.getDates(Integer.parseInt(year)); }
            else{ res = db.getSweetDates(Integer.parseInt(year)); }

            if(res.getCount() == 0){ d.add(""); }
            else {
                while (res.moveToNext()) {
                    d.add(res.getString(1));
                }
            }
        }
        else{
            d.add("");
        }
        ArrayAdapter<String> da = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, d);
        da.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return da;
    }
}

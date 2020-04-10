package com.example.urakdo;

import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ToDoHelper extends AppCompatActivity {
    ForNewDB fornewdb;
    ListView listview;
    EditText edittext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolayout);
        listview = (ListView) findViewById(R.id.todolist);
        edittext = (EditText) findViewById(R.id.newtodo);
        fornewdb = new ForNewDB(this);
        final ArrayAdapter<String> adapter;
        edittext.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                    if (keyCode == KeyEvent.KEYCODE_ENTER){
                        fornewdb.Add(edittext.getText().toString());
                        edittext.setText("");
                        return true;
                    }
                return false;
            }
        });
        populateListView();
    }
    public void populateListView() {
        //get the data and append to a list
        Cursor data = fornewdb.getData();

        ArrayList<String> listData = new ArrayList<>();
        listData.clear();
        while(data.moveToNext()) {
            listData.add( data.getString(1) );
        }
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, listData);
        listview.setAdapter(adapter);
    }
}

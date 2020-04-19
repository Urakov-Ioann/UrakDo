package com.example.urakdo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;

public class ToDoHelper extends AppCompatActivity {
    ForNewDB fornewdb;
    SwipeMenuListView listview, list;
    EditText edittext;
    ArrayList<String> listData;
    ArrayList<String> doneList;
    final Context context = this;
    public static int theme;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(theme == 1){
            setTheme(R.style.darktheme);
        }
        else setTheme(R.style.AppTheme);
        setContentView(R.layout.todolayout);
        listview = (SwipeMenuListView) findViewById(R.id.todolist);
        list = (SwipeMenuListView) findViewById(R.id.tododone);
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
                        populateListView();
                        return true;
                    }
                return false;
            }
        });
        SwipeMenuCreator creator = new SwipeMenuCreator(){

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem editItem = new SwipeMenuItem(getApplicationContext());
                editItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                editItem.setWidth(170);
                editItem.setIcon(R.drawable.ic_change);
                menu.addMenuItem(editItem);
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                deleteItem.setWidth(170);
                deleteItem.setIcon(R.drawable.ic_delete);
                menu.addMenuItem(deleteItem);
            }
        };
        listview.setMenuCreator(creator);
        listview.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index){
                    case 0:
                        LayoutInflater li = LayoutInflater.from(context);
                        View vview = li.inflate(R.layout.add_goal, null);
                        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
                        mDialogBuilder.setView(vview);
                        final EditText userInput = (EditText) vview.findViewById(R.id.input_text);
                        userInput.setTextColor(Color.WHITE);
                        mDialogBuilder
                                .setCancelable(false)
                                .setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                final char dm = (char) 34;
                                                String name = dm + listData.get(position) + dm;
                                                fornewdb.Delete(name);
                                                String name_db = userInput.getText().toString();
                                                name_db.replace(" ", "_");
                                                fornewdb.Add(name_db);
                                                populateListView();
                                            }
                                        })
                                .setNegativeButton("Cancel",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alertDialog = mDialogBuilder.create();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getColor(R.color.colorPrimary)));
                        }
                        alertDialog.show();
                        break;
                    case 1:
                        char f = (char) 34;
                        fornewdb.Delete(f+listData.get(position)+f);
                        populateListView();
                        break;
                }
                return false;
            }
        });
        list.setMenuCreator(creator);
        list.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index){
                    case 0:
                        LayoutInflater li = LayoutInflater.from(context);
                        View vview = li.inflate(R.layout.add_goal, null);
                        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
                        mDialogBuilder.setView(vview);
                        final EditText userInput = (EditText) vview.findViewById(R.id.input_text);
                        userInput.setTextColor(Color.WHITE);
                        mDialogBuilder
                                .setCancelable(false)
                                .setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                final char dm = (char) 34;
                                                String name = dm + doneList.get(position) + dm;
                                                fornewdb.Delete(name);
                                                String name_db = userInput.getText().toString();
                                                name_db.replace(" ", "_");
                                                fornewdb.Add1(name_db);
                                                populateListView();
                                            }
                                        })
                                .setNegativeButton("Cancel",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alertDialog = mDialogBuilder.create();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getColor(R.color.colorG)));
                        }
                        alertDialog.show();
                        break;
                    case 1:
                        char f = (char) 34;
                        fornewdb.Delete(f+doneList.get(position)+f);
                        populateListView();
                        break;
                }
                return false;
            }
        });
        populateListView();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                char f = (char) 34;
                fornewdb.Replace(f+listData.get(position)+f, "1");
                populateListView();
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                char f = (char) 34;
                fornewdb.Replace(f+doneList.get(position)+f, "0");
                populateListView();
            }
        });
    }
    public void populateListView() {
        //get the data and append to a list
        Cursor data = fornewdb.getData();
        doneList = new ArrayList<>();
        listData = new ArrayList<>();
        listData.clear();
        while(data.moveToNext()) {
            if(!data.getString(2).contains("1")){
                listData.add( data.getString(1) );
            }
            else
            doneList.add( data.getString(1) );
        }
        ListAdapter adapter = new ArrayAdapter<>(this, R.layout.goaltodo, listData);
        listview.setAdapter(adapter);
        adapter = new ArrayAdapter<>(this, R.layout.donelist, doneList);
        list.setAdapter(adapter);
        if (listData.size() == 0 && doneList.size() > listData.size()){
            String name = ForNewDB.DB_name;
            MainActivity.rep(name);
        }
    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(ToDoHelper.this, MainActivity.class);
        startActivity(intent);
    }
}

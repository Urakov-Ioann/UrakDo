package com.example.urakdo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SwipeMenuListView listview_goals;
    ArrayList<String> goals = new ArrayList();
    ArrayAdapter adapter;
    final Context context = this;
    DataBaseHelper mDataBaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDataBaseHelper = new DataBaseHelper(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listview_goals = findViewById(R.id.listView);
        additem();
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem changeitem = new SwipeMenuItem(getApplicationContext());
                changeitem.setWidth(170);
                changeitem.setIcon(R.drawable.ic_change);
                changeitem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                menu.addMenuItem(changeitem);
                SwipeMenuItem deleteitem = new SwipeMenuItem(getApplicationContext());
                deleteitem.setWidth(170);
                deleteitem.setIcon(R.drawable.ic_delete);
                deleteitem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                menu.addMenuItem(deleteitem);
            }
        };
        listview_goals.setMenuCreator(creator);
        listview_goals.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
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
                                            public void onClick(DialogInterface dialog,int id) {
                                                final char dm = (char) 34;
                                                String name= dm+goals.get(position)+dm;
                                                mDataBaseHelper.Delete(name);
                                                mDataBaseHelper.Add(userInput.getText().toString());
                                                additem();
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
                        final char D = (char) 34;
                        String name = D+goals.get(position)+D;
                        mDataBaseHelper.Delete(name);
                        additem();
                        break;
                }
                return false;
            }
        });
        listview_goals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ForNewDB.DB_name = goals.get(position);
                Intent intent = new Intent(MainActivity.this, ToDoHelper.class);
                startActivity(intent);
            }
        });
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(context);
                View view1 = li.inflate(R.layout.add_goal, null);
                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
                mDialogBuilder.setView(view1);
                final EditText userinput = (EditText) view1.findViewById(R.id.input_text);
                userinput.setTextColor(Color.WHITE);
                mDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("ОК",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mDataBaseHelper.Add(userinput.getText().toString());
                                        additem();
                                    }
                                })
                        .setNegativeButton("ОТМЕНА",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = mDialogBuilder.create();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getColor(R.color.colorPrimaryDark)));
                }
                alertDialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void additem() {
        Cursor date = mDataBaseHelper.getData();
        goals.clear();
        while (date.moveToNext()) {
            goals.add(date.getString(1));
        }
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, goals);
        listview_goals.setAdapter(adapter);
}
}

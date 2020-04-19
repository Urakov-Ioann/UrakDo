package com.example.urakdo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SwipeMenuListView listview_goals;
    SwipeMenuListView list;
    ArrayList<String> goals = new ArrayList();
    ArrayList<String> goals_done = new ArrayList();
    private Switch mySwitch;
    ArrayAdapter adapter;
    final Context context = this;
    static DataBaseHelper mDataBaseHelper;
    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = new SharedPref(this);
        if(sharedPref.loadNigthState()){
            setTheme(R.style.darktheme);
            ToDoHelper.theme = 1;
        }
        else{
            setTheme(R.style.AppTheme);
            ToDoHelper.theme = 0;
        }
        setContentView(R.layout.activity_main);
        mySwitch = findViewById(R.id.switchh);
        if(sharedPref.loadNigthState()){
            mySwitch.setChecked(true);
        }
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sharedPref.setNigthModeState(true);
                    restartApp();
                }
                else{
                    sharedPref.setNigthModeState(false);
                    restartApp();
                }
            }
        });
        mDataBaseHelper = new DataBaseHelper(this);
        listview_goals = findViewById(R.id.list_view);
        list = findViewById(R.id.goaldone);
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
        SwipeMenuCreator creator1 = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                deleteItem.setWidth(170);
                deleteItem.setIcon(R.drawable.ic_delete);
                menu.addMenuItem(deleteItem);
            }
        };
        list.setMenuCreator(creator1);
        listview_goals.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index) {
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
                                                String name = dm + goals.get(position) + dm;
                                                mDataBaseHelper.Delete(name);
                                                String name_db=userInput.getText().toString();
                                                if(name_db.equals("")||name_db.contains(" ")||name_db.contains("1")||name_db.contains("2")||name_db.contains("3")||name_db.contains("4")||name_db.contains("5")||name_db.contains("6")||name_db.contains("7")||name_db.contains("8")||name_db.contains("9")||name_db.contains("0"))
                                                {
                                                    toastMessage("Name of goal cannot contain numbers and symbols");
                                                    dialog.cancel();

                                                }
                                                else{ mDataBaseHelper.Add(name_db);
                                                    additem();
                                                    toastMessage("Goal added");}
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
                        final char D = (char) 34;
                        String name = D + goals.get(position) + D;
                        mDataBaseHelper.Delete(name);
                        additem();
                        break;
                }
                return false;
            }
        });
        list.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        final char dm = (char) 34;
                        String name = dm + goals_done.get(position) + dm;
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
                                        String name_db=userinput.getText().toString();
                                        if(name_db.equals("")||name_db.contains(" ")||name_db.contains("1")||name_db.contains("2")||name_db.contains("3")||name_db.contains("4")||name_db.contains("5")||name_db.contains("6")||name_db.contains("7")||name_db.contains("8")||name_db.contains("9")||name_db.contains("0"))
                                        {
                                            toastMessage("Name of goal cannot contain numbers and symbols");
                                            dialog.cancel();

                                        }
                                        else{ mDataBaseHelper.Add(name_db);
                                            additem();
                                            toastMessage("Goal added");}
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getColor(R.color.colorG)));
                }
                alertDialog.show();
            }
        });
    }


    public void additem() {
        Cursor date = mDataBaseHelper.getData();
        goals.clear();
        goals_done.clear();
        while (date.moveToNext()) {
            if(! date.getString(2).contains("1")){
                goals.add(date.getString(1));
            }
            else goals_done.add(date.getString(1));
        }
        adapter = new ArrayAdapter<>(this, R.layout.goaltodo, goals);
        listview_goals.setAdapter(adapter);
        adapter = new ArrayAdapter<>(this, R.layout.donelist, goals_done);
        list.setAdapter(adapter);
    }

    public static void rep(String name){
        char dm = (char) 34;
        mDataBaseHelper.replace(dm + name + dm, "1");
    }
    public void restartApp(){
        MainActivity.this.recreate();
    }
    private void toastMessage(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}

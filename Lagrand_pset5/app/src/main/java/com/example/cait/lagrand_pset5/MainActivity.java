package com.example.cait.lagrand_pset5;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Group> groups;
    ToDoManager dbManager;
    ToDoExpandableListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Connect to the database
        dbManager = ToDoManager.getInstance(this);
    }

    protected void onResume() {
        super.onResume();
        setData();
        ExpandableListView listView = (ExpandableListView) findViewById(R.id.expandableListView);
        adapter = new ToDoExpandableListAdapter(this, groups);
        listView.setAdapter(adapter);
    }

    public void setData() {
        groups = new ArrayList<>();
        ArrayList<ToDoList> todoLists = dbManager.readToDoList();
        for (int i = 0; i < todoLists.size(); i++) {
            Group toDoItems = new Group(todoLists.get(i).getTitle(), todoLists.get(i).getId());
            toDoItems.subItems = todoLists.get(i).getToDoList();
            groups.add(toDoItems);
        }
    }

    public void toAdd(View view) {
        Intent toAdd = new Intent(this, AddActivity.class);
        startActivity(toAdd);
    }

}

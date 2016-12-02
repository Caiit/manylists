package com.example.cait.lagrand_pset5;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {

    ToDoManager dbManager;
    ArrayList<String> subItemNames = new ArrayList<>();
    ArrayList<ToDoItem> subToDos = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // Connect to the database
        dbManager = ToDoManager.getInstance(this);

        // Get list view
        ListView itemsListView = (ListView) findViewById(R.id.addListView);
        adapter = new ArrayAdapter<>(this, R.layout.activity_listview, R.id.subAddItemText, subItemNames);
        itemsListView.setAdapter(adapter);
    }


    public void addSubItem(View view) {
        // Get task info
        EditText subItem = (EditText) findViewById(R.id.todoItemEditText);

        ToDoItem todo = new ToDoItem(0, subItem.getText().toString(), false, 0);

        subToDos.add(todo);
        subItemNames.add(todo.getTitle());
        adapter.notifyDataSetChanged();
    }

    public void addItem(View view) {
        EditText item = (EditText) findViewById(R.id.taskEditText);

        ToDoList todo = new ToDoList(0, item.getText().toString(), subToDos);

        dbManager.createToDoList(todo);

        Toast.makeText(this, "Task added to todo list", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void cancelItem(View view) {
        finish();
    }
}

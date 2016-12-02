package com.example.cait.lagrand_pset5;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class EditActivity extends AppCompatActivity {

    ToDoManager dbManager;
    ArrayAdapter<String> adapter;
    ArrayList<ToDoItem> subToDos = new ArrayList<>();
    ArrayList<String> subItemNames = new ArrayList<>();
    String title;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Connect to the database
        dbManager = ToDoManager.getInstance(this);

        // Get group id
        Bundle extras = getIntent().getExtras();
        id = (int) extras.get("id");

        // Get title
        title = (String) extras.get("title");

        // Set sub to dos
        subToDos = dbManager.readToDoItem(id);

        // Set text for title
        EditText titleET = (EditText) findViewById(R.id.taskEditText);
        titleET.setText(title);

        setData();

        // Get list view
        ListView itemsListView = (ListView) findViewById(R.id.addListView);
        adapter = new ArrayAdapter<>(this, R.layout.activity_listview, R.id.subAddItemText, subItemNames);
        itemsListView.setAdapter(adapter);
    }

    private void setData() {
        // Set current to do items
        subToDos = dbManager.readToDoItem(id);
        for (int i = 0; i < subToDos.size(); i++) {
            subItemNames.add(subToDos.get(i).getTitle());
        }
    }

    public void deleteItem(View view) {
        // Delete item from database
        dbManager.deleteToDoList(id);

        // Go back to home page
        finish();
    }

    public void deleteSubItem(View view) {
        // Get item
        TextView nameTV = (TextView) ((LinearLayout) view.getParent()).getChildAt(0);
        String name = nameTV.getText().toString();
        ToDoItem item = subToDos.get(subItemNames.indexOf(name));
        subItemNames.remove(name);
        // Delete item from database
        dbManager.deleteToDoItem(item.getId());
        subToDos.remove(item);
        adapter.notifyDataSetChanged();
    }

    public void addSubItem(View view) {
        // Get subitem info
        EditText subItem = (EditText) findViewById(R.id.todoItemEditText);
        ToDoItem todo = new ToDoItem(0, subItem.getText().toString(), false, 0);
        // Add item to the database
        dbManager.createToDoItem(todo, id);
        subToDos.add(todo);
        subItemNames.add(subItem.getText().toString());
        adapter.notifyDataSetChanged();
    }

    public void addItem(View view) {
        EditText titleET = (EditText) findViewById(R.id.taskEditText);
        title = titleET.getText().toString();

        // Update database
        ToDoList todo = new ToDoList(id, title, subToDos);
        dbManager.updateToDoList(todo);
        finish();
    }

    public void cancelItem(View view) {
        finish();
    }
}

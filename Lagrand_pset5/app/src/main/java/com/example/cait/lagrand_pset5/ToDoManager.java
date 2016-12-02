package com.example.cait.lagrand_pset5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

class ToDoManager extends SQLiteOpenHelper  {

    // Set fields of database schemas
    private static final String DATABASE_NAME = "toDoListDB.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_TODO_LIST = "toDoList";
    private static final String TABLE_TODO_ITEMS = "toDoItems";

    // Set table to do list names
    private static final String KEY_TODO_LIST_ID = "todoListID";
    private static final String KEY_TODO_LIST_TITLE = "todolistTitle";

    // Set table to do items names
    private static final String KEY_TODO_ITEM_ID = "todoItemID";
    private static final String KEY_TODO_ITEM_TITLE = "todoItemTitle";
    private static final String KEY_TODO_ITEM_COMPLETED = "completed";

    // Set create tables
    private static final String CREATE_TABLE_TODO_LIST = "CREATE TABLE " + TABLE_TODO_LIST +
                                    "(" + KEY_TODO_LIST_ID + " INTEGER PRIMARY KEY," +
                                    KEY_TODO_LIST_TITLE + " TEXT)";
    private static final String CREATE_TABLE_TODO_ITEMS = "CREATE TABLE " + TABLE_TODO_ITEMS +
                                    "(" + KEY_TODO_ITEM_ID + " INTEGER PRIMARY KEY," +
                                    KEY_TODO_ITEM_TITLE + " TEXT, " +
                                    KEY_TODO_ITEM_COMPLETED + " INTEGER, " +
                                    KEY_TODO_LIST_ID + " INTEGER)";

    private static ToDoManager ourInstance;

    static ToDoManager getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new ToDoManager(context.getApplicationContext());
        }
        return ourInstance;
    }

    private ToDoManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // OnCreate
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create the tables
        sqLiteDatabase.execSQL(CREATE_TABLE_TODO_LIST);
        sqLiteDatabase.execSQL(CREATE_TABLE_TODO_ITEMS);
    }

    // OnUpgrade
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO_LIST);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO_ITEMS);
        onCreate(sqLiteDatabase);
    }

    // Create todolist
    void createToDoList(ToDoList todoList) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TODO_LIST_TITLE, todoList.getTitle());
        long id = db.insert(TABLE_TODO_LIST, null, values);
        ArrayList<ToDoItem> toDoItems = todoList.getToDoList();
        for (int i = 0; i < toDoItems.size(); i++) {
            createToDoItem(toDoItems.get(i), (int) id);
        }
        db.close();
    }

    // Create todoitem
    void createToDoItem(ToDoItem todoItem, int id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TODO_ITEM_TITLE, todoItem.getTitle());
        values.put(KEY_TODO_ITEM_COMPLETED, todoItem.getCompleted());
        values.put(KEY_TODO_LIST_ID, id);
        db.insert(TABLE_TODO_ITEMS, null, values);
        db.close();
    }

    // Read todoList
    ArrayList<ToDoList> readToDoList() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " + KEY_TODO_LIST_ID + " , " + KEY_TODO_LIST_TITLE +
                       " FROM " + TABLE_TODO_LIST;
        ArrayList<ToDoList> todos = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {

                int id = cursor.getInt(cursor.getColumnIndex(KEY_TODO_LIST_ID));
                String title = cursor.getString(cursor.getColumnIndex(KEY_TODO_LIST_TITLE));
                ArrayList<ToDoItem> todoItems = readToDoItem(id);
                ToDoList todo = new ToDoList(id, title, todoItems);
                todos.add(todo);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return todos;
    }

    // Read todoItem
    ArrayList<ToDoItem> readToDoItem(int todoListID) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " + KEY_TODO_ITEM_ID + " , " + KEY_TODO_ITEM_TITLE + " , " +
                       KEY_TODO_ITEM_COMPLETED + " , " + KEY_TODO_LIST_ID + " FROM " +
                       TABLE_TODO_ITEMS + " WHERE " + KEY_TODO_LIST_ID + " = " + todoListID;
        ArrayList<ToDoItem> todoItems = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {

                int id = cursor.getInt(cursor.getColumnIndex(KEY_TODO_ITEM_ID));
                String title = cursor.getString(cursor.getColumnIndex(KEY_TODO_ITEM_TITLE));
                int completed = cursor.getInt(cursor.getColumnIndex(KEY_TODO_ITEM_COMPLETED));
                int listID = cursor.getInt(cursor.getColumnIndex(KEY_TODO_LIST_ID));
                ToDoItem todo = new ToDoItem(id, title, completed == 1, listID);
                todoItems.add(todo);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return todoItems;
    }

    // Update todolist
    void updateToDoList(ToDoList todo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TODO_LIST_TITLE, todo.getTitle());
        db.update(TABLE_TODO_LIST, values, KEY_TODO_LIST_ID + " = ? ", new String[] {String.valueOf(todo.getId())});
        db.close();
    }

    // Update todoitem
    void updateToDoItem(ToDoItem todoItem) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TODO_ITEM_TITLE, todoItem.getTitle());
        values.put(KEY_TODO_ITEM_COMPLETED, todoItem.getCompleted());
        values.put(KEY_TODO_LIST_ID, todoItem.getListID());
        db.update(TABLE_TODO_ITEMS, values, KEY_TODO_ITEM_ID + " = ? ", new String[] {String.valueOf(todoItem.getId())});
        db.close();
    }

    // Delete todolist
    void deleteToDoList(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_TODO_LIST, KEY_TODO_LIST_ID + " = ? ", new String[] {String.valueOf(id)});
        deleteToDoItemFromToDoList(id);
        db.close();
    }

    // Delete todoitem
    void deleteToDoItem(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_TODO_ITEMS, KEY_TODO_ITEM_ID + " = ? ", new String[] {String.valueOf(id)});
        db.close();
    }

    // Delete todoitem
    private void deleteToDoItemFromToDoList(int toDoListId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_TODO_ITEMS, KEY_TODO_LIST_ID + " = ? ", new String[] {String.valueOf(toDoListId)});
        db.close();
    }
}

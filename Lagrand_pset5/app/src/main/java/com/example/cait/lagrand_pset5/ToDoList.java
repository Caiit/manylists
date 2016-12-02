package com.example.cait.lagrand_pset5;

import java.util.ArrayList;

class ToDoList {

    private int id;
    private String title;
    private ArrayList<ToDoItem> todos;

    // Constructor
    ToDoList(int id, String title, ArrayList<ToDoItem> todos) {
        this.id = id;
        this.title = title;
        this.todos = todos;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String getTitle() {
        return title;
    }

    public void setToDoList(ArrayList<ToDoItem> todos) {
        this.todos = todos;
    }

    ArrayList<ToDoItem> getToDoList() {
        return todos;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}


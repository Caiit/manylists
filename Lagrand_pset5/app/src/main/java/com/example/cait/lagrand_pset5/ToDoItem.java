package com.example.cait.lagrand_pset5;


class ToDoItem {

    private int id;
    private String title;
    private boolean completed;
    private int listID;

    // Constructor
    ToDoItem(int id, String title, boolean completed, int listID) {
        this.id = id;
        this.title = title;
        this.completed = completed;
        this.listID = listID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String getTitle() {
        return title;
    }

    void setCompleted(boolean completed) {
        this.completed = completed;
    }

    boolean getCompleted() {
        return completed;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setListID(int id) {
        this.listID = id;
    }

    int getListID() {
        return listID;
    }
}

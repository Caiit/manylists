package com.example.cait.lagrand_pset5;

import java.util.ArrayList;
import java.util.List;

class Group {

    public String string;
    public int id;
    List<ToDoItem> subItems = new ArrayList<>();

    Group(String string, int id) {
        this.string = string;
        this.id = id;
    }
}
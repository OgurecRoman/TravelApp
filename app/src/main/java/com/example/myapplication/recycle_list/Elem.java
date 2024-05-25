package com.example.myapplication.recycle_list;

public class Elem {
    String name;
    Boolean on;

    public Elem(){
        this.name = "";
        this.on = false;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOn(Boolean on) {
        this.on = on;
    }

    public String getName() {
        return name;
    }

    public Boolean getOn() {
        return on;
    }
}

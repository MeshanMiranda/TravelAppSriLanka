package com.example.travel.Domain;

public class Location {
    private int Id;
    private String loc;

    public Location() {

    }

    @Override
    public String toString() {
        return loc;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }
}

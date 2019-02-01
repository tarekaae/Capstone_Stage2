package com.tarek.nanodegree.capstone.fishing.model.pojo;

import java.util.List;

/**
 * Created by tarek.abdulkader on 3/4/2018.
 */

public class Spot {

    private long id;
    private int depth;
    private List<String> species;
    private String dateTime;
    private double lat;
    private double lng;

    public Spot() {
    }

    public Spot(long id, int depth, List<String> species, String dateTime, double lat, double lng) {
        this.id = id;
        this.depth = depth;
        this.species = species;
        this.dateTime = dateTime;
        this.lat = lat;
        this.lng = lng;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public List<String> getSpecies() {
        return species;
    }

    public void setSpecies(List<String> species) {
        this.species = species;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
